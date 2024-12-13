package com.team2.mosoo_backend.chatting.repository;

import com.team2.mosoo_backend.chatting.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 성능 개선 전 기존 페이징 메서드
    Page<ChatMessage> findChatMessagesByChatRoomId(Pageable pageable, Long chatRoomId);

    // chatRoomId가 동일하고 createdAt이 가장 최근인 ChatMessage 반환
    Optional<ChatMessage> findTopByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);

    boolean existsByChatRoomIdAndSourceUserIdNotAndCheckedIs(Long chatRoomId, Long sourceUserId, boolean checked);

    List<ChatMessage> findChatMessagesByChatRoomIdAndSourceUserIdNotAndCheckedIs(Long chatRoomId, Long sourceUserId, boolean checked);
}
