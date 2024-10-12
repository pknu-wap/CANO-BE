package com.wap.cano_be.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class ResponseDto {
    private String code;
    private String message;

    public ResponseDto(){
        this.code = ResponseCode.SUCCESS.name();
        this.message = ResponseCode.SUCCESS.message();
    }

    public ResponseDto(String code, String message){
        this.code = code;
        this.message = message;
    }

    public static ResponseEntity<ResponseDto> databaseError() {
        ResponseDto responseDto = new ResponseDto(ResponseCode.DATABASE_ERROR.name(), ResponseCode.DATABASE_ERROR.message());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
    }

    public static ResponseEntity<ResponseDto> validationFail(){
        ResponseDto responseDto = new ResponseDto(ResponseCode.VALIDATION_FAIL.name(), ResponseCode.VALIDATION_FAIL.message());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

    public static ResponseEntity<ResponseDto> noSuchUser(){
        ResponseDto responseDto = new ResponseDto(ResponseCode.NO_SUCH_USER.name(), ResponseCode.NO_SUCH_USER.message());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
    }
}
