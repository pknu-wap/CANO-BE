package com.wap.cano_be.domain.enums;

public enum Degree {
    NONE(0),
    LOW(0.2),
    MEDIUM(0.4),
    HIGH(0.6),
    VERY_HIGH(0.8);

    Degree(double percentage) {
    }
}
