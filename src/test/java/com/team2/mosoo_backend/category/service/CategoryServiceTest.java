package com.team2.mosoo_backend.category.service;

import com.team2.mosoo_backend.category.entity.Category;
import com.team2.mosoo_backend.category.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

public class CategoryServiceTest {

    private final CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);

    @Test
    public void testCategoryCreation() {
        // Given
        Category category = new Category();
        category.setName("테스트 카테고리");
        category.setDescription("설명");

        // When
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryRepository.findById(category.getCategoryId())).thenReturn(Optional.of(category));

        // Save category
        Category savedCategory = categoryRepository.save(category);
        Category retrievedCategory = categoryRepository.findById(category.getCategoryId()).orElseThrow();

        // Then
        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getName()).isEqualTo("테스트 카테고리");
        assertThat(retrievedCategory.getName()).isEqualTo("테스트 카테고리");
    }
}