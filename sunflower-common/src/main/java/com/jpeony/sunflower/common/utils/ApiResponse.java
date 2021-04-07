package com.jpeony.sunflower.common.utils;

import com.jpeony.sunflower.common.enums.ErrorCodeEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author yihonglei
 */
public final class ApiResponse<T> implements Serializable {

    private static final long serialVersionUID = 4821891515403482645L;

    /**
     * 1 SUCCESS
     */
    private static final int SUCCESS_CODE = 1;

    /**
     * 0 ERROR
     */
    private static final int ERROR_CODE = 0;

    private static final ApiResponse SUCCESS = new ApiResponse(SUCCESS_CODE, "SUCCESS", null);


    private static final ApiResponse ERROR = new ApiResponse(ERROR_CODE, "ERROR", null);

    @Getter
    @Setter
    private int code;

    @Getter
    @Setter
    private String msg;

    @Getter
    @Setter
    private T data;

    private ApiResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private static <T> ApiResponse<T> success(int code, String msg, T data) {
        return new ApiResponse<>(code, msg, data);
    }

    private static <T> ApiResponse<T> success(String msg, T data) {
        return success(SUCCESS.getCode(), msg, data);
    }

    public static <T> ApiResponse<T> success(T data) {
        return success(SUCCESS.getMsg(), data);
    }

    public static <T> ApiResponse<T> success(String msg) {
        return success(msg, null);
    }

    public static <T> ApiResponse<T> success() {
        return success(SUCCESS.code, SUCCESS.msg, null);
    }

    public static <T> ApiResponse error(ErrorCodeEnum errorCodeEnum) {
        return error(errorCodeEnum.getCode(), errorCodeEnum.getMsg(), null);
    }

    public static <T> ApiResponse error() {
        return error(ERROR.code, ERROR.msg, null);
    }

    public static ApiResponse error(int code, String msg, Object data) {
        return new ApiResponse<>(code, msg, data);
    }

    private static ApiResponse error(String msg, Object data) {
        return error(ERROR.getCode(), msg, data);
    }

    public static ApiResponse error(Object data) {
        return error(ERROR.getMsg(), data);
    }

    public static ApiResponse error(String msg) {
        return error(msg, null);
    }

}
