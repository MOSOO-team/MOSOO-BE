package com.team2.mosoo_backend.chatting.repository;

import com.team2.mosoo_backend.chatting.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Page<ChatRoom> findChatRoomsByUserIdAndUserDeletedAt(Pageable pageable, Long userId, LocalDateTime userDeletedAt);
    Page<ChatRoom> findChatRoomsByGosuIdAndGosuDeletedAt(Pageable pageable, Long gosuId, LocalDateTime gosuDeletedAt);

    boolean existsByBid_Id(Long bidId);
}
