package com.team2.mosoo_backend.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@SQLDelete(sql = "UPDATE member SET is_deleted = true WHERE member_id = ?")
@Where(clause = "is_deleted = false")
@EntityListeners(AuditingEntityListener.class)
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private Long id;
    @Column(nullable = false)
    private String email;
    private String password;

    @Column(name = "fullname")
    private String fullName;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @OneToOne(mappedBy = "users")
    private UserInfo userInfo; // UserInfo와의 관계

    private boolean isDeleted;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPassword(String password) { this.password = password; }


    public void setAuthority(Authority authority) { this.authority = authority; }

    public void setIsDeleted(boolean isDeleted) { this.isDeleted = isDeleted; }


    @Builder
    public Users(Long id, String email, String password, String fullName, Authority authority, boolean isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.authority = authority;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}