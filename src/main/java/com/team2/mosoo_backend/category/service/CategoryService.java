package com.team2.mosoo_backend.category.service;

import com.team2.mosoo_backend.category.dto.CategoryRequestDto;
import com.team2.mosoo_backend.category.dto.CategoryResponseDto;
import com.team2.mosoo_backend.category.entity.Category;
import com.team2.mosoo_backend.category.mapper.CategoryMapper;
import com.team2.mosoo_backend.category.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    
    // 카테고리 생성
    @Transactional
    public void createCategory(CategoryRequestDto categoryRequestDto) {
        Category category = CategoryMapper.INSTANCE.toEntity(categoryRequestDto);

        LocalDateTime currentTime = LocalDateTime.now();
        category.setCreatedAt(currentTime);
        category.setUpdatedAt(currentTime);

        if (categoryRequestDto.getParent_id() != null){
           Category parent = categoryRepository.findById(categoryRequestDto.getParent_id())
                   .orElseThrow(IllegalArgumentException::new);

           category.setParent(parent);
           category.setLevel(parent.getLevel() + 1);
        }
        else {
            category.setParent(null);
            category.setLevel(1); // 대분류
        }

        categoryRepository.save(category);
    }

    // 카테고리 전체 조회
    @Transactional
    public List<CategoryResponseDto> readAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return buildCategoryHierarchy(categories);
    }

    // 카테고리 분류
    @Transactional
    private List<CategoryResponseDto> buildCategoryHierarchy(List<Category> categories) {
        Map<Long, CategoryResponseDto> categoryMap = categories.stream()
                .map(CategoryMapper.INSTANCE::toDto)
                .collect(Collectors.toMap(CategoryResponseDto::getCategory_id, category -> category));

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
    @Transactional
    public void updateCategory(Long category_id, CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.findById(category_id).orElseThrow(IllegalArgumentException::new);

        category.setName(categoryRequestDto.getName());
        category.setDescription(categoryRequestDto.getDescription());

        LocalDateTime currentTime = LocalDateTime.now();
        category.setUpdatedAt(currentTime);

        categoryRepository.save(category);
    }
    
    // 카테고리 삭제
    @Transactional
    public void deleteCategory(Long category_id) {
        Category category = categoryRepository.findById(category_id).orElseThrow(IllegalArgumentException::new);

        categoryRepository.delete(category);
    }
}
