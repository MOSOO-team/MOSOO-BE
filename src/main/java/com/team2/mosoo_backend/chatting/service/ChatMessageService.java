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
import com.team2.mosoo_backend.user.entity.User;
import com.team2.mosoo_backend.user.repository.UserRepository;
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
    private final UserRepository userRepository;

    // 채팅 저장 메서드
    @Transactional
    public void saveChatMessage(Long chatRoomId, ChatMessageRequestDto chatMessageRequestDto) {

        ChatMessage createdChatMessage = ChatMessageMapper.INSTANCE.toEntity(chatMessageRequestDto);

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));

        createdChatMessage.setChatRoom(chatRoom);

        chatMessageRepository.save(createdChatMessage);
    }

    public List<ChatMessageResponseDto> findChatMessages(Long id) {

        List<ChatMessage> chatmessageList = chatMessageRepository.findChatMessagesByChatRoom_IdOrderByCreatedAtAsc(id);

        List<ChatMessageResponseDto> result = new ArrayList<>();
        for (ChatMessage chatMessage : chatmessageList) {

            ChatMessageResponseDto dto = ChatMessageMapper.INSTANCE.toChatMessageResponseDto(chatMessage);
            User foundUser = userRepository.findById(chatMessage.getSourceUserId()).orElse(null);
            dto.setUserFullName( (foundUser != null) ? foundUser.getFullname() : "찾을 수 없는 회원");

            result.add(dto);
        }

        return result;
    }
}
