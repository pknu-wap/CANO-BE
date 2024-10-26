package com.wap.cano_be.exception;

public class CustomJwtException extends RuntimeException {
    public CustomJwtException(String msg) {
        super(msg);
    }
}
