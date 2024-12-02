package com.team2.mosoo_backend.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequestDto {

    private String title;
    private String description;
    private int price;
    private String duration;
    private List<MultipartFile> imageUrls;
}
