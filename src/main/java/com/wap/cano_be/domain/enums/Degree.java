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

    // Double 값을 받아서 해당 Degree를 반환하는 메서드
    public static Degree fromPercentage(Double percentage) {
        if (percentage == null) {
            return null;
        }

        for (Degree degree : Degree.values()) {
            if (degree.getPercentage() == percentage) {
                return degree;
            }
        }
        throw new IllegalArgumentException("No Degree found for percentage: " + percentage);
    }
}
