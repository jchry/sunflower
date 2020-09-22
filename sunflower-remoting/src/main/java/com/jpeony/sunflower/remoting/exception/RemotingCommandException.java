package com.jpeony.sunflower.remoting.exception;

/**
 * @author yihonglei
 */
public class RemotingCommandException extends RemotingException {
    private static final long serialVersionUID = 4038648137286925675L;

    public RemotingCommandException(String message) {
        super(message, null);
    }

    public RemotingCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
