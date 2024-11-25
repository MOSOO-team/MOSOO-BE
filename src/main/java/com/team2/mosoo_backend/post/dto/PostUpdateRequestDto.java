package com.team2.mosoo_backend.post.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostUpdateRequestDto {

    private long id;
    private String title;
    private String description;
    private int price;
    private String duration;

}
