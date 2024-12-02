package com.team2.mosoo_backend.category.service;

import com.team2.mosoo_backend.category.dto.CategoryRequestDto;
import com.team2.mosoo_backend.category.dto.CategoryResponseDto;
import com.team2.mosoo_backend.category.entity.Category;
import com.team2.mosoo_backend.category.mapper.CategoryMapper;
import com.team2.mosoo_backend.category.repository.CategoryRepository;
import com.team2.mosoo_backend.exception.CustomException;
import com.team2.mosoo_backend.exception.ErrorCode;
import com.team2.mosoo_backend.utils.s3bucket.service.S3BucketService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    private final S3BucketService s3BucketService;
    
    // 카테고리 생성
    @Transactional
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto, MultipartFile file) throws IOException {
        Category category = CategoryMapper.INSTANCE.toEntity(categoryRequestDto);

        LocalDateTime currentTime = LocalDateTime.now();
        category.setCreatedAt(currentTime);
        category.setUpdatedAt(currentTime);

        if (categoryRequestDto.getParent_id() != null){
            Category parent = categoryRepository.findById(categoryRequestDto.getParent_id())
                    .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

           category.setParent(parent);
           category.setLevel(parent.getLevel() + 1);
        }
        else {
            category.setParent(null);
            category.setLevel(1); // 대분류
            try {
                String fileUrl = s3BucketService.uploadFile(file);
                category.setIcon(fileUrl);
            } catch (IOException e) {
                throw new CustomException(ErrorCode.INVALID_FILE_DATA);
            }
        }

        categoryRepository.save(category);
        CategoryResponseDto categoryResponseDto = CategoryMapper.INSTANCE.toDto(category);
        categoryResponseDto.setMessage("카테고리 생성 성공");
        return categoryResponseDto;
    }

    // 카테고리 전체 조회
    @Transactional
    public List<CategoryResponseDto> readAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponseDto> categoryResponseDtos = buildCategoryHierarchy(categories);

        for (CategoryResponseDto category : categoryResponseDtos) {
            category.setMessage("카테고리 전체 조회 성공");
        }

        return categoryResponseDtos;
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
    public CategoryResponseDto updateCategory(Long category_id, CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.findById(category_id)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        category.setName(categoryRequestDto.getName());
        category.setDescription(categoryRequestDto.getDescription());

        LocalDateTime currentTime = LocalDateTime.now();
        category.setUpdatedAt(currentTime);

        categoryRepository.save(category);
        CategoryResponseDto categoryResponseDto = CategoryMapper.INSTANCE.toDto(category);
        categoryResponseDto.setMessage("카테고리 수정 성공");
        return categoryResponseDto;
    }
    
    // 카테고리 삭제
    @Transactional
    public CategoryResponseDto deleteCategory(Long category_id) {
        Category category = categoryRepository.findById(category_id)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        CategoryResponseDto categoryResponseDto = CategoryMapper.INSTANCE.toDto(category);
        categoryRepository.delete(category);
        categoryResponseDto.setMessage("카테고리 삭제 성공");
        return categoryResponseDto;
    }
}
