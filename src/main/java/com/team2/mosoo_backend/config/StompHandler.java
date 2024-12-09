package com.team2.mosoo_backend.config;

import com.team2.mosoo_backend.chatting.service.ChatRoomService;
import com.team2.mosoo_backend.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final ChatRoomService chatRoomService;
    private final TokenProvider tokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel messageChannel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // Authorization 헤더에서 토큰을 추출
        String authHeader = accessor.getNativeHeader("Authorization") != null ?
                accessor.getNativeHeader("Authorization").get(0) : null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 이후의 토큰 추출
            Authentication authentication = tokenProvider.getAuthentication(token);

            Long loginUserId = Long.parseLong(authentication.getName());
            // 세션에 사용자 ID 저장
            Objects.requireNonNull(accessor.getSessionAttributes()).put("loginUserId", loginUserId);
        }

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
        String userSessionId = accessor.getSessionId(); // 사용자 ID로 세션 ID 사용

        // 사용자 정보 가져오기
        Long loginUserId = (Long) accessor.getSessionAttributes().get("loginUserId");

        // 사용자 상태 저장
        chatRoomService.connectToChatRoom(loginUserId, chatRoomId, userSessionId);
    }

    private void disconnectFromChatRoom(StompHeaderAccessor accessor) {

        String userSessionId = accessor.getSessionId(); // 사용자 ID
        Long chatRoomId = chatRoomService.getUserChatRoom(userSessionId); // 사용자에 대한 chatRoomId 가져오기

        // 사용자 정보 가져오기
        if (chatRoomId != null) {
            chatRoomService.disconnectFromChatRoom(chatRoomId, userSessionId);
        }
    }

    private Long getChatRoomId(StompHeaderAccessor accessor) {
        String chatRoomId = accessor.getFirstNativeHeader("chatRoomId");

        return Long.valueOf(chatRoomId);
    }
}
