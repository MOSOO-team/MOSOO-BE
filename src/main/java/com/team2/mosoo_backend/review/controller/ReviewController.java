package com.team2.mosoo_backend.review.controller;


import com.team2.mosoo_backend.review.dto.*;
import com.team2.mosoo_backend.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;


    // 게시글 내의 후기 조회
    @GetMapping("/{postId}")
    public ResponseEntity<ReviewListResponseDto> getReviewByPostId(@PathVariable("postId") Long postId) {

        ReviewListResponseDto reviewListResponseDto = reviewService.getReviewByPostId(postId);

        return ResponseEntity.status(200).body(reviewListResponseDto);

    }

    // 로그인한 유저의 후기 조회
    @GetMapping("/myReview")
    public ResponseEntity<MyReviewListResponseDto> getReviewByUserId(@AuthenticationPrincipal UserDetails userDetails) {

        MyReviewListResponseDto reviewListResponseDto = reviewService.getReviewByUserId(Long.parseLong(userDetails.getUsername()));

        return ResponseEntity.status(200).body(reviewListResponseDto);
    }

    // 후기 작성
    @PostMapping("/{postId}")
    public ResponseEntity<ReviewResponseDto> createReview(
            @PathVariable("postId") Long postId,
            @RequestBody CreateReviewRequestDto createReviewRequestDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        ReviewResponseDto reviewResponseDto = reviewService.createReview(Long.parseLong(userDetails.getUsername()), postId, createReviewRequestDto);

        return ResponseEntity.status(201).body(reviewResponseDto);

    }

    // 후기 수정
    @PutMapping
    public ResponseEntity<ReviewResponseDto> updateReview(@RequestBody UpdateReviewRequestDto updateReviewRequestDto) {

        ReviewResponseDto reviewResponseDto = reviewService.updateReview(updateReviewRequestDto);

        return ResponseEntity.status(201).body(reviewResponseDto);
    }

    // 후기 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> deleteReview(@PathVariable("reviewId") Long reviewId) {

        ReviewResponseDto reviewResponseDto = reviewService.deleteReview(reviewId);

        return ResponseEntity.status(200).body(reviewResponseDto);
    }


}
