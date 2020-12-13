package com.jpeony.sunflower.server.enums;

import lombok.Getter;

/**
 * @author yihonglei
 */
public enum ErrorCodeEnum {
    SYSTEM_DEFAULT_ERROR(0, "Network exception, please try again."),
    ILLEGAL_ARGUMENT_ERROR(10000, "invalid param.");

    @Getter
    private int code;
    @Getter
    private String msg;

    ErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
