package com.team2.mosoo_backend.user.dto.response;


import com.team2.mosoo_backend.user.domain.Provider;
import com.team2.mosoo_backend.user.domain.UserRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class UserResponseDto {
    private Long userId;
    private String fullname;
    private String username;
    private String email;
    private UserRole role;
    private Provider provider;
    private LocalDateTime createdAt;
    private boolean isDeleted;
    private List<UsersInfoResponseDto> userInfoList;
    private String message;


}
