package com.jpeony.sunflower.agent.remoting;

import com.jpeony.sunflower.remoting.common.RemotingUtil;

/**
 * @author yihonglei
 */
public class ClientConfig {
    private String clientIP = RemotingUtil.getLocalAddress();

    public String buildClientIP() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClientIP());

        sb.append("@");

        // TODO cluster split

        return sb.toString();
    }

    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }
}
