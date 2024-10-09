package com.wap.cano_be.common.status;

public enum ResultCode {
    SUCCESS("정상 처리되었습니다."),
    ERROR("오류가 발생했습니다.");

    private final String _message;
    ResultCode(String message){
        _message = message;
    }
    public String message() {
        return _message;
    }
}
