package com.team2.mosoo_backend.post.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchedPostListResponseDto {
    private List<PostResponseDto> postList;
    private int totalPages;

    private String keyword;
    private String address;
    private String categoryName;
}
