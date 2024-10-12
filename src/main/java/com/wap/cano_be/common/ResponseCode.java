package com.wap.cano_be.common;

public enum ResponseCode {
    SUCCESS("정상 처리되었습니다."),
    DATABASE_ERROR("데이터베이스 오류가 발생했습니다."),
    VALIDATION_FAIL("유효성 검사에 실패했습니다."),
    NO_SUCH_USER("사용자를 찾지 못했습니다."),
    CERTIFICATION_FAIL("권한 인증에 실패했습니다."),
    IS_DUPLICATED("중복 데이터입니다.");

    private final String responseMessage;

    ResponseCode(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String message() {
        return responseMessage;
    }
}
