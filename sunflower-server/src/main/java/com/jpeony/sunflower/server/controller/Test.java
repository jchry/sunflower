package com.jpeony.sunflower.server.controller;

import com.jpeony.sunflower.server.util.ApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yihonglei
 */
@RestController
@RequestMapping("/test")
public class Test {

    @RequestMapping("/test")
    public ApiResponse test() {
        return ApiResponse.success("SUCCESS");
    }
}
