package com.team2.mosoo_backend.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GosuUpdateRequestDto {
    @NotNull
    private String gender;

    @NotNull
    private String gosuInfoAddress;

    @NotNull
    private String gosuInfoPhone;

}
