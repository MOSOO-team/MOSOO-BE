package com.team2.mosoo_backend.chatting.dto;

import com.team2.mosoo_backend.chatting.entity.ChatMessageType;
import lombok.*;

import java.time.LocalDateTime;

@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageRequestDto {

    private Long sourceUserId;
    private ChatMessageType type;
    private String base64File;
    private String fileName;
    private String content;
    private LocalDateTime createdAt;

    // 클라이언트에서 넘어오는 값이 아닌 서버에서 저장할 값
    private boolean checked;
}