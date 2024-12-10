package com.team2.mosoo_backend.category.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
public class CategoryResponseDto {
    private Long categoryId;
    private String name;
    private String description;
    private String icon;
    private int level;
    private Long parentId;
    private List<CategoryResponseDto> children = new ArrayList<>();;
}
