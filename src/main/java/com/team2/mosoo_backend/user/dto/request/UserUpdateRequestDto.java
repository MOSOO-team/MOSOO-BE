package com.team2.mosoo_backend.user.dto.request;

import com.team2.mosoo_backend.user.entity.Users;
import com.team2.mosoo_backend.user.entity.UsersInfo;
import com.team2.mosoo_backend.user.dto.response.UsersInfoResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class UserUpdateRequestDto {
    private String currentPassword; // 현재 비밀번호
    private String updatePassword; // 변경할 비밀번호
    private String email; // 변경할 이메일
    private List<UsersInfoResponseDto> address;

    public UsersInfo toUsersInfo(Users user) {
        UsersInfoResponseDto userInfo = address.get(0); // 여러 개의 리스트 중 첫 번째 가져오기
        return UsersInfo.builder()
                .address(userInfo.getAddress())
                .streetAddress(userInfo.getStreetAddress())
                .detailedAddress(userInfo.getDetailedAddress())
                .users(user)
                .build();
    }
}
