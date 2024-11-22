package com.team2.mosoo_backend.category.service;

import com.team2.mosoo_backend.category.dto.CategoryRequestDto;
import com.team2.mosoo_backend.category.entity.Category;
import com.team2.mosoo_backend.category.mapper.CategoryMapper;
import com.team2.mosoo_backend.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
