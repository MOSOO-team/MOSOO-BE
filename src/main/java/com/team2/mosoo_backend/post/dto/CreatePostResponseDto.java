package com.team2.mosoo_backend.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostResponseDto {

    private long postId;
    private String title;
    private String description;
    private int price;
    private String duration;
    private String address;
    private boolean isOffer;

}
