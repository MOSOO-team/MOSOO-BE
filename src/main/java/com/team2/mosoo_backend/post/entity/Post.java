package com.team2.mosoo_backend.post.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post")
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", unique = true, nullable = false)
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
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setIsOffer(boolean isOffer) {
        this.isOffer = isOffer;
    }


    @Builder
    public Post(Long id, String title, String description, int price, String duration, boolean isOffer, boolean isSelected, boolean isExpired) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.isOffer = isOffer;
        this.isSelected = isSelected;
        this.isExpired = isExpired;
    }

}
