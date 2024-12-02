package com.team2.mosoo_backend.review.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewListResponseDto {

    private List<ReviewResponseDto> reviews;

}
