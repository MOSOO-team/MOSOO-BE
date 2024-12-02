package com.team2.mosoo_backend.config.swagger;

import com.team2.mosoo_backend.exception.ErrorCode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 어노테이션 붙일 수 있는 위치 설정
@Retention(RetentionPolicy.RUNTIME) // 어노테이션의 메타 정보가 버려질 타이밍 설정
public @interface ApiExceptionResponseExample {

    ErrorCode value();
}
