package com.team2.mosoo_backend.user.entity;


import com.team2.mosoo_backend.common.entity.BaseEntity;
import com.team2.mosoo_backend.user.dto.response.UserResponseDto;
import com.team2.mosoo_backend.user.dto.response.UsersInfoResponseDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Users extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "fullname", nullable = false)
    private String fullname; // 사용자 이름

    @Column(name = "username", nullable = false, unique = true)
    private String username; // 사용자 로그인 계정

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Column(name = "delete_reason")
    private String deletedReason;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsersInfo> usersInfoList = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { // Spring Sercurity에서 사용자 권한을 반환
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override // 사용자 ID 반환 (고유 값)
    public String getUsername() { return username; }

    @Override // 사용자 비밀번호 반환
    public String getPassword() { return password; }

    @Override // 계정 만료 여부
    public boolean isAccountNonExpired() {return true; }

    @Override // 계정 잠금 여부 반환
    public boolean isAccountNonLocked() { return true; }

    public UserResponseDto toResponseDto() {
        // UsersInfo를 UsersInfoResponseDto로 변경
        List<UsersInfoResponseDto> usersInfoDtos = usersInfoList.stream()
                .map(UsersInfo::toUsersInfoResponseDto)
                .toList();

        return UserResponseDto.builder()
                .userId(id)
                .email(email)
                .fullname(fullname)
                .username(username)
                .provider(provider)
                .role(role)
                .isDeleted(isDeleted)
//                .createdAt(getCreatedDate())
                .userInfoList(usersInfoDtos)
                .message("사용자 있음")
                .build();
    }
}
