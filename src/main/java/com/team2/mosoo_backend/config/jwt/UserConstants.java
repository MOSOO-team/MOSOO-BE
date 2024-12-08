package com.team2.mosoo_backend.config.jwt;

// 사용자 토큰 상수 관리

import lombok.NoArgsConstructor;

import java.time.Duration;

@NoArgsConstructor
public class UserConstants {
    public static final String REFRESH_TOKEN_TYPE_VALUE = "refreshToken"; // 리프레시 토큰 이름
    public static final String ACCESS_TOKEN_TYPE_VALUE = "accessToken"; // 액세스 토큰 이름
    public static final Duration REFRESH_TOKEN_TYPE_DURATION = Duration.ofDays(14); // 리프레시 토큰 유효 시간
    public static final Duration ACCESS_TOKEN_TYPE_DURATION = Duration.ofMinutes(15); // 액세스 토큰 유효 시간
}
