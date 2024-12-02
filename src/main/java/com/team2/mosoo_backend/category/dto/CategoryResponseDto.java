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
    private String message;
    private Long category_id;
    private String name;
    private String description;
    private String icon;
    private int level;
    private Long parent_id;
    private List<CategoryResponseDto> children = new ArrayList<>();;
}
