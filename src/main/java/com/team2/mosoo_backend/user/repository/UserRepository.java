package com.team2.mosoo_backend.user.repository;

import com.team2.mosoo_backend.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByEmail(String email);
    boolean existsByUsername(String username);
    Optional<Users> findByUsername(String username);
}
