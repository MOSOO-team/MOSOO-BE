package com.team2.mosoo_backend.post.controller;


import com.team2.mosoo_backend.post.dto.CreatePostRequestDto;
import com.team2.mosoo_backend.post.dto.CreatePostResponseDto;
import com.team2.mosoo_backend.post.dto.PostListResponseDto;
import com.team2.mosoo_backend.post.entity.Post;
import com.team2.mosoo_backend.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;


    @GetMapping("/posts")
    public ResponseEntity<PostListResponseDto> getAllPosts() {

        PostListResponseDto postList = postService.getAllPosts();

        return ResponseEntity.status(200).body(postList);
    }

    @PostMapping("/createOfferPost")
    public ResponseEntity<CreatePostResponseDto> createOfferPost(@RequestBody CreatePostRequestDto createPostRequestDto) {

        boolean isOffer = true;
        CreatePostResponseDto createPost = postService.createPost(createPostRequestDto, isOffer);

        return ResponseEntity.status(201).body(createPost);
    }

    @PostMapping("/createRequestPost")
    public ResponseEntity<CreatePostResponseDto> createRequestPost(@RequestBody CreatePostRequestDto createPostRequestDto) {

        boolean isOffer = false;
        CreatePostResponseDto createPost = postService.createPost(createPostRequestDto, isOffer);

        return ResponseEntity.status(201).body(createPost);
    }

    @GetMapping("/offerPosts")
    public ResponseEntity<PostListResponseDto> getOfferPosts() {
        PostListResponseDto postList = postService.getPostsByIsOffer(true);

        return ResponseEntity.status(200).body(postList);
    }

    @GetMapping("/requestPosts")
    public ResponseEntity<PostListResponseDto> getRequestPosts() {
        PostListResponseDto postList = postService.getPostsByIsOffer(false);

        return ResponseEntity.status(200).body(postList);
    }
}
