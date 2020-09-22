package com.jpeony.sunflower.remoting.exception;

/**
 * @author yihonglei
 */
public class RemotingException extends Exception {
    private static final long serialVersionUID = -7242645536733019239L;

    public RemotingException(String message) {
        super(message);
    }

    public RemotingException(String message, Throwable cause) {
        super(message, cause);
    }
}
