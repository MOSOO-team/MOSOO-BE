package com.team2.mosoo_backend.category.mapper;

import com.team2.mosoo_backend.category.dto.CategoryRequestDto;
import com.team2.mosoo_backend.category.dto.CategoryResponseDto;
import com.team2.mosoo_backend.category.dto.FirstCategoryResponseDto;
import com.team2.mosoo_backend.category.dto.SubCategoryResponseDto;
import com.team2.mosoo_backend.category.entity.Category;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(source = "parent_id", target = "parent.category_id")
    @Mapping(target = "level", ignore = true) // level 매핑 제외
    @Mapping(target = "icon", ignore = true) // icon 매핑 제외
    Category toEntity(CategoryRequestDto request);

    @Mapping(source = "parent.category_id", target = "parent_id")
    CategoryResponseDto toDto(Category category);

    FirstCategoryResponseDto firstCategoryToDto(Category category);

    // 리스트 변환
    @IterableMapping(elementTargetType = FirstCategoryResponseDto.class)
    List<FirstCategoryResponseDto> firstCategoryToDtoList(List<Category> categories);

    SubCategoryResponseDto subCategoryToDto(Category category);

    @IterableMapping(elementTargetType = SubCategoryResponseDto.class)
    List<SubCategoryResponseDto> subCategoryToDtoList(List<Category> categories);
}
