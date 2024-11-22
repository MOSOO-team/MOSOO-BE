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

    @PostMapping("/createPost")
    public ResponseEntity<CreatePostResponseDto> createPost(@RequestBody CreatePostRequestDto createPostRequestDto) {
        CreatePostResponseDto createPost = postService.createPost(createPostRequestDto);

        return ResponseEntity.status(201).body(createPost);
    }
}
