package com.jpeony.sunflower.remoting;

import com.jpeony.sunflower.remoting.exception.RemotingConnectException;
import com.jpeony.sunflower.remoting.exception.RemotingSendRequestException;
import com.jpeony.sunflower.remoting.exception.RemotingTimeoutException;
import com.jpeony.sunflower.remoting.exception.RemotingTooMuchRequestException;
import com.jpeony.sunflower.remoting.protocol.RemotingCommand;

/**
 * @author yihonglei
 */
public interface RemotingClient extends RemotingService {
    /**
     * @param addr          Request ip and port
     * @param request       Request object
     * @param timeoutMillis Message sending timeout
     */
    void invokeOneway(final String addr, final RemotingCommand request, final long timeoutMillis)
            throws InterruptedException, RemotingConnectException, RemotingTooMuchRequestException,
            RemotingTimeoutException, RemotingSendRequestException;
}
