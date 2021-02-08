package com.jpeony.sunflower.remoting.netty;

import com.jpeony.sunflower.remoting.RemotingClient;
import com.jpeony.sunflower.remoting.common.RemotingHelper;
import com.jpeony.sunflower.remoting.common.RemotingUtil;
import com.jpeony.sunflower.remoting.common.SemaphoreReleaseOnlyOnce;
import com.jpeony.sunflower.remoting.exception.RemotingConnectException;
import com.jpeony.sunflower.remoting.exception.RemotingSendRequestException;
import com.jpeony.sunflower.remoting.exception.RemotingTimeoutException;
import com.jpeony.sunflower.remoting.exception.RemotingTooMuchRequestException;
import com.jpeony.sunflower.remoting.protocol.RemotingCommand;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yihonglei
 */
public class NettyRemotingClient extends AbstractNettyRemoting implements RemotingClient {

    private static final long LOCK_TIMEOUT_MILLIS = 3000;

    private final Bootstrap bootstrap;
    private final NettyClientConfig nettyClientConfig;
    private final EventLoopGroup eventLoopGroupWorker;
    private final Lock lockChannelTables = new ReentrantLock();
    private final ConcurrentMap<String /* addr */, ChannelWrapper> channelTables = new ConcurrentHashMap<>();

    private final ExecutorService publicExecutor;

    private DefaultEventExecutorGroup defaultEventExecutorGroup;

    public NettyRemotingClient(final NettyClientConfig nettyClientConfig) {
        super(nettyClientConfig.getClientAsyncSemaphoreValue());
        bootstrap = new Bootstrap();
        this.nettyClientConfig = nettyClientConfig;

        int publicThreadNums = nettyClientConfig.getClientCallbackExecutorThreads();
        if (publicThreadNums <= 0) {
            publicThreadNums = 4;
        }

        this.publicExecutor = Executors.newFixedThreadPool(publicThreadNums, new ThreadFactory() {
            private AtomicInteger threadIndex = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "NettyClientPublicExecutor_" + this.threadIndex.incrementAndGet());
            }
        });

        this.eventLoopGroupWorker = new NioEventLoopGroup(1, new ThreadFactory() {
            private AtomicInteger threadIndex = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, String.format("NettyClientSelector_%d", this.threadIndex.incrementAndGet()));
            }
        });
    }

    @Override
    public void start() {
        this.defaultEventExecutorGroup = new DefaultEventExecutorGroup(
                nettyClientConfig.getClientWorkerThreads(),
                new ThreadFactory() {

                    private AtomicInteger threadIndex = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "NettyClientWorkerThread_" + this.threadIndex.incrementAndGet());
                    }
                });

        this.bootstrap.group(this.eventLoopGroupWorker).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, nettyClientConfig.getConnectTimeoutMillis())
                .option(ChannelOption.SO_SNDBUF, nettyClientConfig.getClientSocketSndBufSize())
                .option(ChannelOption.SO_RCVBUF, nettyClientConfig.getClientSocketRcvBufSize())
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(
                                defaultEventExecutorGroup,
                                new NettyEncoder(),
                                new NettyDecoder(),
                                new NettyConnectManageHandler(),
                                new NettyClientHandler());
                    }
                });
    }

    @Override
    public void shutdown() {
        try {
            for (ChannelWrapper cw : this.channelTables.values()) {
                this.closeChannel(null, cw.getChannel());
            }

            this.channelTables.clear();

            this.eventLoopGroupWorker.shutdownGracefully();

            if (this.defaultEventExecutorGroup != null) {
                this.defaultEventExecutorGroup.shutdownGracefully();
            }
        } catch (Exception e) {

        }

        if (this.publicExecutor != null) {
            try {
                this.publicExecutor.shutdown();
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void invokeOneway(String addr, RemotingCommand request, long timeoutMillis)
            throws InterruptedException, RemotingConnectException, RemotingTooMuchRequestException,
            RemotingTimeoutException, RemotingSendRequestException {
        final Channel channel = this.getAndCreateChannel(addr);
        if (channel != null && channel.isActive()) {
            try {
                this.invokeOnewayImpl(channel, request, timeoutMillis);
            } catch (RemotingSendRequestException e) {
                this.closeChannel(addr, channel);
                throw e;
            }
        } else {
            this.closeChannel(addr, channel);
            throw new RemotingConnectException(addr);
        }
    }

    private Channel getAndCreateChannel(final String addr) throws RemotingConnectException, InterruptedException {

        ChannelWrapper cw = this.channelTables.get(addr);
        if (cw != null && cw.isOK()) {
            return cw.getChannel();
        }

        return this.createChannel(addr);
    }

    private Channel createChannel(final String addr) throws InterruptedException {
        ChannelWrapper cw = this.channelTables.get(addr);
        if (cw != null && cw.isOK()) {
            return cw.getChannel();
        }

        if (this.lockChannelTables.tryLock(LOCK_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {
            try {
                boolean createNewConnection;
                cw = this.channelTables.get(addr);
                if (cw != null) {

                    if (cw.isOK()) {
                        return cw.getChannel();
                    } else if (!cw.getChannelFuture().isDone()) {
                        createNewConnection = false;
                    } else {
                        this.channelTables.remove(addr);
                        createNewConnection = true;
                    }
                } else {
                    createNewConnection = true;
                }

                if (createNewConnection) {
                    ChannelFuture channelFuture = this.bootstrap.connect(RemotingHelper.string2SocketAddress(addr));
                    cw = new ChannelWrapper(channelFuture);
                    this.channelTables.put(addr, cw);
                }
            } catch (Exception e) {

            } finally {
                this.lockChannelTables.unlock();
            }
        } else {

        }

        if (cw != null) {
            ChannelFuture channelFuture = cw.getChannelFuture();
            if (channelFuture.awaitUninterruptibly(this.nettyClientConfig.getConnectTimeoutMillis())) {
                if (cw.isOK()) {
                    return cw.getChannel();
                } else {

                }
            } else {

            }
        }

        return null;
    }

    public void invokeOnewayImpl(final Channel channel, final RemotingCommand request, final long timeoutMillis)
            throws InterruptedException, RemotingTooMuchRequestException, RemotingTimeoutException, RemotingSendRequestException {
        boolean acquired = this.semaphoreOneway.tryAcquire(timeoutMillis, TimeUnit.MILLISECONDS);
        if (acquired) {
            final SemaphoreReleaseOnlyOnce once = new SemaphoreReleaseOnlyOnce(this.semaphoreOneway);
            try {
                channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture f) throws Exception {
                        once.release();
                        if (!f.isSuccess()) {
                            System.out.println("send failed");
                        }
                    }
                });
            } catch (Exception e) {
                once.release();
                throw new RemotingSendRequestException(RemotingHelper.parseChannelRemoteAddr(channel), e);
            }
        } else {
            if (timeoutMillis <= 0) {
                throw new RemotingTooMuchRequestException("invokeAsyncImpl invoke too fast");
            } else {
                String info = String.format(
                        "invokeAsyncImpl tryAcquire semaphore timeout, %dms, waiting thread nums: %d semaphoreAsyncValue: %d",
                        timeoutMillis,
                        this.semaphoreOneway.getQueueLength(),
                        this.semaphoreOneway.availablePermits()
                );
                throw new RemotingTimeoutException(info);
            }
        }
    }

    public void closeChannel(final Channel channel) {
        if (null == channel) {
            return;
        }

        try {
            if (this.lockChannelTables.tryLock(LOCK_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {
                try {
                    boolean removeItemFromTable = true;
                    ChannelWrapper prevCW = null;
                    String addrRemote = null;
                    for (Map.Entry<String, ChannelWrapper> entry : channelTables.entrySet()) {
                        String key = entry.getKey();
                        ChannelWrapper prev = entry.getValue();
                        if (prev.getChannel() != null) {
                            if (prev.getChannel() == channel) {
                                prevCW = prev;
                                addrRemote = key;
                                break;
                            }
                        }
                    }

                    if (null == prevCW) {
                        removeItemFromTable = false;
                    }

                    if (removeItemFromTable) {
                        this.channelTables.remove(addrRemote);
                        RemotingUtil.closeChannel(channel);
                    }
                } catch (Exception e) {

                } finally {
                    this.lockChannelTables.unlock();
                }
            } else {

            }
        } catch (InterruptedException e) {

        }
    }

    public void closeChannel(final String addr, final Channel channel) {
        if (null == channel) {
            return;
        }

        final String addrRemote = null == addr ? RemotingHelper.parseChannelRemoteAddr(channel) : addr;

        try {
            if (this.lockChannelTables.tryLock(LOCK_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {
                try {
                    boolean removeItemFromTable = true;
                    final ChannelWrapper prevCW = this.channelTables.get(addrRemote);

                    if (null == prevCW) {
                        removeItemFromTable = false;
                    } else if (prevCW.getChannel() != channel) {
                        removeItemFromTable = false;
                    }

                    if (removeItemFromTable) {
                        this.channelTables.remove(addrRemote);
                    }

                    RemotingUtil.closeChannel(channel);
                } catch (Exception e) {

                } finally {
                    this.lockChannelTables.unlock();
                }
            } else {

            }
        } catch (InterruptedException e) {

        }
    }

    static class ChannelWrapper {
        private final ChannelFuture channelFuture;

        public ChannelWrapper(ChannelFuture channelFuture) {
            this.channelFuture = channelFuture;
        }

        public boolean isOK() {
            return this.channelFuture.channel() != null && this.channelFuture.channel().isActive();
        }

        public boolean isWritable() {
            return this.channelFuture.channel().isWritable();
        }

        private Channel getChannel() {
            return this.channelFuture.channel();
        }

        public ChannelFuture getChannelFuture() {
            return channelFuture;
        }
    }

    class NettyClientHandler extends SimpleChannelInboundHandler<RemotingCommand> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, RemotingCommand msg) throws Exception {
            System.out.println("NettyClientHandler-channelRead0");
            processMessageReceived(ctx, msg);
        }
    }

    class NettyConnectManageHandler extends ChannelDuplexHandler {
        @Override
        public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress,
                            ChannelPromise promise) throws Exception {
            final String local = localAddress == null ? "UNKNOWN" : RemotingHelper.parseSocketAddressAddr(localAddress);
            final String remote = remoteAddress == null ? "UNKNOWN" : RemotingHelper.parseSocketAddressAddr(remoteAddress);
            System.out.println("NETTY CLIENT PIPELINE: CONNECT " + local + ", remote " + remote);

            super.connect(ctx, remoteAddress, localAddress, promise);
        }

        @Override
        public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
            final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            System.out.println("NETTY CLIENT PIPELINE: DISCONNECT " + remoteAddress);
            closeChannel(ctx.channel());
            super.disconnect(ctx, promise);
        }

        @Override
        public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
            final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            System.out.println("NETTY CLIENT PIPELINE: CLOSE " + remoteAddress);
            closeChannel(ctx.channel());
            super.close(ctx, promise);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            final String remoteAddress = RemotingHelper.parseChannelRemoteAddr(ctx.channel());
            System.out.println("NETTY CLIENT PIPELINE: exceptionCaught " + remoteAddress);
            closeChannel(ctx.channel());
        }
    }
}
