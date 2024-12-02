package com.team2.mosoo_backend.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCheckUsernameResponseDto {
    private boolean isAvailable;
    private String message;
}
