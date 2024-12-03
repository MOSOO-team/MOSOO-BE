package com.team2.mosoo_backend.user.service;


import com.team2.mosoo_backend.exception.CustomException;
import com.team2.mosoo_backend.exception.ErrorCode;
import com.team2.mosoo_backend.jwt.TokenProvider;

import com.team2.mosoo_backend.oath.repository.RefreshTokenRepository;
import com.team2.mosoo_backend.oath.util.RefreshTokenCookieUtil;
import com.team2.mosoo_backend.user.dto.TokenDto;
import com.team2.mosoo_backend.user.dto.UserReqeustDto;
import com.team2.mosoo_backend.user.dto.UserResponseDto;
import com.team2.mosoo_backend.user.entity.Authority;
import com.team2.mosoo_backend.user.entity.User;
import com.team2.mosoo_backend.user.mapper.UserMapper;
import com.team2.mosoo_backend.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthenticationManagerBuilder managerBuilder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenCookieUtil refreshTokenCookieUtil;

    public UserResponseDto signup(UserReqeustDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE);
        }
        if (userRepository.findByFullName(requestDto.getFullName()).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE);
        }

        User user = userMapper.requestToUser(requestDto);
        user.setAuthority(Authority.ROLE_USER);
        return userMapper.userToResponse(userRepository.save(user));
    }

    public TokenDto login(HttpServletRequest request, HttpServletResponse response, UserReqeustDto requestDto) {

        // 비밀번호 미 입력 시
        if(requestDto.getPassword() == null || requestDto.getPassword().isEmpty()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        String refreshTokenValue = tokenProvider.generateRefreshToken(authentication);

        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        refreshTokenCookieUtil.saveRefreshToken(user.getId(), refreshTokenValue);
        refreshTokenCookieUtil.addRefreshTokenToCookie(request, response, refreshTokenValue); // 리프레시 토큰을 쿠키에 추가

        return tokenDto;
    }

}
