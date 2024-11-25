package com.team2.mosoo_backend.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostListResponseDto {
    private List<PostResponseDto> postList;
    private int totalPages;
}
