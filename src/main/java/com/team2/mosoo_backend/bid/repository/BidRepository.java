package com.team2.mosoo_backend.bid.repository;


import com.team2.mosoo_backend.bid.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findAllByPostId(Long postId);
}
