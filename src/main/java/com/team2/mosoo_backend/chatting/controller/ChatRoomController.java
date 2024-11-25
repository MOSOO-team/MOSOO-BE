package com.team2.mosoo_backend.chatting.controller;

import com.team2.mosoo_backend.chatting.dto.ChatMessageResponseDto;
import com.team2.mosoo_backend.chatting.dto.ChatRoomRequestDto;
import com.team2.mosoo_backend.chatting.dto.ChatRoomResponseWrapperDto;
import com.team2.mosoo_backend.chatting.service.ChatMessageService;
import com.team2.mosoo_backend.chatting.service.ChatRoomService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    // 채팅방 전체 조회 (채팅 내역 조회)
    @GetMapping("/chatrooms")
    public ResponseEntity<ChatRoomResponseWrapperDto> findAllChatRooms(
            @RequestParam(required = false, value = "page", defaultValue = "1")
            @Positive int page) {

        ChatRoomResponseWrapperDto chatRoomResponseWrapperDto = chatRoomService.findAllChatRooms(page);
        return ResponseEntity.status(HttpStatus.OK).body(chatRoomResponseWrapperDto);
    }

    // 채팅방 단건 조회 (포함된 채팅 메세지 조회)
    @GetMapping("/chatroom/{chatRoomId}")
    public ResponseEntity<List<ChatMessageResponseDto>> findChatRoom(@PathVariable("chatRoomId") Long chatRoomId) {

        List<ChatMessageResponseDto> chatMessageResponseDtoList = chatMessageService.findChatMessages(chatRoomId);
        return ResponseEntity.status(HttpStatus.OK).body(chatMessageResponseDtoList);
    }

    @PostMapping("/chatroom")
    public ResponseEntity<Map<String, Long>> createChatRoom(@RequestBody ChatRoomRequestDto chatRoomRequestDto) {

        Map<String, Long> result = chatRoomService.createChatRoom(chatRoomRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping("/chatroom/{chatRoomId}")
    public ResponseEntity<Map<String, Long>> quitChatRoom(@PathVariable("chatRoomId") Long chatRoomId) {

        Map<String, Long> result = chatRoomService.quitChatRoom(chatRoomId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
