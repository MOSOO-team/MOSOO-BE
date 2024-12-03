package com.team2.mosoo_backend.config;


import com.team2.mosoo_backend.exception.CustomException;
import com.team2.mosoo_backend.exception.ErrorCode;
import com.team2.mosoo_backend.user.entity.Users;
import com.team2.mosoo_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SecurityUtil {

    private final UserRepository userRepository;

    public Long getCurrentMemberId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Security Context에 인증 정보가 없습니다.");
        }
        authentication.getDetails();

        try {
            // 인증된 이름을 ID로 변환하여 반환
            return Long.parseLong(authentication.getName());
        } catch (NumberFormatException e) {
            // 파싱 실패 시 memberRepository를 사용하여 이름으로 멤버 ID 조회
            Users users = userRepository.findByFullName(authentication.getName())
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            // 조회된 멤버의 ID 반환
            return users.getId();
        }
    }
}
