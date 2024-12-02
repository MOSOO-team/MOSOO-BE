package com.team2.mosoo_backend.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupRequestDto {

    private String fullname;
    private String username;
    private String password;
    private String email;
}
