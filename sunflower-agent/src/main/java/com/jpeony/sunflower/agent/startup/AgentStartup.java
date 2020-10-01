package com.jpeony.sunflower.agent.startup;

import com.jpeony.sunflower.agent.remoting.RemotingClientInstance;
import com.jpeony.sunflower.agent.remoting.RemotingClientManager;
import com.jpeony.sunflower.common.protocol.RequestCode;
import com.jpeony.sunflower.remoting.protocol.RemotingCommand;

/**
 * @author yihonglei
 */
public class AgentStartup {
    public static void main(String[] args) {
        // ClientInstance
        RemotingClientInstance remotingClientInstance = RemotingClientManager.getInstance().getOrCreateClientInstance();

        // Shutdown client
        Runtime.getRuntime().addShutdownHook(new Thread(remotingClientInstance::shutdown));

        // Start client
        remotingClientInstance.start();

        // sendMessage-test
        RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.SEND_ERROR_MONITOR_MESSAGE);
        request.setRemark("send message agent");
        remotingClientInstance.getRemotingClientAPIImpl().sendMessage("localhost:9999", request, 1000 * 3);
    }
}
