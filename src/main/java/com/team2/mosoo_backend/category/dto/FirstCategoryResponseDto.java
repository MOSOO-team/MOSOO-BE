package com.team2.mosoo_backend.category.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class FirstCategoryResponseDto {
    private Long category_id;
    private String name;
    private String description;
    private String icon;
}
