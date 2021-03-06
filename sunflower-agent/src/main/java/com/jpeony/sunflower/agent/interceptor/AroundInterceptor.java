package com.jpeony.sunflower.agent.interceptor;

/**
 * @author yihonglei
 */
public interface AroundInterceptor extends Interceptor {

    void before(Object target, Object[] args);

    void after(Object target, Object[] args, Object result, Throwable throwable);

}
