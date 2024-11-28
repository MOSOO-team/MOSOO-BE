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

    private String opponentFullName;

    private BidResponseDto bidResponseDto;  // 입찰 정보 포함

    private List<ChatMessageResponseDto> chatMessageResponseDtoList;

    private int totalCount;
}
