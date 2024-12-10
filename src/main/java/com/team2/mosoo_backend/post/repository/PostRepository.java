package com.team2.mosoo_backend.post.repository;


import com.team2.mosoo_backend.category.entity.Category;
import com.team2.mosoo_backend.post.entity.Post;
import com.team2.mosoo_backend.user.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByIsOffer(Pageable pageable, boolean isOffer);

    Page<Post> findAll(Pageable pageable);

    Page<Post> findByIsOfferAndCategory(Pageable pageable, boolean isOffer, Category category);

    Page<Post> findByTitleContainingAndAddressContainingAndIsOfferAndCategory(
            String keyword,
            String address,
            boolean isOffer,
            Category category,
            Pageable pageable);

    Page<Post> findByTitleContainingAndIsOfferAndCategory(String keyword, boolean isOffer, Category category, Pageable pageable);

    Page<Post> findByAddressContainingAndIsOfferAndCategory(String address, boolean isOffer, Category category, Pageable pageable);

    Page<Post> findByTitleContainingAndAddressContainingAndIsOffer(String keyword, String address, boolean isOffer, Pageable pageable);

    Page<Post> findByTitleContainingAndIsOffer(String keyword, boolean isOffer, Pageable pageable);

    Page<Post> findByAddressContainingAndIsOffer(String address, boolean isOffer, Pageable pageable);

    Page<Post> findByUser(Users user, Pageable pageable);
}
