package com.jpeony.sunflower.agent.remoting;

import com.jpeony.sunflower.remoting.common.RemotingUtil;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author yihonglei
 */
public class RemotingClientManager {
    private static RemotingClientManager instance = new RemotingClientManager();
    private ConcurrentMap<String/* clientId */, RemotingClientInstance> factoryTable =
            new ConcurrentHashMap<String, RemotingClientInstance>();

    private RemotingClientManager() {

    }

    public static RemotingClientManager getInstance() {
        return instance;
    }

    public RemotingClientInstance getOrCreateClientInstance() {
        String clientId = this.buildClientIP();
        RemotingClientInstance instance = this.factoryTable.get(clientId);
        if (null == instance) {
            instance = new RemotingClientInstance();
            RemotingClientInstance prev = this.factoryTable.putIfAbsent(clientId, instance);
            if (prev != null) {
                instance = prev;
            }
        }
        return instance;
    }

    public String buildClientIP() {
        String clientIP = RemotingUtil.getLocalAddress();

        StringBuilder sb = new StringBuilder();
        sb.append(clientIP);

        sb.append("@");

        // TODO cluster split

        return sb.toString();
    }
}
