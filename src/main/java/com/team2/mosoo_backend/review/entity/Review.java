package com.team2.mosoo_backend.review.entity;


import com.team2.mosoo_backend.common.entity.BaseEntity;
import com.team2.mosoo_backend.post.entity.Post;
import com.team2.mosoo_backend.review.dto.UpdateReviewRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review")
@EntityListeners(AuditingEntityListener.class)
public class Review extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private int rating;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public void update(UpdateReviewRequestDto updateReviewRequestDto) {
        this.content = updateReviewRequestDto.getContent();
        this.rating = updateReviewRequestDto.getRating();
    }

    // todo: 유저 연관 매핑


}