package com.team2.mosoo_backend.review.repository;


import com.team2.mosoo_backend.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByPostId(Long postId);

    List<Review> findAllByUserId(Long userId);
}
