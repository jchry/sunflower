package com.jpeony.sunflower.agent.monitor;

import com.jpeony.sunflower.agent.producer.MQProducer;
import com.jpeony.sunflower.remoting.protocol.RemotingCommand;

/**
 * @author yihonglei
 */
public abstract class AbstractMonitor {

    public void send(RemotingCommand request) {
        try {
            MQProducer mqProducer = MQProducer.getInstance();

            /*
             * Launch the instance
             */
            mqProducer.start();

            mqProducer.send("localhost:9898", request, mqProducer.getSendMsgTimeout());
        } catch (Exception e) {
            System.out.println("......");
        }
    }
}
