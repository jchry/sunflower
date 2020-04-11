package com.jpeony.sunflower.agent;

import java.lang.instrument.Instrumentation;

/**
 * @author yihonglei
 */
public class SunflowerAgent {
    /**
     * jvm参数形式启动
     *
     * @param args
     * @param inst
     */
    public static void premain(String args, Instrumentation inst) {
        System.out.println("premain");
    }
}
