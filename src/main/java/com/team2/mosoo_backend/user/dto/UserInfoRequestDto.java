package com.team2.mosoo_backend.user.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoRequestDto {
    private String email;
    private String newAddress;
}
