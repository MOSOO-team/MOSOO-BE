package com.team2.mosoo_backend.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatRoomOpponentInfoResponseDto {

    // TODO: 상대방의 정보 (dto)로 변경
    private String opponentFullName;    // 채팅 참여하는 상대방 이름
}
