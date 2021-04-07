package com.jpeony.sunflower.agent;

import com.jpeony.sunflower.agent.processor.ErrorProcessor;
import com.jpeony.sunflower.agent.producer.MQProducer;

import java.util.concurrent.CountDownLatch;

/**
 * @author yihonglei
 */
public class AgentStartupTest {
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
             * Launch the producer instance.
             */
            producer.start();

            CountDownLatch c = new CountDownLatch(1);
            Thread errorThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ErrorProcessor error = new ErrorProcessor();
                        error.errorMonitor();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        c.countDown();
                    }
                }
            });
            errorThread.start();

            c.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
