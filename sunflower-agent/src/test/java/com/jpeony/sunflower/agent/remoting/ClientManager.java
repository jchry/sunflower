package com.jpeony.sunflower.agent.remoting;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author yihonglei
 */
public class ClientManager {
    private static ClientManager instance = new ClientManager();
    private ConcurrentMap<String/* clientId */, ClientInstance> factoryTable =
            new ConcurrentHashMap<String, ClientInstance>();

    private ClientManager() {

    }

    public static ClientManager getInstance() {
        return instance;
    }

    public ClientInstance getOrCreateClientInstance(final ClientConfig clientConfig) {
        String clientId = clientConfig.buildClientIP();
        ClientInstance instance = this.factoryTable.get(clientId);
        if (null == instance) {
            instance = new ClientInstance();
            ClientInstance prev = this.factoryTable.putIfAbsent(clientId, instance);
            if (prev != null) {
                instance = prev;
            }
        }
        return instance;
    }
}
