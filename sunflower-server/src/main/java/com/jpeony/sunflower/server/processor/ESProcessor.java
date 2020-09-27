package com.jpeony.sunflower.server.processor;

import com.jpeony.sunflower.remoting.netty.AsyncNettyRequestProcessor;
import com.jpeony.sunflower.remoting.protocol.RemotingCommand;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author yihonglei
 */
public class ESProcessor extends AsyncNettyRequestProcessor {
    @Override
    public void processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws Exception {
        System.out.println("ES......");
    }

    @Override
    public boolean rejectRequest() {
        return false;
    }
}
