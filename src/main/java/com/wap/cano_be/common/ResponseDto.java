package com.wap.cano_be.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseDto {
    private String responseCode;
    private String responseMessage;

    public ResponseDto(){
        this.responseCode = ResponseCode.SUCCESS.name();
        this.responseMessage = ResponseCode.SUCCESS.message();
    }

    public ResponseDto(String responseCode, String responseMessage){
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    public static ResponseEntity<ResponseDto> databaseError() {
        ResponseDto responseDto = new ResponseDto(ResponseCode.DATABASE_ERROR.name(), ResponseCode.DATABASE_ERROR.message());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
    }

    public static ResponseEntity<ResponseDto> validationFail(){
        ResponseDto responseDto = new ResponseDto(ResponseCode.VALIDATION_FAILED.name(), ResponseCode.VALIDATION_FAILED.message());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

    public static ResponseEntity<ResponseDto> noSuchUser(){
        ResponseDto responseDto = new ResponseDto(ResponseCode.NO_SUCH_USER.name(), ResponseCode.NO_SUCH_USER.message());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
    }
}
