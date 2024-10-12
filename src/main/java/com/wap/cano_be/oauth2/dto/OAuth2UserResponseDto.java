package com.wap.cano_be.oauth2.dto;

import com.wap.cano_be.common.ResponseCode;
import com.wap.cano_be.common.ResponseDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class OAuth2UserResponseDto extends ResponseDto {
    private String socialId;
    private String name;
    private String email;
    private String profileImageUrl;
    private String accessToken;
    private String refreshToken;

    @Builder
    public OAuth2UserResponseDto(String socialId, String name, String email, String profileImageUrl, String accessToken, String refreshToken) {
        super(ResponseCode.SUCCESS.name(), ResponseCode.SUCCESS.message());
        this.socialId = socialId;
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static ResponseEntity<ResponseDto> certificationFail(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDto(ResponseCode.CERTIFICATION_FAIL.name(), ResponseCode.CERTIFICATION_FAIL.message()));
    }
}
