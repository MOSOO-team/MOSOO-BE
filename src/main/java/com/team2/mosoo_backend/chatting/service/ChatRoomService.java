package com.team2.mosoo_backend.chatting.service;

import com.team2.mosoo_backend.bid.entity.Bid;
import com.team2.mosoo_backend.bid.repository.BidRepository;
import com.team2.mosoo_backend.chatting.dto.*;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
//    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final BidRepository bidRepository;
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
    public ChatRoomCreateResponseDto createChatRoom(ChatRoomRequestDto chatRoomRequestDto) {

        // TODO: 고수 정보 검증
//        userRepository.findById(chatRoomRequestDto.getGosuId())
//                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 게시글 정보 가져옴
        Post post = postRepository.findById(chatRoomRequestDto.getPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 입찰 정보 가져옴
        Bid bid = bidRepository.findById(chatRoomRequestDto.getBidId())
                .orElseThrow(() -> new CustomException(ErrorCode.BID_NOT_FOUND));

        // 해당 입찰에 대한 채팅방이 이미 존재하는 경우
        if(chatRoomRepository.existsByBidId(bid.getId())) {
            // TODO: 존재하는 채팅방으로 들어가야 함
            throw new CustomException(ErrorCode.DUPLICATE_CHAT_ROOM);
        }

        // TODO: 유저정보를 포함한 채팅방 생성
//        ChatRoom chatRoom = chatRoomMapper.toEntity(chatRoomRequestDto, getLoginUser().getId());
        ChatRoom chatRoom = chatRoomMapper.toEntity(chatRoomRequestDto, 1L);

        // 채팅방 <-> 게시글 + 입찰 연관관계 설정
        chatRoom.setMappings(post, bid);

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        return new ChatRoomCreateResponseDto(savedChatRoom.getId());
    }

    // 채팅방 나가기 메서드 - 일반유저, 고수유저 공통
    @Transactional
    public ChatRoomDeleteResponseDto quitChatRoom(Long chatRoomId) {

        // TODO: 유저 정보 가져옴
//        User loginUser = getLoginUser();

        // 채팅방 정보 가져옴
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        // TODO: 가져온 USER 정보 토대로 채팅 참여 여부 검증
//        if(chatRoom.getUserId() != user.getId() && chatRoom.getGosuId() != user.getId()) {
//            throw new CustomException(ErrorCode.USER_NOT_AUTHORIZED);
//        }

        // TODO: 이미 채팅방을 나간 이력이 있는 경우 410 에러 반환
//        if( (user.getUserRole().equals("gosu") && chatRoom.getGosuDeletedAt() != null) ||
//                (user.getUserRole().equals("user") && chatRoom.getUserDeletedAt() != null) ) {
//            throw new CustomException(ErrorCode.CHAT_ROOM_DELETED);
//        }

        // TODO: USER의 고수 여부 포함하여 채팅방 나가기
//        chatRoom.quitChatRoom((user.getUserRole().equals("gosu")));
        chatRoom.quitChatRoom(false);

        return new ChatRoomDeleteResponseDto(chatRoomId);
    }

    // 로그인 유저의 id를 가져오는 임시 메서드
    // TODO: USER 정보 가져오기 확인 + 권한 확인
//    public User getLoginUser() {
//        return userRepository.findById(2L).orElse(null);
//    }
}
