package com.team2.mosoo_backend.chatting.controller;

import com.team2.mosoo_backend.chatting.dto.ChatMessageRequestDto;
import com.team2.mosoo_backend.chatting.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class ChatMessageController {
    private final ChatMessageService chatMessageService;


    // 특정 채팅방에 대한 메시지 전송 및 저장
    @MessageMapping("/{chatRoomId}")
    @SendTo("/sub/{chatRoomId}") // 해당 채팅방에 구독한 클라이언트들에게 메시지를 전송
    public ResponseEntity<ChatMessageRequestDto> chat(@DestinationVariable("chatRoomId") Long chatRoomId, ChatMessageRequestDto chatMessageRequestDto) {

        // 채팅 메시지 db에 저장
        chatMessageService.saveChatMessage(chatRoomId, chatMessageRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(chatMessageRequestDto);
    }

}
