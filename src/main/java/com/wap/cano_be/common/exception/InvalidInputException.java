package com.wap.cano_be.common.exception;

public class InvalidInputException extends RuntimeException {
    private String fieldName;

    public InvalidInputException(String fieldName, String message){
        super(message);
        this.fieldName = fieldName;
    }

    public InvalidInputException(String message){
        this("", message);
    }

    public String getFieldName(){
        return fieldName;
    }

}
