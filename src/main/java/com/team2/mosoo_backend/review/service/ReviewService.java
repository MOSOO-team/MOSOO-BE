package com.team2.mosoo_backend.review.service;


import com.team2.mosoo_backend.exception.CustomException;
import com.team2.mosoo_backend.exception.ErrorCode;
import com.team2.mosoo_backend.post.entity.Post;
import com.team2.mosoo_backend.post.repository.PostRepository;
import com.team2.mosoo_backend.review.dto.*;
import com.team2.mosoo_backend.review.entity.Review;
import com.team2.mosoo_backend.review.mapper.ReviewMapper;
import com.team2.mosoo_backend.review.repository.ReviewRepository;
import com.team2.mosoo_backend.user.entity.Users;
import com.team2.mosoo_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;
    private final PostRepository postRepository;


    // 게시글 내의 후기 조회
    public ReviewListResponseDto getReviewByPostId(Long postId) {

        List<Review> reviewList = reviewRepository.findAllByPostId(postId);
        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();


        for (Review review : reviewList) {
            ReviewResponseDto reviewResponseDto = reviewMapper.reviewToReviewResponseDto(review);
            reviewResponseDto.setFullName(review.getUser().getFullName());
            reviewResponseDtoList.add(reviewResponseDto);
        }

        return new ReviewListResponseDto(reviewResponseDtoList);

    }

    // 본인 후기 조회
    public MyReviewListResponseDto getReviewByUserId(Long userId) {
        List<Review> reviewList = reviewRepository.findAllByUserId(userId);
        List<MyReviewResponseDto> reviewResponseDtoList = new ArrayList<>();


        for (Review review : reviewList) {
            MyReviewResponseDto myReviewResponseDto = reviewMapper.reviewToMyReviewResponseDto(review);
            myReviewResponseDto.setPostTitle(review.getPost().getTitle());
            myReviewResponseDto.setPostId(review.getPost().getId());

            reviewResponseDtoList.add(myReviewResponseDto);
        }

        return new MyReviewListResponseDto(reviewResponseDtoList);
    }

    // 후기 생성
    @Transactional
    public ReviewResponseDto createReview(Long userId, Long postId, CreateReviewRequestDto createReviewRequestDto) {

        Review review = reviewMapper.createReviewRequestDtoToReview(createReviewRequestDto);
        Users user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        review.setMapping(user, post);

        Review savedReview = reviewRepository.save(review);

        return reviewMapper.reviewToReviewResponseDto(savedReview);
    }

    // 후기 수정
    @Transactional
    public ReviewResponseDto updateReview(UpdateReviewRequestDto updateReviewRequestDto) {

        Review review = reviewRepository.findById(updateReviewRequestDto.getId()).orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        review.update(updateReviewRequestDto);

        return reviewMapper.reviewToReviewResponseDto(review);
    }

    // 후기 삭제
    @Transactional
    public ReviewResponseDto deleteReview(Long reviewId) {

        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        reviewRepository.deleteById(reviewId);

        return reviewMapper.reviewToReviewResponseDto(review);
    }


}
