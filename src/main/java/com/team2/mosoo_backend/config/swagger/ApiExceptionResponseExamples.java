package com.team2.mosoo_backend.config.swagger;

import com.team2.mosoo_backend.exception.ErrorCode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiExceptionResponseExamples {

    ErrorCode[] value();
}
