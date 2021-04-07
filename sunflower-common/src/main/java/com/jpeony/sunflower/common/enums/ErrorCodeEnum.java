package com.jpeony.sunflower.common.enums;

import lombok.Getter;

/**
 * @author yihonglei
 */
public enum ErrorCodeEnum {
    ERROR_SYSTEM_DEFAULT(0, "Network exception, please try again"),
    ERROR_ILLEGAL_ARGUMENT(10000, "invalid param");

    @Getter
    private int code;
    @Getter
    private String msg;

    ErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
