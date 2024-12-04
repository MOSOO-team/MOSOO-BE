package com.team2.mosoo_backend.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name ="UserInfo")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userInfo_id", unique = true, nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users users; // Users 엔티티와의 관계

    private String address;
    private Boolean isGosu;
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "userInfo") // Gosu와의 관계
    private Gosu gosu; // Gosu 엔티티와의 관계

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }



}
