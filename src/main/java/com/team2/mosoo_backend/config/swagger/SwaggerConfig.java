package com.team2.mosoo_backend.config.swagger;

import com.team2.mosoo_backend.exception.*;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class SwaggerConfig {

    // SecurityScheme 이름 정의
    static String jwtSchemeName = "token";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo())                        // API 정보 설정
                .addSecurityItem(securityRequirement()) // 보안 요구 사항 추가
                .components(components());              // 구성 요소 추가
    }

    // API 정보 설정 메서드
    private Info apiInfo() {
        return new Info()
                .title("MOSOO")
                .description("MOSOO API 명세서")
                .version("v0.0.1");
    }

    // 보안 요구 사항 설정 메서드
    private SecurityRequirement securityRequirement() {

        // API 요청 헤더에 인증 정보를 포함
        return new SecurityRequirement().addList(jwtSchemeName);
    }

    // SecurityScheme 구성 요소 등록 메서드
    private Components components() {

        // SecuritySchemes 등록
        return new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP) // HTTP 방식으로 인증
                        .scheme("bearer") // Bearer 스킴 사용
                        .bearerFormat("JWT")); // 토큰 형식을 지정하는 임의의 문자(Optional)
    }

    @Bean
    public OperationCustomizer customize() {
        return (Operation operation, HandlerMethod handlerMethod) -> {

            ApiExceptionResponseExamples apiExceptionResponseExamples = handlerMethod.getMethodAnnotation(
                    ApiExceptionResponseExamples.class);

            // @ApiExceptionResponseExamples 어노테이션이 붙어있다면 (예시 여러개)
            if (apiExceptionResponseExamples != null) {
                generateApiExceptionResponseExample(operation, apiExceptionResponseExamples.value());
            } else {
                ApiExceptionResponseExample apiExceptionResponseExample
                        = handlerMethod.getMethodAnnotation(ApiExceptionResponseExample.class);

                // @ApiExceptionResponseExample 어노테이션이 붙어있다면 (예시 한개)
                if (apiExceptionResponseExample != null) {
                    generateApiExceptionResponseExample(operation, apiExceptionResponseExample.value());
                }
            }

            return operation;
        };
    }

    // 여러 개 에러 응답 예시를 ApiResponses에 추가
    private void generateApiExceptionResponseExample(Operation operation, ErrorCode[] errorCodes) {
        ApiResponses responses = operation.getResponses();

        // ExampleHolder(에러 응답값) 객체를 만들고 에러 코드별로 그룹화
        Map<Integer, List<ExampleHolder>> statusWithExampleHolders = Arrays.stream(errorCodes)
                .map(
                        errorCode -> ExampleHolder.builder()
                                .holder(getSwaggerExample(errorCode))
                                .code(errorCode.getHttpStatus().value())
                                .name(errorCode.name())
                                .build()
                )
                .collect(Collectors.groupingBy(ExampleHolder::getCode));

        // statusWithExampleHolders를 ApiResponses에 추가
        addExamplesToResponses(responses, statusWithExampleHolders);
    }

    // 단일 에러 응답 예시를 ApiResponses에 추가
    private void generateApiExceptionResponseExample(Operation operation, ErrorCode errorCode) {
        ApiResponses responses = operation.getResponses();

        // ExampleHolder 객체 생성 및 ApiResponses에 추가
        ExampleHolder exampleHolder = ExampleHolder.builder()
                .holder(getSwaggerExample(errorCode))
                .name(errorCode.name())
                .code(errorCode.getHttpStatus().value())
                .build();

        // exampleHolder를 ApiResponses에 추가
        addExamplesToResponses(responses, exampleHolder);
    }

    // ErrorResponseEntity 형태의 예시 객체 생성
    private Example getSwaggerExample(ErrorCode errorCode) {
        ErrorResponseEntity errorResponseEntity = ErrorResponseEntity.toResponseEntity(errorCode).getBody();
        Example example = new Example();
        example.setValue(errorResponseEntity);

        return example;
    }

    // 여러 개의 ExampleHolder를 ApiResponses에 추가
    private void addExamplesToResponses(ApiResponses responses,
                                        Map<Integer, List<ExampleHolder>> statusWithExampleHolders) {
        statusWithExampleHolders.forEach(
                (status, v) -> {
                    Content content = new Content(); // 응답 콘텐츠 생성
                    MediaType mediaType = new MediaType(); // 미디어 타입 생성
                    io.swagger.v3.oas.models.responses.ApiResponse apiResponse = new io.swagger.v3.oas.models.responses.ApiResponse();

                    // 각 ExampleHolder에 대해 예시 추가
                    v.forEach(
                            exampleHolder -> mediaType.addExamples(
                                    exampleHolder.getName(), // 예시 이름
                                    exampleHolder.getHolder() // 예시 객체
                            )
                    );
                    content.addMediaType("application/json", mediaType); // JSON 미디어 타입 설정

                    // ApiResponse 설정
                    apiResponse.setContent(content);
                    responses.addApiResponse(String.valueOf(status), apiResponse); // 상태 코드에 따른 응답 추가
                }
        );
    }

    // 단일 ExampleHolder를 ApiResponses에 추가
    private void addExamplesToResponses(ApiResponses responses, ExampleHolder exampleHolder) {
        Content content = new Content();
        MediaType mediaType = new MediaType();
        io.swagger.v3.oas.models.responses.ApiResponse apiResponse = new io.swagger.v3.oas.models.responses.ApiResponse();

        mediaType.addExamples(exampleHolder.getName(), exampleHolder.getHolder());
        content.addMediaType("application/json", mediaType);
        apiResponse.content(content);
        responses.addApiResponse(String.valueOf(exampleHolder.getCode()), apiResponse);
    }
}
