package com.team2.mosoo_backend.chatting.controller;

import com.team2.mosoo_backend.chatting.dto.ChatMessageResponseDto;
import com.team2.mosoo_backend.chatting.dto.ChatRoomRequestDto;
import com.team2.mosoo_backend.chatting.dto.ChatRoomResponseWrapperDto;
import com.team2.mosoo_backend.chatting.service.ChatMessageService;
import com.team2.mosoo_backend.chatting.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    // 채팅방 전체 조회 (채팅 내역 조회)
    @GetMapping("/chatrooms")
    public ResponseEntity<ChatRoomResponseWrapperDto> findAllChatRooms(
            @RequestParam(required = false, value = "page", defaultValue = "1") int page) {

        ChatRoomResponseWrapperDto chatRoomResponseWrapperDto = chatRoomService.findAllChatRooms(page);
        return ResponseEntity.status(HttpStatus.OK).body(chatRoomResponseWrapperDto);
    }

    // 채팅방 단건 조회 (포함된 채팅 메세지 조회)
    @GetMapping("/chatroom/{id}")
    public ResponseEntity<List<ChatMessageResponseDto>> findChatRoom(@PathVariable("id") Long id) {

        List<ChatMessageResponseDto> chatMessageResponseDtoList = chatMessageService.findChatMessages(id);
        return ResponseEntity.status(HttpStatus.OK).body(chatMessageResponseDtoList);
    }

    @PostMapping("/chatroom")
    public ResponseEntity<Long> createChatRoom(@RequestBody ChatRoomRequestDto chatRoomRequestDto) {

        Long createdChatRoomId = chatRoomService.createChatRoom(chatRoomRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChatRoomId);
    }

    @DeleteMapping("/chatroom/{id}")
    public ResponseEntity<Long> quitChatRoom(@PathVariable("id") Long id) {

        Long deletedChatRoomId = chatRoomService.quitChatRoom(id);
        return ResponseEntity.status(HttpStatus.OK).body(deletedChatRoomId);
    }

}
