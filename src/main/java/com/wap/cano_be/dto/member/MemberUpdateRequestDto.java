package com.wap.cano_be.dto.member;

import com.wap.cano_be.domain.enums.Degree;
import lombok.Data;

@Data
public class MemberUpdateRequestDto {
    private String name;
    private Degree acidity;
    private Degree body;
    private Degree bitterness;
    private Degree sweetness;
}
