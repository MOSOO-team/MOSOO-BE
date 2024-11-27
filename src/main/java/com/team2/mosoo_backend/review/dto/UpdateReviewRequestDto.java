package com.team2.mosoo_backend.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReviewRequestDto {

    private long id;

    private String content;

    private int rating;
}
