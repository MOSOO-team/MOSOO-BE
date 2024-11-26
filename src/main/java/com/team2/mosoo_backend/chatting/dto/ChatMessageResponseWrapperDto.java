package com.team2.mosoo_backend.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponseWrapperDto {

    private List<ChatMessageResponseDto> chatMessageResponseDtoList;

    private int totalCount;
}
