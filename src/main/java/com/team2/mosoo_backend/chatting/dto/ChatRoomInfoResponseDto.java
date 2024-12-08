package com.team2.mosoo_backend.chatting.dto;

import com.team2.mosoo_backend.bid.dto.BidResponseDto;
import com.team2.mosoo_backend.post.dto.PostResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatRoomInfoResponseDto {

    private PostResponseDto postResponseDto; // 파생된 게시글 정보

    private BidResponseDto bidResponseDto;  // 파생된 입찰 정보 (존재한다면)

    private boolean isGosu;    // 고수 여부 (가격 수정은 고수만 가능)

    // 위의 dto에는 게시글의 가격이 포함되므로 채팅방의 가격 필드 따로 생성
    private int price;
}
