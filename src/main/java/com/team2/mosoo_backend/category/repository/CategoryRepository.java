package com.team2.mosoo_backend.category.repository;

import com.team2.mosoo_backend.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.parent.categoryId = :parentId")
    List<Category> findByParentId(@Param("parentId") Long parentId);

    List<Category> findByParentIsNull();
}
