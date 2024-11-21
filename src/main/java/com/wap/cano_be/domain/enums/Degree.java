package com.wap.cano_be.domain.enums;

import lombok.Getter;

@Getter
public enum Degree {
    NONE(0),
    LOW(0.25),
    MEDIUM(0.5),
    HIGH(0.75),
    VERY_HIGH(1.0);

    private double percentage;

    Degree(double percentage) {
        this.percentage = percentage;
    }
}
