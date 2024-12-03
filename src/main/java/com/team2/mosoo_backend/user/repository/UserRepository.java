package com.team2.mosoo_backend.user.repository;

import com.team2.mosoo_backend.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByFullName(String fullName);
    Optional<User> findById(Long id);

    Page<User> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM member WHERE is_deleted = true", nativeQuery = true)
    Page<User> findAllByIsDeleteTrue(Pageable pageable);

}
