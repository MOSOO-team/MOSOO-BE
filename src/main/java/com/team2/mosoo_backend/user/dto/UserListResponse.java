package com.team2.mosoo_backend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserListResponse {
    private List<UserResponseDto> memberList;
    private int totalPages;
}
