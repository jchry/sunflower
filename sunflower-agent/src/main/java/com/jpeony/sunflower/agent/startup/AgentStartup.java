package com.jpeony.sunflower.agent.startup;

import com.jpeony.sunflower.agent.producer.MQProducer;

/**
 * @author yihonglei
 */
public class AgentStartup {
    public static void main(String[] args) {
        try {
            MQProducer producer = MQProducer.getInstance();

            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    producer.shutdown();
                }
            }));
            /*
             * Launch the instance
             */
            producer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
