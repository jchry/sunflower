package com.jpeony.sunflower.remoting;

import com.jpeony.sunflower.remoting.netty.NettyRequestProcessor;

import java.util.concurrent.ExecutorService;

/**
 * @author yihonglei
 */
public interface RemotingServer extends RemotingService {
    void registerProcessor(int requestCode, final NettyRequestProcessor processor, final ExecutorService executor);
}
