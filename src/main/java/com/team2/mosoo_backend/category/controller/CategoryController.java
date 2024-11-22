package com.team2.mosoo_backend.category.controller;

import com.team2.mosoo_backend.category.dto.CategoryRequestDto;
import com.team2.mosoo_backend.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


}
