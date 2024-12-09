package com.team2.mosoo_backend.exception;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponseEntity> handleCustomException(CustomException e) {
        // 예외 발생 시 로그 기록
        logger.error("CustomException 발생: {}", e.getErrorCode().getMessage());
        return ErrorResponseEntity.toResponseEntity(e.getErrorCode());
    }

    // 메서드 유효성 검사 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponseEntity> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // 유효성 검사 실패 시 로그 기록
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));



        // 각 필드에 대한 에러 메시지를 기반으로 적절한 ErrorCode 반환
        if (ex.getBindingResult().getFieldErrors().stream().anyMatch(error -> error.getField().equals("email"))) {
            return ErrorResponseEntity.toResponseEntity(ErrorCode.INVALID_EMAIL_FORMAT);
        } else if (ex.getBindingResult().getFieldErrors().stream().anyMatch(error -> error.getField().equals("password"))) {
            return ErrorResponseEntity.toResponseEntity(ErrorCode.INVALID_PASSWORD);
        }

        logger.error("Validation failed: {}", errorMessage);

        return ErrorResponseEntity.toResponseEntity(ErrorCode.METHOD_ARGUMENT_NOT_VALID);
    }


    // 페이지 양수 조건 예외 처리
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ErrorResponseEntity> handlePageValidationExceptions(ConstraintViolationException ex) {

        // 페이지 유효성 검사 실패 시 로그 기록
        String errorMessage = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));

        logger.error("페이지 유효성 검사 실패: {}", errorMessage);

        return ErrorResponseEntity.toResponseEntity(ErrorCode.PAGE_NOT_VALID);
    }
}
