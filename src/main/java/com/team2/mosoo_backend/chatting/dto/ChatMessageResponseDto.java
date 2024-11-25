package com.team2.mosoo_backend.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponseDto {

    private String userFullName;
    private String content;
    private LocalDateTime createdAt;
}
