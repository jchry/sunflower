package com.jpeony.sunflower.example.controller;

import com.jpeony.sunflower.remoting.exception.RemotingConnectException;
import com.jpeony.sunflower.remoting.exception.RemotingSendRequestException;
import com.jpeony.sunflower.remoting.exception.RemotingTimeoutException;
import com.jpeony.sunflower.remoting.exception.RemotingTooMuchRequestException;
import com.jpeony.sunflower.remoting.netty.NettyClientConfig;
import com.jpeony.sunflower.remoting.netty.NettyRemotingClient;
import com.jpeony.sunflower.remoting.protocol.RemotingCommand;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yihonglei
 */
@RestController
@RequestMapping("/remoting")
public class RemotingController {

    @RequestMapping("/test")
    public String dealRequest(Integer requestCode) throws InterruptedException, RemotingSendRequestException,
            RemotingTimeoutException, RemotingTooMuchRequestException, RemotingConnectException {
        NettyClientConfig config = new NettyClientConfig();
        NettyRemotingClient remotingClient = new NettyRemotingClient(config);
        remotingClient.start();

        RemotingCommand request = RemotingCommand.createRequestCommand(requestCode);
        request.setRemark("message");

        remotingClient.invokeAsync("localhost:9999", request, 1000 * 3);
        return "SUCCESS";
    }
}
