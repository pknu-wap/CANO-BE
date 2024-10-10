package com.wap.cano_be.member.domain;

public enum Gender {
    MAN("남"),
    WOMAN("여");

    private final String desc;

    Gender(String desc) {
        this.desc = desc;
    }
}
