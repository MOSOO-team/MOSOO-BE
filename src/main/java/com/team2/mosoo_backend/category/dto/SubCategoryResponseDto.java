package com.team2.mosoo_backend.category.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SubCategoryResponseDto {
    private Long categoryId;
    private String name;
    private String description;
}
