package com.jpeony.sunflower.agent.monitor;

import com.jpeony.sunflower.agent.producer.MQProducer;
import com.jpeony.sunflower.agent.producer.MQClientManager;
import com.jpeony.sunflower.remoting.protocol.RemotingCommand;

/**
 * @author yihonglei
 */
public abstract class AbstractMonitor {

    public void send(RemotingCommand request) {
        try {
            MQProducer MQProducer =
                    MQClientManager.getInstance().getOrCreateClientInstance().getMQProducer();

            MQProducer.send("localhost:9999", request, 1000 * 3);
        } catch (Exception e) {
            System.out.println("......");
        }
    }
}
