package com.team2.mosoo_backend.review.mapper;


import com.team2.mosoo_backend.review.dto.CreateReviewRequestDto;
import com.team2.mosoo_backend.review.dto.ReviewResponseDto;
import com.team2.mosoo_backend.review.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    ReviewResponseDto reviewToReviewResponseDto(Review review);

    Review createReviewRequestDtoToReview(CreateReviewRequestDto createReviewRequestDto);
}
