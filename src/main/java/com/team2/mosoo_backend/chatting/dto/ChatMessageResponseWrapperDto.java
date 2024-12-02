package com.team2.mosoo_backend.chatting.dto;

import com.team2.mosoo_backend.bid.dto.BidResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponseWrapperDto {

    private String opponentFullName;    // 채팅 참여하는 상대방 이름

    private Long postId;        // 파생된 게시글 id
    private String postTitle;   // 파생된 게시글 제목

    private int price;

    private List<ChatMessageResponseDto> chatMessageResponseDtoList;

    private int totalCount;
}
