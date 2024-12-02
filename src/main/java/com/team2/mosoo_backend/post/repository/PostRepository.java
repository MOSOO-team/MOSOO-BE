package com.team2.mosoo_backend.post.repository;


import com.team2.mosoo_backend.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByIsOffer(boolean isOffer);

}
