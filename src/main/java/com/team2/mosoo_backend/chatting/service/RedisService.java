package com.team2.mosoo_backend.chatting.service;

import com.team2.mosoo_backend.chatting.dto.ChatMessageRequestDto;
import com.team2.mosoo_backend.chatting.entity.ChatRoomConnection;
import com.team2.mosoo_backend.chatting.repository.ChatRoomConnectionRepository;
import com.team2.mosoo_backend.exception.CustomException;
import com.team2.mosoo_backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatRoomConnectionRepository chatRoomConnectionRepository;
    private final ChatMessageService chatMessageService;

    // 채팅방 연결 시 메서드
    public void connectToChatRoom(Long chatRoomId) {

        String chatRoomIdString = String.valueOf(chatRoomId);

        // ChatRoomConnection 엔티티를 가져오거나 새로 생성
        ChatRoomConnection connection = chatRoomConnectionRepository.findById(chatRoomIdString)
                .orElse(ChatRoomConnection.builder()
                        .id(chatRoomIdString)
                        .connectionCount(0) // 초기 연결 수 0
                        .build());

        // 연결 수 증가
        connection.incrementConnectionCount();

        // 레디스에 연결 정보 저장
        chatRoomConnectionRepository.save(connection);
    }

    // 채팅방 연결 해제 시 메서드
    public void disconnectFromChatRoom(Long chatRoomId) {

        String chatRoomIdString = String.valueOf(chatRoomId);

        // ChatRoomConnection 엔티티를 가져오거나 새로 생성
        ChatRoomConnection connection = chatRoomConnectionRepository.findById(chatRoomIdString).orElseThrow();

        // 연결 수 감소
        connection.decrementConnectionCount();

        // 레디스에 연결 정보 저장
        chatRoomConnectionRepository.save(connection);

        // 채팅방에 접속중인 유저가 0명이라면 레디스의 채팅 메세지를 db로 저장
        if(connection.getConnectionCount() == 0) {
            chatMessageService.saveChatMessages(chatRoomId);

            // Redis 키 삭제
            String redisKey = "chatRoom:" + chatRoomId + ":messages";
            redisTemplate.delete(redisKey); // redisTemplate을 사용하여 Redis에서 키 삭제

            // 채팅방 연결 정보 키 삭제
            redisTemplate.delete("chatRoomConnection:" + chatRoomId);
        }
    }

    public void saveUserChatRoom(String userId, Long chatRoomId) {
        // 사용자 ID를 키로 하고, 채팅방 ID를 값으로 저장
        redisTemplate.opsForValue().set(userId, chatRoomId);
    }

    public void removeUserChatRoom(String userId) {
        redisTemplate.delete(userId); // 사용자 ID로 저장된 채팅방 ID 삭제
    }

    // 사용자 ID로 채팅방 ID를 가져오는 메서드
    public Long getUserChatRoom(String userId) {
        Object value = redisTemplate.opsForValue().get(userId);
        if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof Integer) {
            return ((Integer) value).longValue(); // Integer를 Long으로 변환
        }
        return null; // null 처리
    }

    public Integer getConnectionCount(Long chatRoomId) {
        String chatRoomIdString = String.valueOf(chatRoomId);

        // ChatRoomConnection 엔티티를 가져옴
        ChatRoomConnection connection = chatRoomConnectionRepository.findById(chatRoomIdString)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND)); // 채팅방이 없을 경우 예외 처리

        // 연결 수 반환
        return connection.getConnectionCount();
    }

    public void saveChatMessageToSortedSet(Long chatRoomId, ChatMessageRequestDto chatMessageRequestDto) {

        String redisKey = "chatRoom:" + chatRoomId + ":messages";

        // Redis에 메시지 저장
        if(chatMessageRequestDto.getBase64File() != null) {
            chatMessageRequestDto = chatMessageService.convertToMultipartFile(chatMessageRequestDto);
        }
        redisTemplate.opsForList().leftPush(redisKey, chatMessageRequestDto); // 메시지 추가
    }

}