package com.jpeony.sunflower.agent.remoting;

import com.jpeony.sunflower.remoting.RemotingClient;
import com.jpeony.sunflower.remoting.exception.RemotingConnectException;
import com.jpeony.sunflower.remoting.exception.RemotingSendRequestException;
import com.jpeony.sunflower.remoting.exception.RemotingTimeoutException;
import com.jpeony.sunflower.remoting.exception.RemotingTooMuchRequestException;
import com.jpeony.sunflower.remoting.netty.NettyClientConfig;
import com.jpeony.sunflower.remoting.netty.NettyRemotingClient;
import com.jpeony.sunflower.remoting.protocol.RemotingCommand;

/**
 * @author yihonglei
 */
public class RemotingClientAPIImpl {
    private final RemotingClient remotingClient;

    public RemotingClientAPIImpl(final NettyClientConfig nettyClientConfig) {
        remotingClient = new NettyRemotingClient(nettyClientConfig);
    }

    public void start() {
        this.remotingClient.start();
    }

    public void shutdown() {
        this.remotingClient.shutdown();
    }

    public void sendMessage(final String addr, final RemotingCommand request, final long timeoutMillis) {
        System.out.println("agent-sendMessage");
        try {
            remotingClient.invokeAsync(addr, request, timeoutMillis);
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
}
