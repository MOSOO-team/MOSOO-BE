package com.team2.mosoo_backend.user.repository;

import com.team2.mosoo_backend.user.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<Users> findByFullName(String fullName);
    Optional<Users> findById(Long id);

    Page<Users> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM users WHERE is_deleted = true", nativeQuery = true)
    Page<Users> findAllByIsDeleteTrue(Pageable pageable);

}
