package com.team2.mosoo_backend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {

    private Long id;
    private Long userId;
    private String address;
    private Boolean is_gosu;
    private LocalDateTime createdAt;

}
