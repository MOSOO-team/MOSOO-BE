package com.team2.mosoo_backend.chatting.service;

//import com.team2.mosoo_backend.bid.entity.Bid;
//import com.team2.mosoo_backend.bid.repository.BidRepository;
import com.team2.mosoo_backend.chatting.dto.ChatRoomRequestDto;
import com.team2.mosoo_backend.chatting.dto.ChatRoomResponseDto;
import com.team2.mosoo_backend.chatting.dto.ChatRoomResponseWrapperDto;
import com.team2.mosoo_backend.chatting.entity.ChatRoom;
import com.team2.mosoo_backend.chatting.mapper.ChatRoomMapper;
import com.team2.mosoo_backend.chatting.repository.ChatRoomRepository;
import com.team2.mosoo_backend.exception.CustomException;
import com.team2.mosoo_backend.exception.ErrorCode;
import com.team2.mosoo_backend.post.entity.Post;
import com.team2.mosoo_backend.post.repository.PostRepository;
//import com.team2.mosoo_backend.user.entity.User;
//import com.team2.mosoo_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
//    private final UserRepository userRepository;
    private final PostRepository postRepository;
//    private final BidRepository bidRepository;
    private final ChatRoomMapper chatRoomMapper;

    // 채팅방 조회 메서드
    public ChatRoomResponseWrapperDto findAllChatRooms(int page) {

        // 페이지 당 채팅 10개, 최근 수정시간 기준 내림차순 정렬
        PageRequest pageRequest = PageRequest.of(page - 1, 10,
                Sort.by("updatedAt").descending());


        // TODO: USER 정보 가져오기 확인
//        User loginUser = getLoginUser();

        Page<ChatRoom> chatRooms;

        // TODO: 유저 정보를 토대로 채팅방 찾기
//        if(loginUser.getUserRole().equals("user")) {
//            chatRooms = chatRoomRepository.findChatRoomsByUserIdAndUserDeletedAt(pageRequest, loginUser.getId(), null);
//        } else {
//            chatRooms = chatRoomRepository.findChatRoomsByGosuIdAndGosuDeletedAt(pageRequest, loginUser.getId(), null);
//        }
        chatRooms = chatRoomRepository.findChatRoomsByUserIdAndUserDeletedAt(pageRequest, 1L, null);

        List<ChatRoomResponseDto> dtos = new ArrayList<>();
        
        for (ChatRoom chatRoom : chatRooms) {
            ChatRoomResponseDto dto = chatRoomMapper.toChatRoomResponseDto(chatRoom);
            dtos.add(dto);
        }

        // 총 페이지 수
        int totalPages = (chatRooms.getTotalPages()==0 ? 1 : chatRooms.getTotalPages());

        return new ChatRoomResponseWrapperDto(dtos, totalPages);
    }

    // 채팅방 생성 메서드
    @Transactional
    public Map<String, Long> createChatRoom(ChatRoomRequestDto chatRoomRequestDto) {

        // TODO: 고수 정보 검증
//        userRepository.findById(chatRoomRequestDto.getGosuId())
//                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 게시글 정보 가져옴
        Post post = postRepository.findById(chatRoomRequestDto.getPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // TODO: 해당 입찰에 대한 채팅방이 이미 존재하는 경우
//        if(chatRoomRepository.existsByBid_Id(chatRoomRequestDto.getBidId())) {
//            // TODO: 존재하는 채팅방으로 들어가야 함
//            throw new CustomException(ErrorCode.DUPLICATE_CHAT_ROOM);
//        }

        // TODO: 입찰 정보 가져옴
//        Bid bid = bidRepository.findById(chatRoomRequestDto.getBidId())
//                .orElseThrow(() -> new CustomException(ErrorCode.BID_NOT_FOUND));

        // TODO: 유저정보를 포함한 채팅방 생성
//        ChatRoom chatRoom = chatRoomMapper.toEntity(chatRoomRequestDto, getLoginUser().getId());
        ChatRoom chatRoom = chatRoomMapper.toEntity(chatRoomRequestDto, 1L);

        // TODO: 채팅방 <-> 게시글, 입찰 연관관계 설정
//        chatRoom.setMappings(post, bid);
        chatRoom.setMappings(post);

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        // Map 형식으로 리턴
        return createResponseMap("chatRoomId", savedChatRoom.getId());
    }

    // 채팅방 나가기 메서드 - 일반유저, 고수유저 공통
    @Transactional
    public Map<String, Long> quitChatRoom(Long chatRoomId) {

        // TODO: 유저 정보 가져옴
//        User user = userRepository.findById(getLoginUser().getId())
//                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 채팅방 정보 가져옴
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));

        // TODO: 가져온 USER 정보 토대로 채팅 참여 여부 검증
//        if(chatRoom.getUserId() != user.getId() && chatRoom.getGosuId() != user.getId()) {
//            throw new CustomException(ErrorCode.USER_NOT_AUTHORIZED);
//        }

        // TODO: USER의 고수 여부 포함하여 채팅방 나가기
//        chatRoom.quitChatRoom((user.getUserRole().equals("gosu")));
        chatRoom.quitChatRoom(false);

        // Map 형식으로 리턴
        return createResponseMap("chatRoomId", chatRoomId);
    }

    static Map<String, Long> createResponseMap(String key, Long chatRoomId) {

        // 프론트와 JSON으로 통신하기 위해 Map 으로 리턴
        Map<String, Long> result = new HashMap<>();
        result.put(key, chatRoomId);

        return result;
    }

    // 로그인 유저의 id를 가져오는 임시 메서드
    // TODO: USER 정보 가져오기 확인 + 권한 확인
//    public User getLoginUser() {
//        return userRepository.findById(2L).orElse(null);
//    }
}
