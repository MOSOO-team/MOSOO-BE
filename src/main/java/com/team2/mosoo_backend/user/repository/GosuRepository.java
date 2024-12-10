package com.team2.mosoo_backend.user.repository;

import com.team2.mosoo_backend.user.entity.Gosu;
import com.team2.mosoo_backend.user.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GosuRepository extends JpaRepository<Gosu, Long> {

    //특정 유저 정보 ID로 고수 정보 조회
    Optional<Gosu> findByUserInfoId(Long userInfoId);


    // 고수 전체 조회
    Page<Gosu> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM users WHERE is_deleted = true", nativeQuery = true)
    Page<Users> findAllByIsDeleteTrue(Pageable pageable);
}
