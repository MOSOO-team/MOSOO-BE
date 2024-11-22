package com.team2.mosoo_backend.post.service;


import com.team2.mosoo_backend.post.dto.CreatePostRequestDto;
import com.team2.mosoo_backend.post.dto.CreatePostResponseDto;
import com.team2.mosoo_backend.post.dto.PostListResponseDto;
import com.team2.mosoo_backend.post.dto.PostResponseDto;
import com.team2.mosoo_backend.post.entity.Post;
import com.team2.mosoo_backend.post.mapper.PostMapper;
import com.team2.mosoo_backend.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;


    public PostListResponseDto getAllPosts() {

        List<Post> posts = postRepository.findAll();

        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        for (Post post : posts) {
            postResponseDtoList.add(postMapper.postToPostResponseDto(post));
        }

        return new PostListResponseDto(postResponseDtoList);

    }

    public CreatePostResponseDto createPost(CreatePostRequestDto createPostRequestDto) {

        Post post = postMapper.createPostRequestDtoToPost(createPostRequestDto);

        CreatePostResponseDto createPostResponseDto = postMapper.postToCreatePostResponseDto(postRepository.save(post));

        return createPostResponseDto;
    }

    public PostListResponseDto getPostsByIsOffer(boolean isOffer) {

        List<Post> posts = postRepository.findAllByIsOffer(isOffer);

        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        for (Post post : posts) {
            postResponseDtoList.add(postMapper.postToPostResponseDto(post));
        }

        return new PostListResponseDto(postResponseDtoList);
    }
}
