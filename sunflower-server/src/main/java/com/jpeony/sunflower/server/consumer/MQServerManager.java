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
public class MQServerManager {
    private final NettyServerConfig nettyServerConfig;
    private RemotingServer remotingServer;
    private ExecutorService remotingExecutor;

    public MQServerManager(NettyServerConfig nettyServerConfig) {
        this.nettyServerConfig = nettyServerConfig;
    }

    public static MQServerManager createMQServerManager(String[] args) {
        final NettyServerConfig nettyServerConfig = new NettyServerConfig();
        final MQServerManager mqServerManager = new MQServerManager(nettyServerConfig);

        return mqServerManager;
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
        this.remotingServer.registerProcessor(RequestCode.SEND_ERROR_MONITOR_MESSAGE, new ErrorProcessor(), this.remotingExecutor);
        this.remotingServer.registerProcessor(RequestCode.SEND_JVM_MONITOR_MESSAGE, new JVMProcessor(), this.remotingExecutor);
        this.remotingServer.registerProcessor(RequestCode.SEND_HTTP_MONITOR_MESSAGE, new HttpProcessor(), this.remotingExecutor);
        this.remotingServer.registerProcessor(RequestCode.SEND_MYSQL_MONITOR_MESSAGE, new MySqlProcessor(), this.remotingExecutor);
        this.remotingServer.registerProcessor(RequestCode.SEND_REDIS_MONITOR_MESSAGE, new RedisProcessor(), this.remotingExecutor);
        this.remotingServer.registerProcessor(RequestCode.SEND_MONGODB_MONITOR_MESSAGE, new MongodbProcessor(), this.remotingExecutor);
        this.remotingServer.registerProcessor(RequestCode.SEND_ES_MONITOR_MESSAGE, new ESProcessor(), this.remotingExecutor);
        this.remotingServer.registerProcessor(RequestCode.SEND_CPU_MONITOR_MESSAGE, new CPUProcessor(), this.remotingExecutor);
        this.remotingServer.registerProcessor(RequestCode.SEND_IO_MONITOR_MESSAGE, new IOProcessor(), this.remotingExecutor);
    }

    public void start() {
        this.remotingServer.start();
    }

    public void shutdown() {
        this.remotingServer.shutdown();
        this.remotingExecutor.shutdown();
    }
}
