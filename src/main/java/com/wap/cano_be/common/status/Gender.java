package com.wap.cano_be.common.status;

public enum Gender {
    MAN("남"),
    WOMAN("여");

    private final String _desc;

    Gender(String desc) {
        _desc = desc;
    }

    public String desc(){
        return _desc;
    }
}
