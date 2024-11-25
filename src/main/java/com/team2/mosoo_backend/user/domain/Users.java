package com.team2.mosoo_backend.user.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

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
    private List<UserInfo> userInfoList = new ArrayList<>();




}
