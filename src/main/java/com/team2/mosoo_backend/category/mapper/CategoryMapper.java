package com.team2.mosoo_backend.category.mapper;

import com.team2.mosoo_backend.category.dto.CategoryRequestDto;
import com.team2.mosoo_backend.category.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(source = "parentId", target = "parent.id")
    @Mapping(target = "level", ignore = true) // level 매핑 제외
    Category toEntity(CategoryRequestDto request);
}
