package com.team2.mosoo_backend.category.controller;

import com.team2.mosoo_backend.category.dto.CategoryRequestDto;
import com.team2.mosoo_backend.category.dto.CategoryResponseDto;
import com.team2.mosoo_backend.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    // 카테고리 생성
    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestPart(value = "category") CategoryRequestDto categoryRequestDto,
                                                 @RequestPart(value = "icon", required = false) MultipartFile file) throws IOException {
        CategoryResponseDto categoryResponseDto = categoryService.createCategory(categoryRequestDto, file);
        return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDto);
    }

    // 카테고리 전체 조회
    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> readAllCategories(){
        List<CategoryResponseDto> categories = categoryService.readAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    // 카테고리 수정
    @PutMapping("/{category_id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable Long category_id, @RequestBody CategoryRequestDto categoryRequestDto){
        CategoryResponseDto categoryResponseDto = categoryService.updateCategory(category_id, categoryRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDto);
    }
    
    // 카테고리 삭제
    @DeleteMapping("/{category_id}")
    public ResponseEntity<CategoryResponseDto> deleteCategory(@PathVariable Long category_id){
        CategoryResponseDto categoryResponseDto = categoryService.deleteCategory(category_id);
        return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDto);
    }
}
