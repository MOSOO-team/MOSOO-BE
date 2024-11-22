package com.team2.mosoo_backend.category.service;

import com.team2.mosoo_backend.category.dto.CategoryRequestDto;
import com.team2.mosoo_backend.category.dto.CategoryResponseDto;
import com.team2.mosoo_backend.category.entity.Category;
import com.team2.mosoo_backend.category.mapper.CategoryMapper;
import com.team2.mosoo_backend.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    
    // 카테고리 생성
    public void createCategory(CategoryRequestDto request) {
        Category category = CategoryMapper.INSTANCE.toEntity(request);

        if (request.getParent_id() != null){
           Category parent = categoryRepository.findById(request.getParent_id())
                   .orElseThrow(IllegalArgumentException::new);

           category.setParent(parent);
           category.setLevel(parent.getLevel() + 1);
        }
        else {
            category.setLevel(1); // 대분류
        }

        categoryRepository.save(category);
    }

    // 카테고리 전체 조회
    public List<CategoryResponseDto> readAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return buildCategoryHierarchy(categories);
    }

    // 카테고리 분류
    private List<CategoryResponseDto> buildCategoryHierarchy(List<Category> categories) {
        Map<Long, CategoryResponseDto> categoryMap = categories.stream()
                .map(CategoryMapper.INSTANCE::toDto)
                .collect(Collectors.toMap(CategoryResponseDto::getId, category -> category));

        List<CategoryResponseDto> roots = new ArrayList<>();
        for (CategoryResponseDto category : categoryMap.values()) {
            if (category.getParent_id() == null) {
                roots.add(category); // 부모가 없는 대분류
            } else {
                CategoryResponseDto parent = categoryMap.get(category.getParent_id());
                if (parent != null) {
                    parent.getChildren().add(category);
                }
            }
        }
        return roots;
    }

    // 카테고리 수정
    public void updateCategory(Long id, CategoryRequestDto request) {
        Category category = categoryRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        categoryRepository.save(category);
    }
    
    // 카테고리 삭제
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        categoryRepository.delete(category);
    }
}
