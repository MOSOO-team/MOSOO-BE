package com.team2.mosoo_backend.user.dto;

import com.team2.mosoo_backend.user.domain.Provider;
import com.team2.mosoo_backend.user.domain.UserRole;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long userId;
    private String fullname;
    private String username;
    private String email;
    private String password;
    private UserRole role;
    private Provider provider;
}
