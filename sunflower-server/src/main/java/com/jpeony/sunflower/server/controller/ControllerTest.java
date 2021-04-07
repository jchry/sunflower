package com.jpeony.sunflower.server.controller;

import com.jpeony.sunflower.server.service.ServiceTest;
import com.jpeony.sunflower.common.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yihonglei
 */
@RestController
@RequestMapping("/test")
public class ControllerTest {
    @Autowired
    private ServiceTest serviceTest;

    @RequestMapping("/test")
    public ApiResponse test(String content) {
        serviceTest.test(content);
        return ApiResponse.success("SUCCESS");
    }
}
