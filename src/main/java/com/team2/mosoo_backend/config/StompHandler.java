package com.team2.mosoo_backend.config;

import com.team2.mosoo_backend.chatting.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final ChatRoomService chatRoomService;

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
        String userSessionId = accessor.getSessionId(); // 사용자 ID로 세션 ID 사용

        // 사용자 정보 가져오기
        Object principal = Objects.requireNonNull(accessor.getSessionAttributes()).get("SPRING_SECURITY_CONTEXT");
        if (principal != null) {
            // SecurityContext에서 UserDetails를 가져옴
            UserDetails userDetails = (UserDetails) ((SecurityContext) principal).getAuthentication().getPrincipal();
            Long loginUserId = Long.parseLong(userDetails.getUsername());

            // 사용자 상태 저장
            chatRoomService.connectToChatRoom(loginUserId, chatRoomId, userSessionId);
        }
    }

    private void disconnectFromChatRoom(StompHeaderAccessor accessor) {

        String userSessionId = accessor.getSessionId(); // 사용자 ID
        Long chatRoomId = chatRoomService.getUserChatRoom(userSessionId); // 사용자에 대한 chatRoomId 가져오기

        // 사용자 정보 가져오기
        Object principal = Objects.requireNonNull(accessor.getSessionAttributes()).get("SPRING_SECURITY_CONTEXT");
        if (chatRoomId != null && principal != null) {

            chatRoomService.disconnectFromChatRoom(chatRoomId, userSessionId);
        }
    }

    private Long getChatRoomId(StompHeaderAccessor accessor) {
        String chatRoomId = accessor.getFirstNativeHeader("chatRoomId");

        return Long.valueOf(chatRoomId);
    }
}
