package com.jpeony.sunflower.server.service.impl;

import com.jpeony.sunflower.server.service.ServiceTest;
import org.springframework.stereotype.Service;

/**
 * @author yihonglei
 */
@Service
public class ServiceTestImpl implements ServiceTest {
    @Override
    public void test(String content) {
        System.out.println("content");
    }
}
