package com.team2.mosoo_backend.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UsersInfoResponseDto {
    private String address;
    private String streetAddress;
    private String detailedAddress;
}
