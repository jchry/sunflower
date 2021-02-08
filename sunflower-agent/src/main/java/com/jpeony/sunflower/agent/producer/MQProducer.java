package com.jpeony.sunflower.agent.producer;

import com.jpeony.sunflower.agent.enums.ServiceState;
import com.jpeony.sunflower.remoting.RemotingClient;
import com.jpeony.sunflower.remoting.common.RemotingUtil;
import com.jpeony.sunflower.remoting.exception.RemotingConnectException;
import com.jpeony.sunflower.remoting.exception.RemotingSendRequestException;
import com.jpeony.sunflower.remoting.exception.RemotingTimeoutException;
import com.jpeony.sunflower.remoting.exception.RemotingTooMuchRequestException;
import com.jpeony.sunflower.remoting.netty.NettyClientConfig;
import com.jpeony.sunflower.remoting.netty.NettyRemotingClient;
import com.jpeony.sunflower.remoting.protocol.RemotingCommand;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author yihonglei
 */
public class MQProducer {
    private static MQProducer instance = new MQProducer();
    private ServiceState serviceState = ServiceState.CREATE_JUST;
    private final RemotingClient remotingClient;
    /**
     * Timeout for sending messages
     */
    private int sendMsgTimeout = 3000;

    private MQProducer() {
        final NettyClientConfig nettyClientConfig = new NettyClientConfig();
        remotingClient = new NettyRemotingClient(nettyClientConfig);
    }

    public static MQProducer getInstance() {
        return instance;
    }

    public void start() {
        synchronized (this.serviceState) {
            switch (this.serviceState) {
                case CREATE_JUST:
                    this.serviceState = ServiceState.START_FAILED;
                    this.remotingClient.start();
                    // Start request channel
                    this.serviceState = ServiceState.RUNNING;
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
        synchronized (this.serviceState) {
            switch (this.serviceState) {
                case CREATE_JUST:
                    break;
                case RUNNING:
                    this.serviceState = ServiceState.SHUTDOWN_ALREADY;
                    this.remotingClient.shutdown();
                    break;
                case SHUTDOWN_ALREADY:
                    break;
                default:
                    break;
            }
        }
    }

    public void send(final String addr, final RemotingCommand request, final long timeoutMillis) {
        try {
            remotingClient.invokeOneway(addr, request, timeoutMillis);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException");
        } catch (RemotingConnectException e) {
            System.out.println("RemotingConnectException");
        } catch (RemotingTooMuchRequestException e) {
            System.out.println("RemotingTooMuchRequestException");
        } catch (RemotingTimeoutException e) {
            System.out.println("RemotingTimeoutException");
        } catch (RemotingSendRequestException e) {
            System.out.println("RemotingSendRequestException");
        }
    }

    public int getSendMsgTimeout() {
        return sendMsgTimeout;
    }

    public void setSendMsgTimeout(int sendMsgTimeout) {
        this.sendMsgTimeout = sendMsgTimeout;
    }
}
