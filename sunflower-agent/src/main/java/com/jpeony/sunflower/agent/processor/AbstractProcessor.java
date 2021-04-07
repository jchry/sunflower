package com.jpeony.sunflower.agent.processor;

import com.jpeony.sunflower.agent.producer.MQProducer;
import com.jpeony.sunflower.remoting.protocol.RemotingCommand;

/**
 * @author yihonglei
 */
public abstract class AbstractProcessor {

    public void send(RemotingCommand request) {
        try {
            MQProducer mqProducer = MQProducer.getInstance();

            mqProducer.send("localhost:9999", request, mqProducer.getSendMsgTimeout());
        } catch (Exception e) {
            System.out.println("......");
        }
    }
}
