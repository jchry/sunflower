package com.jpeony.sunflower.agent;

import java.lang.instrument.Instrumentation;

/**
 * @author yihonglei
 */
public class AgentBootStrap {
    /**
     * jvm 参数形式启动，运行此方法
     *
     * @param agentArgs
     * @param inst
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("premain");
    }
}
