package com.team2.mosoo_backend.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GosuResponseDto {

    @NotNull
    private String gosuInfoAddress;

    @NotNull
    private String businessName;

}
