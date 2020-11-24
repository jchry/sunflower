package com.jpeony.sunflower.agent.startup;

import com.jpeony.sunflower.agent.remoting.RemotingClientInstance;
import com.jpeony.sunflower.agent.remoting.RemotingClientManager;

/**
 * @author yihonglei
 */
public class AgentStartup {
    public static void main(String[] args) {
        // ClientInstance
        RemotingClientInstance clientInstance = RemotingClientManager.getInstance().getOrCreateClientInstance();

        // Shutdown client
        Runtime.getRuntime().addShutdownHook(new Thread(clientInstance::shutdown));

        // Start client
        clientInstance.start();
    }
}
