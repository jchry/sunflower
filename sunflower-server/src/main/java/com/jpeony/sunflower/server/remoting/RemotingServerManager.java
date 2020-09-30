package com.jpeony.sunflower.server.remoting;

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
public class RemotingServerManager {
    private final NettyServerConfig nettyServerConfig;
    private RemotingServer remotingServer;
    private ExecutorService remotingExecutor;

    public RemotingServerManager() {
        this.nettyServerConfig = new NettyServerConfig();
    }

    public boolean initialize() {
        remotingServer = new NettyRemotingServer(nettyServerConfig);

        this.remotingExecutor = Executors.newFixedThreadPool(nettyServerConfig.getServerWorkerThreads(),
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
        remotingServer.registerProcessor(RequestCode.SEND_ERROR_MONITOR_MESSAGE, new ErrorProcessor(), remotingExecutor);
        remotingServer.registerProcessor(RequestCode.SEND_JVM_MONITOR_MESSAGE, new JVMProcessor(), remotingExecutor);
        remotingServer.registerProcessor(RequestCode.SEND_HTTP_MONITOR_MESSAGE, new HttpProcessor(), remotingExecutor);
        remotingServer.registerProcessor(RequestCode.SEND_MYSQL_MONITOR_MESSAGE, new MySqlProcessor(), remotingExecutor);
        remotingServer.registerProcessor(RequestCode.SEND_REDIS_MONITOR_MESSAGE, new RedisProcessor(), remotingExecutor);
        remotingServer.registerProcessor(RequestCode.SEND_MONGODB_MONITOR_MESSAGE, new MongodbProcessor(), remotingExecutor);
        remotingServer.registerProcessor(RequestCode.SEND_ES_MONITOR_MESSAGE, new ESProcessor(), remotingExecutor);
        remotingServer.registerProcessor(RequestCode.SEND_CPU_MONITOR_MESSAGE, new CPUProcessor(), remotingExecutor);
        remotingServer.registerProcessor(RequestCode.SEND_IO_MONITOR_MESSAGE, new IOProcessor(), remotingExecutor);
    }

    public void start() {
        boolean initResult = this.initialize();

        if (!initResult) {
            shutdown();
        }

        remotingServer.start();
    }

    public void shutdown() {
        remotingServer.shutdown();
        remotingExecutor.shutdown();
    }
}
