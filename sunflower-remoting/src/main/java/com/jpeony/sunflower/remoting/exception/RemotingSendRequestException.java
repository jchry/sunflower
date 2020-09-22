package com.jpeony.sunflower.remoting.exception;

/**
 * @author yihonglei
 */
public class RemotingSendRequestException extends RemotingException {
    private static final long serialVersionUID = 6958285667407282232L;

    public RemotingSendRequestException(String addr) {
        this(addr, null);
    }

    public RemotingSendRequestException(String addr, Throwable cause) {
        super("send request to <" + addr + "> failed", cause);
    }
}
