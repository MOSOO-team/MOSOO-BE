package com.team2.mosoo_backend.oath.controller;


import com.team2.mosoo_backend.exception.CustomException;
import com.team2.mosoo_backend.exception.ErrorCode;
import com.team2.mosoo_backend.oath.dto.CreateAccessTokenResponse;
import com.team2.mosoo_backend.oath.entity.RefreshToken;
import com.team2.mosoo_backend.oath.repository.RefreshTokenRepository;
import com.team2.mosoo_backend.oath.service.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenApiController {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenService tokenService;

    @PostMapping("/api/auth/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {

        RefreshToken refreshToken =  refreshTokenRepository.findByMemberId(Long.parseLong(userDetails.getUsername())).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String newAccessToken = tokenService.createNewAccessToken(refreshToken.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponse(newAccessToken));
    }
}
