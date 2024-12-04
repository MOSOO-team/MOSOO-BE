package com.team2.mosoo_backend.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "GosuInfo")
public class Gosu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "technology_provider_id", nullable = false)
    private Long technologyProviderId; // 기술 제공 ID

    @NotNull
    @Column(name = "user_info_id", nullable = false)
    private Long userInfoId; // 유저 정보 ID

    @NotNull
    @Column(nullable = false)
    private String gender; // 성별

    @NotNull
    @Column(name = "business_name", nullable = false)
    private String businessName; // 사업자명

    @NotNull
    @Column(name = "business_number", nullable = false)
    private String businessNumber; // 사업자 번호

    @NotNull
    @Column(name = "gosuinfo_address", nullable = false)
    private String gosuInfoAddress; // 기술 제공 주소

    @NotNull
    @Column(name = "gosuinfo_phone", nullable = false)
    private String gosuInfoPhone; // 기술 제공 번호

    @NotNull
    @Column(name = "category", nullable = false)
    private String category; // 기술 제공 카테고리

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 생성일

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; // 수정일

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt; // 탈퇴 시간

    @Column
    private Integer points; // 포인트
}
