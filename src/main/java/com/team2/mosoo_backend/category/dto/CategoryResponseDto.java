package com.team2.mosoo_backend.category.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryResponseDto {
    private Long category_id;
    private String name;
    private String description;
    private String icon;
    private int level;
    private Long parent_id;
    private List<CategoryResponseDto> children = new ArrayList<>();;
}
