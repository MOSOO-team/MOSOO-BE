package com.team2.mosoo_backend.chatting.dto;

import com.team2.mosoo_backend.chatting.entity.ChatMessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponseDto {

    private Long sourceUserId;
    private String content;
    private String fileName;
    private LocalDateTime createdAt;
    private ChatMessageType type;
}
