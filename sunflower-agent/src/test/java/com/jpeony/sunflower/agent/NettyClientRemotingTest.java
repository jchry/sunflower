package com.jpeony.sunflower.agent;

import com.jpeony.sunflower.common.protocol.RequestCode;
import com.jpeony.sunflower.remoting.RemotingClient;
import com.jpeony.sunflower.remoting.RemotingServer;
import com.jpeony.sunflower.remoting.exception.RemotingConnectException;
import com.jpeony.sunflower.remoting.exception.RemotingSendRequestException;
import com.jpeony.sunflower.remoting.exception.RemotingTimeoutException;
import com.jpeony.sunflower.remoting.exception.RemotingTooMuchRequestException;
import com.jpeony.sunflower.remoting.netty.*;
import com.jpeony.sunflower.remoting.protocol.RemotingCommand;
import io.netty.channel.ChannelHandlerContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.Executors;

/**
 * @author yihonglei
 */
public class NettyClientRemotingTest {
    private static RemotingClient remotingClient;

    public static RemotingClient createRemotingClient() {
        NettyClientConfig config = new NettyClientConfig();
        return createRemotingClient(config);
    }

    public static RemotingClient createRemotingClient(NettyClientConfig nettyClientConfig) {
        RemotingClient client = new NettyRemotingClient(nettyClientConfig);
        client.start();
        return client;
    }

    @BeforeClass
    public static void setup() {
        remotingClient = createRemotingClient();
    }

    @AfterClass
    public static void destroy() {
        remotingClient.shutdown();
    }

    @Test
    public void testInvokeAsync() throws InterruptedException, RemotingConnectException,
            RemotingTimeoutException, RemotingTooMuchRequestException, RemotingSendRequestException {
        RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.SEND_ERROR_MONITOR_MESSAGE);
        request.setRemark("send message");
        remotingClient.invokeAsync("localhost:9999", request, 1000 * 3);

        Thread.sleep(300000);
    }
}
