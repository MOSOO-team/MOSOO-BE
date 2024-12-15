package com.team2.mosoo_backend.oath.service;


import com.team2.mosoo_backend.exception.CustomException;
import com.team2.mosoo_backend.exception.ErrorCode;
import com.team2.mosoo_backend.jwt.TokenProvider;
import com.team2.mosoo_backend.user.entity.Users;
import com.team2.mosoo_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;


    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if(!tokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }
        // 리프레시 토큰에서 사용자 정보 추출
        Authentication authentication = tokenProvider.getAuthentication(refreshToken);

        Long userId = Long.parseLong(((UserDetails) authentication.getPrincipal()).getUsername()); // UserDetails가 ID를 제공하는 경우

        Users user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        // 새로운 JWT 액세스 토큰 생성
        return tokenProvider.generateAccessToken(userId, user.getAuthority());

    }
}
