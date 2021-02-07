package com.jpeony.sunflower.agent.producer;

import com.jpeony.sunflower.agent.enums.ServiceState;
import com.jpeony.sunflower.remoting.netty.NettyClientConfig;

/**
 * @author yihonglei
 */
public class MQClientInstance {
    private final NettyClientConfig nettyClientConfig;
    private final MQProducer MQProducer;
    private ServiceState serviceState = ServiceState.CREATE_JUST;

    public MQClientInstance() {
        this.nettyClientConfig = new NettyClientConfig();
        this.MQProducer = new MQProducer(nettyClientConfig);
    }

    public void start() {
        synchronized (this) {
            switch (this.serviceState) {
                case CREATE_JUST:
                    // Start request channel
                    this.MQProducer.start();
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
                    this.MQProducer.shutdown();
                    break;
                case SHUTDOWN_ALREADY:
                    break;
                default:
                    break;
            }
        }
    }

    public MQProducer getMQProducer() {
        return MQProducer;
    }
}
