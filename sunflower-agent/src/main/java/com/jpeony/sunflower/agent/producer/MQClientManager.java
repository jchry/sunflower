package com.jpeony.sunflower.agent.producer;

import com.jpeony.sunflower.remoting.common.RemotingUtil;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author yihonglei
 */
public class MQClientManager {
    private static MQClientManager instance = new MQClientManager();
    private ConcurrentMap<String/* clientId */, MQClientInstance> factoryTable =
            new ConcurrentHashMap<String, MQClientInstance>();

    private MQClientManager() {

    }

    public static MQClientManager getInstance() {
        return instance;
    }

    public MQClientInstance getOrCreateClientInstance() {
        String clientId = this.buildClientIP();
        MQClientInstance instance = this.factoryTable.get(clientId);
        if (null == instance) {
            instance = new MQClientInstance();
            MQClientInstance prev = this.factoryTable.putIfAbsent(clientId, instance);
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
