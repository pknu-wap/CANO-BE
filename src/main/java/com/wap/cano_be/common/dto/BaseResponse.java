package com.wap.cano_be.common.dto;

public record BaseResponse<T>(
    String resultCode,
    T data,
    String message
) {
    public BaseResponse(String resultCode, T data, String message){
        this.resultCode = resultCode;
        this.data = data;
        this.message = message;
    }
}
