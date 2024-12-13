package com.team2.mosoo_backend.user.dto;

import com.team2.mosoo_backend.user.entity.Authority;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {


    private long id;
    private String email;
    private String fullName;
    private Authority authority;
    private UserInfoDto userInfoDto;
    private LocalDateTime createdAt;
    private LocalDateTime deleteAt;
    private String deleteReason;


}
