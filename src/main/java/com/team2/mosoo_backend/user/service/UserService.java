package com.team2.mosoo_backend.user.service;

import com.team2.mosoo_backend.user.domain.Provider;
import com.team2.mosoo_backend.user.domain.UserRole;
import com.team2.mosoo_backend.user.domain.Users;
import com.team2.mosoo_backend.user.dto.request.JwtTokenLoginRequest;
import com.team2.mosoo_backend.user.dto.request.UserSignupRequestDto;
import com.team2.mosoo_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j

public class UserService {


    private static final int PAGE_SIZE = 10;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtProviderImpl jwtProvider;
    private final UserInfoRepository userInfoRepository;

    @Value("${admin.code}")
    private String adminCode;

    // 일반 회원가입
    public Users save(UserSignupRequestDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 ID 입니다.");
        }

        Users user = Users.builder()
                .fullname(dto.getFullname())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .role(UserRole.USER)
                .provider(Provider.NONE)
                .build();

        return userRepository.save(user);
    }

    public boolean validateSignup(UserSignupRequestDto userSignupRequestDto) {
        // fullname 유효성 검증: 2 ~ 20 글자, 숫자 포함 불가
        String fullname = userSignupRequestDto.getFullname();
        boolean isFullnameValid = fullname != null && fullname.length() >= 2 && fullname.length() <= 20 && !fullname.matches(".*\\d*");
        if (!isFullnameValid) {
            return false; // 유효성 검증 실패
        }

        //username 유효성 검증: 6~20글자
        String username = userSignupRequestDto.getUsername();
        boolean isUsernameValid = username != null && username.length() >= 6 && username.length() <= 20;
        if (!isUsernameValid) {
            return false; // 유효성 검증 실패
        }

        // email 유효성 검증: 이메일 형식
        String email = userSignupRequestDto.getEmail();
        boolean isEmailValid = email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        if (!isEmailValid) {
            return false; // 유효성 검증 실패
        }

        //password 유효성 검증: 8~20글자, 영문자, 특수문자 포함
        String password = userSignupRequestDto.getPassword();
        boolean isPasswordValid = password != null && password.length() >= 8 && password.length() <= 20 && password.matches("^(?=.*[a-zA-Z])(?=.*\\W).+$");
        if (!isPasswordValid) {
            return false; // 유효성 검증 실패
        }

        return true;

    }

    public boolean validatedLogin (JwtTokenLoginRequest request) {
        // 아이디 값이 반값이면 false
        String username = request.getUsername();
        if (username.isEmpty()) {
            return false;
        }

        // 패스워드 값이 빈값이면 false
        String password = request.getPassword();
        if (password.isEmpty()) {
            return false;
        }
    }
}
