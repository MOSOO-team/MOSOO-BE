package com.team2.mosoo_backend.config.swagger;

import io.swagger.v3.oas.models.examples.Example;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExampleHolder {

    private Example holder;
    private int code;
    private String name;
}