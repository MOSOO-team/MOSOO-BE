package com.team2.mosoo_backend.chatting.service;

import com.team2.mosoo_backend.chatting.entity.ChatRoom;
import com.team2.mosoo_backend.chatting.repository.ChatRoomRepository;
import com.team2.mosoo_backend.exception.CustomException;
import com.team2.mosoo_backend.exception.ErrorCode;
import com.team2.mosoo_backend.user.entity.UserRole;
import com.team2.mosoo_backend.user.entity.Users;
import com.team2.mosoo_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatRoomUtils {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    // 로그인 유저, 채팅방 id 를 통해서 채팅방에 접근할 수 있는 지 판단하는 메서드
    public ChatRoom validateChatRoomOwnership(Long chatRoomId, Users loginUser) {

        // 채팅방이 존재하지 않으면 404 에러 반환
        ChatRoom foundChatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        // 유저 정보가 일치하지 않으면 403 에러 반환
        if(!foundChatRoom.getUserId().equals(loginUser.getId())
                && !foundChatRoom.getGosuId().equals(loginUser.getId())) {
            throw new CustomException(ErrorCode.USER_NOT_AUTHORIZED);
        }

        return foundChatRoom;
    }

    public String getOpponentFullName(ChatRoom chatRoom, Users loginUser) {

        // 채팅 상대 이름 저장
        String opponentFullName;
        // 로그인한 유저가 고수인 경우 (상대방이 일반 유저인 경우)
        if(loginUser.getId().equals(chatRoom.getGosuId())) {
            Users user = userRepository.findById(chatRoom.getUserId()).orElse(null);
            opponentFullName = ( (user != null) ? user.getFullname() : "찾을 수 없는 유저");
        } else {    // 로그인한 유저가 일반 유저인 경우 (상대방이 고수인 경우)
            Users gosu = userRepository.findById(chatRoom.getGosuId()).orElse(null);
            opponentFullName = ( (gosu != null) ? gosu.getFullname() : "찾을 수 없는 고수");
        }

        return opponentFullName;
    }
}
