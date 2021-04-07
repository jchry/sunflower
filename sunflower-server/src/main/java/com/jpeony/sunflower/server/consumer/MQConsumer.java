package com.jpeony.sunflower.server.consumer;

import com.jpeony.sunflower.common.protocol.RequestCode;
import com.jpeony.sunflower.remoting.RemotingServer;
import com.jpeony.sunflower.remoting.netty.NettyRemotingServer;
import com.jpeony.sunflower.remoting.netty.NettyServerConfig;
import com.jpeony.sunflower.server.processor.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yihonglei
 */
public class MQConsumer {
    private final NettyServerConfig nettyServerConfig;
    private RemotingServer remotingServer;
    private ExecutorService remotingExecutor;

    public MQConsumer(NettyServerConfig nettyServerConfig) {
        this.nettyServerConfig = nettyServerConfig;
    }

    public static MQConsumer createMQServerManager(String[] args) {
        // TODO Dynamic acquire configuration
        final NettyServerConfig nettyServerConfig = new NettyServerConfig();
        final MQConsumer mqConsumer = new MQConsumer(nettyServerConfig);

        return mqConsumer;
    }

    public boolean initialize() {
        this.remotingServer = new NettyRemotingServer(this.nettyServerConfig);

        this.remotingExecutor = Executors.newFixedThreadPool(this.nettyServerConfig.getServerWorkerThreads(),
                new ThreadFactory() {
                    private AtomicInteger threadIndex = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, String.format("RemotingExecutorThread_%d", this.threadIndex.incrementAndGet()));
                    }
                });

        this.registerProcessor();

        return true;
    }

    private void registerProcessor() {
        this.remotingServer.registerProcessor(RequestCode.SEND_ERROR_MESSAGE, new ErrorProcessor(), this.remotingExecutor);
        this.remotingServer.registerProcessor(RequestCode.SEND_JVM_MESSAGE, new JVMProcessor(), this.remotingExecutor);
        this.remotingServer.registerProcessor(RequestCode.SEND_HTTP_MESSAGE, new HttpProcessor(), this.remotingExecutor);
        this.remotingServer.registerProcessor(RequestCode.SEND_MYSQL_MESSAGE, new MySqlProcessor(), this.remotingExecutor);
        this.remotingServer.registerProcessor(RequestCode.SEND_REDIS_MESSAGE, new RedisProcessor(), this.remotingExecutor);
        this.remotingServer.registerProcessor(RequestCode.SEND_MONGODB_MESSAGE, new MongodbProcessor(), this.remotingExecutor);
        this.remotingServer.registerProcessor(RequestCode.SEND_ES_MESSAGE, new ESProcessor(), this.remotingExecutor);
        this.remotingServer.registerProcessor(RequestCode.SEND_CPU_MESSAGE, new CPUProcessor(), this.remotingExecutor);
    }

    public void start() {
        this.remotingServer.start();
    }

    public void shutdown() {
        this.remotingServer.shutdown();
        this.remotingExecutor.shutdown();
    }
}
