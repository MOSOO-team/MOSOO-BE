package com.team2.mosoo_backend.user.controller;


import com.team2.mosoo_backend.user.dto.ChangePasswordRequestDto;
import com.team2.mosoo_backend.user.dto.UserResponseDto;
import com.team2.mosoo_backend.user.entity.UserInfo;
import com.team2.mosoo_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    // 유저 개인 정보 가져오기
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyMemberInfo(@AuthenticationPrincipal UserDetails userDetails) {
        UserResponseDto myInfoBySecurity = userService.getMyInfoBySecurity(Long.parseLong(userDetails.getUsername()));
        System.out.println(myInfoBySecurity.getFullName());
        return ResponseEntity.ok((myInfoBySecurity));
    }


    // 유저 비밀번호 변경
    @PutMapping("/password")
    public ResponseEntity<UserResponseDto> setMemberPassword(@Validated @RequestBody ChangePasswordRequestDto request) {
        return ResponseEntity.ok(userService.changeMemberPassword(request.getExPassword(), request.getNewPassword()));
    }

    // 유저 정보 주소 변경
    @PutMapping("/{id}")
    public ResponseEntity<UserInfo> updateUserInfoAddress(@PathVariable Long id, @RequestBody UserInfo updatedUserInfo) {
        UserInfo userInfo = userService.updateUserInfoAddress(id, updatedUserInfo);
        return ResponseEntity.ok(userInfo);
    }

    // 유저 탈퇴
    @DeleteMapping("/delete")
    public ResponseEntity<UserResponseDto> deleteMember() {
        return ResponseEntity.ok(userService.deleteMember());
    }

}