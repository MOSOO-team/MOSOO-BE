package com.team2.mosoo_backend.chatting.service;

import com.team2.mosoo_backend.chatting.dto.ChatMessageRequestDto;
import com.team2.mosoo_backend.chatting.dto.ChatMessageResponseDto;
import com.team2.mosoo_backend.chatting.entity.ChatMessage;
import com.team2.mosoo_backend.chatting.entity.ChatRoom;
import com.team2.mosoo_backend.chatting.mapper.ChatMessageMapper;
import com.team2.mosoo_backend.chatting.repository.ChatMessageRepository;
import com.team2.mosoo_backend.chatting.repository.ChatRoomRepository;
import com.team2.mosoo_backend.exception.CustomException;
import com.team2.mosoo_backend.exception.ErrorCode;
//import com.team2.mosoo_backend.user.entity.User;
//import com.team2.mosoo_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageMapper chatMessageMapper;
//    private final UserRepository userRepository;

    // 채팅 저장 메서드
    @Transactional
    public void saveChatMessage(Long chatRoomId, ChatMessageRequestDto chatMessageRequestDto) {

        ChatMessage createdChatMessage = chatMessageMapper.toEntity(chatMessageRequestDto);

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));

        createdChatMessage.setChatRoom(chatRoom);

        chatMessageRepository.save(createdChatMessage);
    }

    // 채팅 내역 조회 메서드
    public List<ChatMessageResponseDto> findChatMessages(Long chatRoomId) {

        List<ChatMessage> chatmessageList = chatMessageRepository.findChatMessagesByChatRoomIdOrderByCreatedAtAsc(chatRoomId);

        List<ChatMessageResponseDto> result = new ArrayList<>();
        for (ChatMessage chatMessage : chatmessageList) {

            ChatMessageResponseDto dto = chatMessageMapper.toChatMessageResponseDto(chatMessage);
            // TODO: User 정보를 가져와서 이름 포함
//            User foundUser = userRepository.findById(chatMessage.getSourceUserId()).orElse(null);
//            dto.setUserFullName( (foundUser != null) ? foundUser.getFullname() : "찾을 수 없는 회원");
            dto.setUserFullName("유저이름");

            result.add(dto);
        }

        return result;
    }
}
