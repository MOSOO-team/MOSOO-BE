package com.team2.mosoo_backend.category.controller;

import com.team2.mosoo_backend.category.dto.CategoryRequestDto;
import com.team2.mosoo_backend.category.dto.CategoryResponseDto;
import com.team2.mosoo_backend.category.dto.FirstCategoryResponseDto;
import com.team2.mosoo_backend.category.dto.SubCategoryResponseDto;
import com.team2.mosoo_backend.category.service.CategoryService;
import com.team2.mosoo_backend.exception.CustomException;
import com.team2.mosoo_backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
                                                              @RequestPart(value = "icon", required = false) MultipartFile file,
                                                              @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        if (userDetails.getAuthorities().stream().noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            throw new CustomException(ErrorCode.USER_NOT_AUTHORIZED);
        }

        CategoryResponseDto categoryResponseDto = categoryService.createCategory(categoryRequestDto, file);
        return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDto);
    }

    // 카테고리 전체 조회
    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> readAllCategories(){
        List<CategoryResponseDto> categories = categoryService.readAllCategories();
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    // 카테고리 대분류 조회
    @GetMapping("/firstCategory")
    public ResponseEntity<List<FirstCategoryResponseDto>> readFirstCategories() {
        List<FirstCategoryResponseDto> categories = categoryService.readFirstCategories();
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    // 하위 카테고리 조회
    @GetMapping("{parentId}")
    public ResponseEntity<List<SubCategoryResponseDto>> readSubCategories(@PathVariable("parentId") Long parentId) {
        List<SubCategoryResponseDto> categories = categoryService.readSubCategories(parentId);
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    // 카테고리 수정
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable("categoryId") Long categoryId, @RequestBody CategoryRequestDto categoryRequestDto,
                                                              @AuthenticationPrincipal UserDetails userDetails){

        if (userDetails.getAuthorities().stream().noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            throw new CustomException(ErrorCode.USER_NOT_AUTHORIZED);
        }

        CategoryResponseDto categoryResponseDto = categoryService.updateCategory(categoryId, categoryRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDto);
    }
    
    // 카테고리 삭제
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> deleteCategory(@PathVariable("categoryId") Long categoryId, @AuthenticationPrincipal UserDetails userDetails){

        if (userDetails.getAuthorities().stream().noneMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            throw new CustomException(ErrorCode.USER_NOT_AUTHORIZED);
        }

        CategoryResponseDto categoryResponseDto = categoryService.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDto);
    }
}
