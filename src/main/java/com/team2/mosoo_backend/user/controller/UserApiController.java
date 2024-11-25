package com.team2.mosoo_backend.user.controller;


import com.team2.mosoo_backend.user.domain.Users;
import com.team2.mosoo_backend.user.dto.request.UserSignupRequestDto;
import com.team2.mosoo_backend.user.dto.response.UserSignupResponseDto;
import com.team2.mosoo_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class UserApiController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<UserSignupResponseDto> signup(@RequestBody UserSignupRequestDto userSignupRequestDto) {

        // 요청 데이터가 유효한지 검사
        if(!userService.validateSignup(userSignupRequestDto)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(UserSignupResponseDto.builder().message("유효성 검사 실패").build());
        }

        Users user = userService.save(userSignupRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserSignupResponseDto.builder().message("성공적으로 회원가입하셨습니다.").build());
    }

    
}
