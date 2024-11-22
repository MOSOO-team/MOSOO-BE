package com.team2.mosoo_backend.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatRoomRequestDto {

    private Long gosuId;

    private Long postId;
    private Long bidId;
}
