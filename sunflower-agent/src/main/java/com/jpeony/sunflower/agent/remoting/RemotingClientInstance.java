package com.jpeony.sunflower.agent.remoting;

import com.jpeony.sunflower.agent.enums.ServiceState;
import com.jpeony.sunflower.remoting.netty.NettyClientConfig;

/**
 * @author yihonglei
 */
public class RemotingClientInstance {
    private final NettyClientConfig nettyClientConfig;
    private final RemotingClientAPIImpl remotingClientAPIImpl;
    private ServiceState serviceState = ServiceState.CREATE_JUST;

    public RemotingClientInstance() {
        this.nettyClientConfig = new NettyClientConfig();
        this.remotingClientAPIImpl = new RemotingClientAPIImpl(nettyClientConfig);
    }

    public void start() {
        System.out.println("agent-start......");
        synchronized (this) {
            switch (this.serviceState) {
                case CREATE_JUST:
                    // Start request channel
                    this.remotingClientAPIImpl.start();
                    serviceState = ServiceState.RUNNING;
                    break;
                case START_FAILED:
                    System.out.println("start failed");
                    break;
                default:
                    break;
            }
        }
    }

    public void shutdown() {
        System.out.println("agent-shutdown......");
        synchronized (this.serviceState) {
            switch (this.serviceState) {
                case CREATE_JUST:
                    break;
                case RUNNING:
                    this.serviceState = ServiceState.SHUTDOWN_ALREADY;
                    this.remotingClientAPIImpl.shutdown();
                    break;
                case SHUTDOWN_ALREADY:
                    break;
                default:
                    break;
            }
        }
    }

    public RemotingClientAPIImpl getRemotingClientAPIImpl() {
        return remotingClientAPIImpl;
    }
}
