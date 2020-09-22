package com.jpeony.sunflower.remoting.exception;

/**
 * @author yihonglei
 */
public class RemotingTooMuchRequestException extends RemotingException {
    private static final long serialVersionUID = 90296060225038875L;

    public RemotingTooMuchRequestException(String message) {
        super(message);
    }
}
