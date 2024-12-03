package com.team2.mosoo_backend.chatting.dto;

import com.team2.mosoo_backend.chatting.entity.ChatMessageType;
import lombok.*;

@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageRequestDto {

    private Long sourceUserId;
    private ChatMessageType type;
    private String base64File;
    private String fileName;
    private String content;
}