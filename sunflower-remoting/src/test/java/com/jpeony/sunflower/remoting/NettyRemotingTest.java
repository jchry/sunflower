package com.jpeony.sunflower.remoting;

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

public class NettyRemotingTest {
    private static RemotingServer remotingServer;
    private static RemotingClient remotingClient;

    public static RemotingServer createRemotingServer() {
        NettyServerConfig config = new NettyServerConfig();
        RemotingServer remotingServer = new NettyRemotingServer(config);
        remotingServer.registerProcessor(0, new AsyncNettyRequestProcessor() {
            @Override
            public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) {
                request.setRemark("Hi " + ctx.channel().remoteAddress());
                System.out.println("服务端收到消息, remark=" + request.getRemark());
                return request;
            }

            @Override
            public boolean rejectRequest() {
                return false;
            }
        }, Executors.newCachedThreadPool());

        remotingServer.start();

        return remotingServer;
    }

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
        remotingServer = createRemotingServer();
        remotingClient = createRemotingClient();
    }

    @AfterClass
    public static void destroy() {
        remotingClient.shutdown();
        remotingServer.shutdown();
    }

    @Test
    public void testInvokeAsync() throws InterruptedException, RemotingConnectException,
            RemotingTimeoutException, RemotingTooMuchRequestException, RemotingSendRequestException {

        RemotingCommand request = RemotingCommand.createRequestCommand(0);
        request.setRemark("message");
        remotingClient.invokeAsync("localhost:9999", request, 1000 * 3);
        System.out.println("");

        Thread.sleep(300000);
    }
}
