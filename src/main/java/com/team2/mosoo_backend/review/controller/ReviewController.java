package com.team2.mosoo_backend.review.controller;


import com.team2.mosoo_backend.review.dto.CreateReviewRequestDto;
import com.team2.mosoo_backend.review.dto.ReviewListResponseDto;
import com.team2.mosoo_backend.review.dto.ReviewResponseDto;
import com.team2.mosoo_backend.review.dto.UpdateReviewRequestDto;
import com.team2.mosoo_backend.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;


    @GetMapping("/{postId}")
    public ResponseEntity<ReviewListResponseDto> getReviewByPostId(@PathVariable("postId") Long postId) {

        ReviewListResponseDto reviewListResponseDto = reviewService.getReviewByPostId(postId);

        return ResponseEntity.status(200).body(reviewListResponseDto);

    }

    @GetMapping("/my/{userId}")
    public ResponseEntity<ReviewListResponseDto> getReviewByUserId(@PathVariable("userId") Long userId) {

        ReviewListResponseDto reviewListResponseDto = reviewService.getReviewByUserId(userId);

        return ResponseEntity.status(200).body(reviewListResponseDto);
    }


    @PostMapping("/{postId}")
    public ResponseEntity<ReviewResponseDto> createReview(
            @PathVariable("postId") Long postId,
            @RequestBody CreateReviewRequestDto createReviewRequestDto,
            @RequestParam("userId") Long userId) {

        ReviewResponseDto reviewResponseDto = reviewService.createReview(userId, postId, createReviewRequestDto);

        return ResponseEntity.status(201).body(reviewResponseDto);

    }

    @PutMapping
    public ResponseEntity<ReviewResponseDto> updateReview(@RequestBody UpdateReviewRequestDto updateReviewRequestDto) {

        ReviewResponseDto reviewResponseDto = reviewService.updateReview(updateReviewRequestDto);

        return ResponseEntity.status(201).body(reviewResponseDto);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> deleteReview(@PathVariable("reviewId") Long reviewId) {

        ReviewResponseDto reviewResponseDto = reviewService.deleteReview(reviewId);

        return ResponseEntity.status(200).body(reviewResponseDto);
    }


}
