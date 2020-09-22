package com.jpeony.sunflower.remoting.netty;

import com.jpeony.sunflower.remoting.protocol.RemotingCommand;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author yihonglei
 */
public abstract class AsyncNettyRequestProcessor implements NettyRequestProcessor {

    public void asyncProcessRequest(ChannelHandlerContext ctx, RemotingCommand request) throws Exception {
        processRequest(ctx, request);
    }
}
