package com.team2.mosoo_backend.post.mapper;


import com.team2.mosoo_backend.post.dto.CreatePostRequestDto;
import com.team2.mosoo_backend.post.dto.CreatePostResponseDto;
import com.team2.mosoo_backend.post.dto.PostResponseDto;
import com.team2.mosoo_backend.post.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface PostMapper {

    PostResponseDto postToPostResponseDto(Post post);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Post createPostRequestDtoToPost(CreatePostRequestDto CreatePostRequestDto);

    CreatePostResponseDto postToCreatePostResponseDto(Post post);
}
