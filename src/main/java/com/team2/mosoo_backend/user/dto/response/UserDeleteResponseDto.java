package com.team2.mosoo_backend.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDeleteResponseDto {
    private String message;
    private boolean isDeleted;
}
