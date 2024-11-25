package com.team2.mosoo_backend.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatRoomResponseDto {

    // 일단은 id -> 추후에 유저 정보 직접 포함으로 변경 고민 (프론트에서 api 호출을 한 번 더 해야하므로)
    private Long userId;
    private Long gosuId;

    private Long postId;    // 게시글 id
    private Long bidId;     // 입찰 id
}
