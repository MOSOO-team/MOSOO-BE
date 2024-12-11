package com.team2.mosoo_backend.jwt;

import com.team2.mosoo_backend.user.dto.TokenDto;
import com.team2.mosoo_backend.user.entity.Users;
import com.team2.mosoo_backend.user.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {

    @Autowired
    private UserRepository userRepository;

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";

    public static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30L;
//    public static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 30;
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60L * 24 * 7;

    private final Key key;


    // 주의점: 여기서 @Value는 `springframework.beans.factory.annotation.Value`소속이다! lombok의 @Value와 착각하지 말것!
    //     * @param secretKey
    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    // 토큰DTO 생성
    public TokenDto generateTokenDto(Authentication authentication) {
        String accessToken = generateAccessToken(authentication);
        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .tokenExpiresIn(ACCESS_TOKEN_EXPIRE_TIME)
                .build();
    }

    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, ACCESS_TOKEN_EXPIRE_TIME);
    }

    // 1. refresh token 발급
    public String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication, REFRESH_TOKEN_EXPIRE_TIME);
    }

    private String generateToken(Authentication authentication, long expireTime) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expireTime);

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(expiredDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }
        String username = claims.getSubject();

        // 사용자 정보를 레포지토리에서 조회
        Users user = userRepository.findById(Long.parseLong(username))
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));


        // 단일 권한을 가져옴
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getAuthority().toString());

        // UserDetails 생성
        UserDetails principal = new User(username, "", Collections.singletonList(authority));

        return new UsernamePasswordAuthenticationToken(principal, "", Collections.singletonList(authority));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
