package com.team2.mosoo_backend.category.dto;

import lombok.Data;

@Data
public class CategoryRequestDto {
    private String name;
    private String description;
    private Long parentId;
    private int level;
}
