package com.team2.mosoo_backend.category.controller;

import com.team2.mosoo_backend.category.dto.CategoryRequestDto;
import com.team2.mosoo_backend.category.dto.CategoryResponseDto;
import com.team2.mosoo_backend.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    // 카테고리 생성
    @PostMapping
    public ResponseEntity<String> createCategory(@RequestBody CategoryRequestDto request){
        categoryService.createCategory(request);
        return ResponseEntity.ok("카테고리 생성 성공");
    }

    // 카테고리 전체 조회
    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> readAllCategories(){
        List<CategoryResponseDto> categories = categoryService.readAllCategories();
        return ResponseEntity.ok(categories);
    }

    // 카테고리 수정
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id, @RequestBody CategoryRequestDto request){
        categoryService.updateCategory(id, request);
        return ResponseEntity.ok("카테고리 수정 성공");
    }
    
    // 카테고리 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("카테고리 삭제 성공");
    }
}
