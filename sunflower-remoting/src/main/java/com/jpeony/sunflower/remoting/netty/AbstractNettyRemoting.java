package com.jpeony.sunflower.remoting.netty;

import com.jpeony.sunflower.remoting.common.Pair;
import com.jpeony.sunflower.remoting.protocol.RemotingCommand;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;

/**
 * @author yihonglei
 */
public abstract class AbstractNettyRemoting {
    protected final HashMap<Integer/* request code */, Pair<NettyRequestProcessor, ExecutorService>> processorTable =
            new HashMap<>(64);

    protected Pair<NettyRequestProcessor, ExecutorService> defaultRequestProcessor;

    protected final Semaphore semaphoreOneway;

    public AbstractNettyRemoting(final int permitsOneway) {
        this.semaphoreOneway = new Semaphore(permitsOneway, true);
    }

    public void processMessageReceived(ChannelHandlerContext ctx, RemotingCommand msg) throws Exception {
        final RemotingCommand cmd = msg;
        if (cmd != null) {
            switch (cmd.getType()) {
                case REQUEST_COMMAND:
                    processRequestCommand(ctx, cmd);
                    break;
                default:
                    break;
            }
        }
    }

    public void processRequestCommand(final ChannelHandlerContext ctx, final RemotingCommand cmd) {
        final Pair<NettyRequestProcessor, ExecutorService> matched = this.processorTable.get(cmd.getCode());
        final Pair<NettyRequestProcessor, ExecutorService> pair = null == matched ? this.defaultRequestProcessor : matched;

        if (pair != null) {
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    try {
                        NettyRequestProcessor processor = pair.getObject1();
                        processor.processRequest(ctx, cmd);
                    } catch (Throwable e) {
                        System.out.println("process request exception, " + e);
                    }
                }
            };

            if (pair.getObject1().rejectRequest()) {
                return;
            }

            try {
                final RequestTask requestTask = new RequestTask(run, ctx.channel(), cmd);
                pair.getObject2().submit(requestTask);
            } catch (RejectedExecutionException e) {
                System.out.println("too many requests and system thread pool busy");
            }
        }
    }
}
