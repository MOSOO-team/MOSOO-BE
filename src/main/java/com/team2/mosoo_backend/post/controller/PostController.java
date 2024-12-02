package com.team2.mosoo_backend.post.controller;


import com.team2.mosoo_backend.post.dto.*;
import com.team2.mosoo_backend.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;


    @GetMapping("/posts")
    public ResponseEntity<PostListResponseDto> getAllPosts() {

        PostListResponseDto postList = postService.getAllPosts();

        return ResponseEntity.status(200).body(postList);
    }

    @PostMapping("/")
    public ResponseEntity<CreatePostResponseDto> createRequestPost(@RequestBody CreatePostRequestDto createPostRequestDto) {

        CreatePostResponseDto createPost = postService.createPost(createPostRequestDto);

        return ResponseEntity.status(201).body(createPost);
    }

    @GetMapping("/")
    public ResponseEntity<PostListResponseDto> getOfferPosts(@RequestParam(value = "isOffer") boolean isOffer) {
        PostListResponseDto postList = postService.getPostsByIsOffer(isOffer);

        return ResponseEntity.status(200).body(postList);
    }

    @PutMapping("/")
    public ResponseEntity<PostResponseDto> updatePost(@RequestBody PostUpdateRequestDto postUpdateRequestDto) {
        PostResponseDto postResponseDto = postService.updatePost(postUpdateRequestDto);

        return ResponseEntity.status(201).body(postResponseDto);
    }

    @DeleteMapping("/")
    public ResponseEntity<String> deletePost(@RequestParam(value = "id") Long id) {
        postService.deletePost(id);
        return ResponseEntity.status(204).build();
    }


}
