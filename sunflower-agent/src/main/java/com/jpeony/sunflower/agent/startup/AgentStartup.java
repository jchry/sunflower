package com.jpeony.sunflower.agent.startup;

import com.jpeony.sunflower.agent.producer.MQClientInstance;
import com.jpeony.sunflower.agent.producer.MQClientManager;

/**
 * @author yihonglei
 */
public class AgentStartup {
    public static void main(String[] args) {
        // ClientInstance
        MQClientInstance clientInstance = MQClientManager.getInstance().getOrCreateClientInstance();

        // Shutdown client
        Runtime.getRuntime().addShutdownHook(new Thread(clientInstance::shutdown));

        // Start client
        clientInstance.start();
    }
}
