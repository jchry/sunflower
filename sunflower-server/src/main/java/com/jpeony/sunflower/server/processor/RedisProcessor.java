package com.jpeony.sunflower.server.processor;

import com.jpeony.sunflower.remoting.netty.NettyRequestProcessor;
import com.jpeony.sunflower.remoting.protocol.RemotingCommand;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author yihonglei
 */
public class RedisProcessor implements NettyRequestProcessor {
    @Override
    public void processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws Exception {
        System.out.println("Redis......");
    }

    @Override
    public boolean rejectRequest() {
        return false;
    }
}
