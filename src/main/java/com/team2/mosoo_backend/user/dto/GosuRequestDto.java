package com.team2.mosoo_backend.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GosuRequestDto {
    @NotNull
    private Long userInfoId;

    @NotNull
    private String gender;

    @NotNull
    private String businessName;

    @NotNull
    private String businessNumber;

    @NotNull
    private String gosuInfoAddress;

    @NotNull
    private String gosuInfoPhone;

    @NotNull
    private String category;
}