package com.wap.cano_be.security;

public class JwtConstants {
    public static final int ACCESS_EXP_TIME = 60;   // 1시간
    public static final int REFRESH_EXP_TIME = 60 * 24 * 7;   // 1주일

    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_TYPE = "Bearer ";
}
