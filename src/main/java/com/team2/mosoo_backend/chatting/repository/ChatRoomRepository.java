package com.team2.mosoo_backend.chatting.repository;

import com.team2.mosoo_backend.chatting.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    boolean existsByBidIdAndUserIdAndGosuId(Long bidId, Long userId, Long gosuId);
    boolean existsByPostIdAndUserIdAndGosuId(Long postId, Long userId, Long gosuId);

    Optional<ChatRoom> findByBidIdAndGosuId(Long bidId, Long gosuId);
    Optional<ChatRoom> findByPostIdAndGosuId(Long postId, Long gosuId);

    @Query("SELECT c FROM ChatRoom c " +
                "WHERE (c.gosuId = :id AND c.gosuDeletedAt IS null) " +
                        "OR (c.userId = :id AND c.userDeletedAt IS null)")
    Page<ChatRoom> findActiveChatRoomsByUserId(Pageable pageable, @Param(value = "id") Long id);
}
