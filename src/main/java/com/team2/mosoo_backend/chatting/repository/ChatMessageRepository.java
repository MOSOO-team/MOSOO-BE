package com.team2.mosoo_backend.chatting.repository;

import com.team2.mosoo_backend.chatting.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findChatMessagesByChatRoom_IdOrderByCreatedAtAsc(Long chatRoomId);
}
