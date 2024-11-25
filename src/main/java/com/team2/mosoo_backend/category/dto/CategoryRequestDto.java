package com.team2.mosoo_backend.category.dto;

import lombok.Data;

@Data
public class CategoryRequestDto {
    private String name;
    private String description;
    private String icon; // MultipartFile로 받을 예정
    private Long parent_id;
    private int level;
}
