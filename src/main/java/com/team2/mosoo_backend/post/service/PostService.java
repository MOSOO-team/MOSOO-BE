package com.team2.mosoo_backend.post.service;


import com.team2.mosoo_backend.exception.CustomException;
import com.team2.mosoo_backend.exception.ErrorCode;
import com.team2.mosoo_backend.post.dto.*;
import com.team2.mosoo_backend.post.entity.Post;
import com.team2.mosoo_backend.post.mapper.PostMapper;
import com.team2.mosoo_backend.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;


    public PostListResponseDto getAllPosts(int page) {

        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("id").descending());

        Page<Post> posts = postRepository.findById(pageable, 1L);

        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        for (Post post : posts) {
            postResponseDtoList.add(postMapper.postToPostResponseDto(post));
        }

        int totalPages = (posts.getTotalPages() == 0 ? 1 : posts.getTotalPages());

        return new PostListResponseDto(postResponseDtoList, totalPages);

    }

    @Transactional
    public CreatePostResponseDto createPost(CreatePostRequestDto createPostRequestDto) {

        Post post = postMapper.createPostRequestDtoToPost(createPostRequestDto);
        post.setIsOffer(createPostRequestDto.isOffer());

        return postMapper.postToCreatePostResponseDto(postRepository.save(post));
    }

    public PostListResponseDto getPostsByIsOffer(int page, boolean isOffer) {

        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("id").descending());

        Page<Post> posts = postRepository.findByIsOffer(pageable, isOffer);

        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        for (Post post : posts) {
            postResponseDtoList.add(postMapper.postToPostResponseDto(post));
        }

        int totalPages = (posts.getTotalPages() == 0 ? 1 : posts.getTotalPages());

        return new PostListResponseDto(postResponseDtoList, totalPages);
    }

    @Transactional
    public PostResponseDto updatePost(PostUpdateRequestDto postUpdateRequestDto) {
        Post post = postRepository.findById(postUpdateRequestDto.getId()).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        Post savedPost = update(post, postUpdateRequestDto);

        return postMapper.postToPostResponseDto(savedPost);
    }

    private Post update(Post existPost, PostUpdateRequestDto postUpdateRequestDto) {
        existPost.setTitle(postUpdateRequestDto.getTitle());
        existPost.setDescription(postUpdateRequestDto.getDescription());
        existPost.setPrice(postUpdateRequestDto.getPrice());
        existPost.setDuration(postUpdateRequestDto.getDuration());
        return postRepository.save(existPost);
    }

    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
