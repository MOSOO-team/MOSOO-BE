package com.team2.mosoo_backend.chatting.controller;

import com.team2.mosoo_backend.chatting.dto.ChatMessageRequestDto;
import com.team2.mosoo_backend.chatting.dto.ChatMessageResponseWrapperDto;
import com.team2.mosoo_backend.chatting.service.ChatMessageService;
import com.team2.mosoo_backend.config.swagger.ApiExceptionResponseExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.team2.mosoo_backend.exception.ErrorCode.CHAT_ROOM_NOT_FOUND;
import static com.team2.mosoo_backend.exception.ErrorCode.USER_NOT_AUTHORIZED;


@RequiredArgsConstructor
@RestController
@Validated
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

    // 기존 채팅 메시지 조회
//    @GetMapping("/api/chatroom/{chatRoomId}")
//    @Operation(summary = "채팅 메시지 목록 조회", description = "채팅 메시지 목록 조회")
//    @ApiExceptionResponseExamples({USER_NOT_AUTHORIZED, CHAT_ROOM_NOT_FOUND})
//    /*
//        403 에러 : 유저 정보가 일치하지 않는 경우
//        404 에러 : 채팅방을 찾을 수 없는 경우
//    */
//    @ApiResponse(responseCode = "200", description = "채팅 내역 조회 성공",
//            content = @Content(mediaType = "application/json",
//                    schema = @Schema(implementation = ChatMessageResponseWrapperDto.class)))
//    public ResponseEntity<ChatMessageResponseWrapperDto> findChatRoom(@PathVariable("chatRoomId") Long chatRoomId,
//                                                                      @RequestParam(required = false, value = "offset", defaultValue = "0")
//                                                                      @PositiveOrZero int offset) {
//
//        ChatMessageResponseWrapperDto chatMessageResponseWrapperDto = chatMessageService.findChatMessages(chatRoomId, offset);
//        return ResponseEntity.status(HttpStatus.OK).body(chatMessageResponseWrapperDto);
//    }

    // 성능 개선한 채팅 메시지 조회
    @GetMapping("/api/chatroom/{chatRoomId}")
    @Operation(summary = "채팅 메시지 목록 조회", description = "채팅 메시지 목록 조회")
    @ApiExceptionResponseExamples({USER_NOT_AUTHORIZED, CHAT_ROOM_NOT_FOUND})
    /*
        403 에러 : 유저 정보가 일치하지 않는 경우
        404 에러 : 채팅방을 찾을 수 없는 경우
    */
    @ApiResponse(responseCode = "200", description = "채팅 내역 조회 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ChatMessageResponseWrapperDto.class)))
    public ResponseEntity<ChatMessageResponseWrapperDto> findChatRoom(
            @PathVariable("chatRoomId") Long chatRoomId,
            @RequestParam(value = "index", required = false) Long index) {

        ChatMessageResponseWrapperDto chatMessageResponseWrapperDto = chatMessageService.findChatMessages(chatRoomId, index);
        return ResponseEntity.status(HttpStatus.OK).body(chatMessageResponseWrapperDto);
    }
}
