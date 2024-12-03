package com.team2.mosoo_backend.config;

import com.team2.mosoo_backend.chatting.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final RedisService redisService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel messageChannel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        handleMessage(accessor.getCommand(), accessor);
        return message;
    }

    private void handleMessage(StompCommand stompCommand, StompHeaderAccessor accessor) {

        switch (stompCommand) {
            case CONNECT:
                connectToChatRoom(accessor);
                break;
            case DISCONNECT:
                disconnectFromChatRoom(accessor);
                break;
            default:
                break;
        }
    }

    private void connectToChatRoom(StompHeaderAccessor accessor) {

        Long chatRoomId = getChatRoomId(accessor);
        String userId = accessor.getSessionId(); // 사용자 ID로 세션 ID 사용

        // 사용자 상태 저장
        redisService.saveUserChatRoom(userId, chatRoomId);
        redisService.connectToChatRoom(chatRoomId);
    }

    private void disconnectFromChatRoom(StompHeaderAccessor accessor) {

        String userId = accessor.getSessionId(); // 사용자 ID
        Long chatRoomId = redisService.getUserChatRoom(userId); // 사용자에 대한 chatRoomId 가져오기

        if (chatRoomId != null) {
            redisService.disconnectFromChatRoom(chatRoomId);
            redisService.removeUserChatRoom(userId); // 사용자 ID로 저장된 채팅방 ID 삭제
        }
    }

    private Long getChatRoomId(StompHeaderAccessor accessor) {
        String chatRoomId = accessor.getFirstNativeHeader("chatRoomId");

        return Long.valueOf(chatRoomId);
    }
}
