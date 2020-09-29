package com.jpeony.sunflower.agent.startup;

import com.jpeony.sunflower.agent.remoting.ClientConfig;
import com.jpeony.sunflower.agent.remoting.ClientInstance;
import com.jpeony.sunflower.agent.remoting.ClientManager;
import com.jpeony.sunflower.common.protocol.RequestCode;
import com.jpeony.sunflower.remoting.protocol.RemotingCommand;

/**
 * @author yihonglei
 */
public class AgentBootStrap {
    public static void main(String[] args) {
        // Start client
        ClientManager clientManager = ClientManager.getInstance();
        ClientConfig clientConfig = new ClientConfig();
        ClientInstance clientInstance = clientManager.getOrCreateClientInstance(clientConfig);
        clientInstance.start();

        // Shutdown client
        Runtime.getRuntime().addShutdownHook(new Thread(clientInstance::shutdown));

        // sendMessage-test
        RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.SEND_ERROR_MONITOR_MESSAGE);
        request.setRemark("send message agent");
        clientInstance.getClientAPIImpl().sendMessage("localhost:9999", request, 1000 * 3);
    }
}
