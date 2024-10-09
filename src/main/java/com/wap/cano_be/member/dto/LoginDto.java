package com.wap.cano_be.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record LoginDto(
        @NotBlank
        @JsonProperty("loginId")
        String loginId,

        @NotBlank
        @JsonProperty("password")
        String password
) {
}
