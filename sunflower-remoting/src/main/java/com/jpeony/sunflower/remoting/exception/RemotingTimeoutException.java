package com.jpeony.sunflower.remoting.exception;

/**
 * @author yihonglei
 */
public class RemotingTimeoutException extends RemotingException {
    private static final long serialVersionUID = -368841760515244174L;

    public RemotingTimeoutException(String message) {
        super(message);
    }

    public RemotingTimeoutException(String addr, long timeoutMillis) {
        this(addr, timeoutMillis, null);
    }

    public RemotingTimeoutException(String addr, long timeoutMillis, Throwable cause) {
        super("wait response on the channel <" + addr + "> timeout, " + timeoutMillis + "(ms)", cause);
    }
}
