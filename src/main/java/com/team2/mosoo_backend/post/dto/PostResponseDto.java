package com.team2.mosoo_backend.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {

    private Long id;
    private String title;
    private String description;
    private int price;
    private String duration;
    private boolean isOffer;
    private LocalDateTime updatedAt;
    private String fullName;

    private List<String> imgUrls;

}
