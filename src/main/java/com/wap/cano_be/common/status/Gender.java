package com.wap.cano_be.common.status;

public enum Gender {
    MAN("남"),
    WOMAN("여");

    private final String desc;

    Gender(String desc) {
        this.desc = desc;
    }

    public String getGender(){
        return desc;
    }
}
