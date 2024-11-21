package com.team2.mosoo_backend.post.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post")
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description",nullable = true)
    private String description;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "duration")
    private String duration;

    @Column(name = "is_offer", nullable = false)
    private boolean isOffer;

    @Column(name = "is_selected", nullable = false)
    private boolean isSelected;

    @Column(name = "is_expired", nullable = false)
    private boolean isExpired;

//    todo: 연관 관계 추가 (카테고리 + 작성자)

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


}
