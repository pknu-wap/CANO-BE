package com.wap.cano_be.common.exception;

import com.wap.cano_be.common.dto.BaseResponse;
import com.wap.cano_be.common.status.ResultCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<BaseResponse<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage != null ? errorMessage : "Not Exception Message");
        });

        return new ResponseEntity<>(new BaseResponse<>(ResultCode.ERROR.name(), errors, ResultCode.ERROR.message()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidInputException.class)
    protected ResponseEntity<BaseResponse<Map<String, String>>> handleInvalidInputException(InvalidInputException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getFieldName(), ex.getMessage() != null ? ex.getMessage() : "Not Exception Message");

        return new ResponseEntity<>(new BaseResponse<>(ResultCode.ERROR.name(), errors, ResultCode.ERROR.message()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<BaseResponse<Map<String, String>>> handleBadCredentialsException(BadCredentialsException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("로그인 실패", "아이디 호은 비밀번호를 다시 확인하세요.");

        return new ResponseEntity<>(new BaseResponse<>(ResultCode.ERROR.name(), errors, ResultCode.ERROR.message()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<BaseResponse<Map<String, String>>> handleDefaultException(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("미처리 에러", ex.getMessage() != null ? ex.getMessage() : "Not Exception Message");

        return new ResponseEntity<>(new BaseResponse<>(ResultCode.ERROR.name(), errors, ResultCode.ERROR.message()), HttpStatus.BAD_REQUEST);
    }
}
