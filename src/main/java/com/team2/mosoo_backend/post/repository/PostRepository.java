package com.team2.mosoo_backend.post.repository;


import com.team2.mosoo_backend.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByIsOffer(Pageable pageable, boolean isOffer);

    Page<Post> findById(Pageable pageable, long id);
}
