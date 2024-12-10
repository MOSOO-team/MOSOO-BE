package com.team2.mosoo_backend.review.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyReviewResponseDto {

    private Long id;

    private String content;

    private int rating;

    private String postTitle;
    private Long postId;
}
