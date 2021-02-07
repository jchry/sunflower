package com.jpeony.sunflower.agent.monitor;

import com.jpeony.sunflower.agent.remoting.RemotingClientAPIImpl;
import com.jpeony.sunflower.agent.remoting.RemotingClientManager;
import com.jpeony.sunflower.remoting.protocol.RemotingCommand;

/**
 * @author yihonglei
 */
public abstract class AbstractMonitor {

    public void send(RemotingCommand request) {
        try {
            RemotingClientAPIImpl remotingClientAPIImpl =
                    RemotingClientManager.getInstance().getOrCreateClientInstance().getRemotingClientAPIImpl();

            remotingClientAPIImpl.send("localhost:9999", request, 1000 * 3);
        } catch (Exception e) {
            System.out.println("......");
        }
    }
}
