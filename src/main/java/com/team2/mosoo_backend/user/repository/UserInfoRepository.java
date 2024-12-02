package com.team2.mosoo_backend.user.repository;


import com.team2.mosoo_backend.user.entity.UsersInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UsersInfo, Long> {
    // Users 엔티티에 있는 userId 값을 찾아서 반환
    Optional<UsersInfo> findByUsersId(Long userId);
}
