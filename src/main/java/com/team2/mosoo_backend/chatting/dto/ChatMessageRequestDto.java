package com.team2.mosoo_backend.chatting.dto;

import lombok.*;


@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageRequestDto {

    private Long sourceUserId;
    private String content;
}