package com.team2.mosoo_backend.post.controller;


import com.team2.mosoo_backend.exception.CustomException;
import com.team2.mosoo_backend.post.dto.*;
import com.team2.mosoo_backend.post.service.PostService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;


    // 게시글 전체 조회 요청
    @GetMapping("/posts")
    public ResponseEntity<PostListResponseDto> getAllPosts(
            @RequestParam(required = false, value = "page", defaultValue = "1") @Positive int page) {

        PostListResponseDto postList = postService.getAllPosts(page);

        return ResponseEntity.status(200).body(postList);
    }

    // 게시글 작성 요청
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreatePostResponseDto> createRequestPost(
            @ModelAttribute CreatePostRequestDto createPostRequestDto,
            @RequestParam("isOffer") boolean isOffer) throws IOException {

        CreatePostResponseDto createPost = postService.createPost(createPostRequestDto, isOffer);

        return ResponseEntity.status(201).body(createPost);
    }

    // 고수 / 일반 게시글 조회 요청
    @GetMapping
    public ResponseEntity<PostListResponseDto> getOfferPosts(
            @RequestParam(required = false, value = "page", defaultValue = "1") @Positive int page,
            @RequestParam(value = "isOffer") boolean isOffer) {

        PostListResponseDto postList = postService.getPostsByIsOffer(page, isOffer);

        return ResponseEntity.status(200).body(postList);
    }

    // 카테고리별 게시글
    @GetMapping("/getBy/{categoryId}")
    public ResponseEntity<SearchedPostListResponseDto> getPostByCategoryId(
            @RequestParam(required = false, value = "page", defaultValue = "1") @Positive int page,
            @PathVariable("categoryId") Long categoryId,
            @RequestParam(value = "isOffer") boolean isOffer,
            @RequestParam(required = false, value = "keyword", defaultValue = "") String keyword,
            @RequestParam(required = false, value = "address", defaultValue = "") String address) {

        SearchedPostListResponseDto postList = postService.getSearchedPost(page, categoryId, isOffer, keyword, address);

        return ResponseEntity.status(200).body(postList);

    }

    // 게시글 단건 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPostById(
            @PathVariable("postId") Long postId){
        PostResponseDto postResponseDto = postService.getPostById(postId);

        return ResponseEntity.status(200).body(postResponseDto);
    }

    // 게시글 수정 요청
    @PutMapping("")
    public ResponseEntity<PostResponseDto> updatePost(@RequestBody PostUpdateRequestDto postUpdateRequestDto) {
        PostResponseDto postResponseDto = postService.updatePost(postUpdateRequestDto);

        return ResponseEntity.status(201).body(postResponseDto);
    }

    // 게시글 삭제 요청
    @DeleteMapping("/{postId}")
    public ResponseEntity<PostResponseDto> deletePost(@PathVariable("postId") Long postId) {
        PostResponseDto postResponseDto = postService.deletePost(postId);
        return ResponseEntity.status(204).body(postResponseDto);
    }

    // 게시글 상태 수정 요청
    @PutMapping("/status/{postId}")
    public ResponseEntity<PostResponseDto> updatePostStatus(@RequestBody UpdatePostStatusRequestDto updatePostStatusRequestDto){
        PostResponseDto postResponseDto = postService.updatePostStatus(updatePostStatusRequestDto);

        return ResponseEntity.status(201).body(postResponseDto);
    }


}
