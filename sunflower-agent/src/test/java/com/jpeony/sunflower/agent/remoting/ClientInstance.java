package com.jpeony.sunflower.agent.remoting;

import com.jpeony.sunflower.agent.enums.ServiceState;
import com.jpeony.sunflower.remoting.netty.NettyClientConfig;

/**
 * @author yihonglei
 */
public class ClientInstance {
    private final NettyClientConfig nettyClientConfig;
    private final ClientAPIImpl clientAPIImpl;
    private ServiceState serviceState = ServiceState.CREATE_JUST;

    public ClientInstance() {
        this.nettyClientConfig = new NettyClientConfig();
        this.clientAPIImpl = new ClientAPIImpl(nettyClientConfig);
    }

    public void start() {
        System.out.println("agent-start......");
        synchronized (this) {
            switch (this.serviceState) {
                case CREATE_JUST:
                    // Start request channel
                    this.clientAPIImpl.start();
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
                    this.clientAPIImpl.shutdown();
                    break;
                case SHUTDOWN_ALREADY:
                    break;
                default:
                    break;
            }
        }
    }

    public ClientAPIImpl getClientAPIImpl() {
        return clientAPIImpl;
    }
}
