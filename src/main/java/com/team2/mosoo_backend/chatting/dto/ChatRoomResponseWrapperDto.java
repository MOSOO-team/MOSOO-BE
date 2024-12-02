package com.team2.mosoo_backend.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChatRoomResponseWrapperDto {

    private String responseMessage;
    private List<ChatRoomResponseDto> chatRoomResponseDtoList;

    private int totalPages;
}
