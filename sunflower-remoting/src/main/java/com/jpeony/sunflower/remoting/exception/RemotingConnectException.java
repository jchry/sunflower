package com.jpeony.sunflower.remoting.exception;

/**
 * @author yihonglei
 */
public class RemotingConnectException extends RemotingException {
    private static final long serialVersionUID = -5420676577967561342L;

    public RemotingConnectException(String addr) {
        this(addr, null);
    }

    public RemotingConnectException(String addr, Throwable cause) {
        super("connect to " + addr + " failed", cause);
    }
}
