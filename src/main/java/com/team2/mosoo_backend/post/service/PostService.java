package com.team2.mosoo_backend.post.service;


import com.team2.mosoo_backend.post.entity.Post;
import com.team2.mosoo_backend.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;


    public List<Post> getAllPosts() {

        return postRepository.findAll();
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }
}
