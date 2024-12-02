package com.team2.mosoo_backend.chatting.service;

import com.team2.mosoo_backend.bid.entity.Bid;
import com.team2.mosoo_backend.bid.repository.BidRepository;
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
import com.team2.mosoo_backend.user.entity.User;
import com.team2.mosoo_backend.user.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final BidRepository bidRepository;

    // 채팅방 조회 메서드
    public ChatRoomResponseWrapperDto findAllChatRooms(int page) {

        // 페이지 당 채팅 10개, 최근 수정시간 기준 내림차순 정렬
        PageRequest pageRequest = PageRequest.of(page - 1, 10,
                Sort.by("updatedAt").descending());


        // TODO: USER 정보 가져오기 확인
        User loginUser = getLoginUser();

        Page<ChatRoom> chatRooms;
        if(loginUser.getUserRole().equals("user")) {
            chatRooms = chatRoomRepository.findChatRoomsByUserIdAndUserDeletedAt(pageRequest, loginUser.getId(), null);
        } else {
            chatRooms = chatRoomRepository.findChatRoomsByGosuIdAndGosuDeletedAt(pageRequest, loginUser.getId(), null);
        }

        List<ChatRoomResponseDto> dtos = new ArrayList<>();
        
        for (ChatRoom chatRoom : chatRooms) {
            ChatRoomResponseDto dto = ChatRoomMapper.INSTANCE.toChatRoomResponseDto(chatRoom);
            dtos.add(dto);
        }

        // 총 페이지 수
        int totalPages = (chatRooms.getTotalPages()==0 ? 1 : chatRooms.getTotalPages());

        return new ChatRoomResponseWrapperDto("채팅방 조회 완료했습니다.", dtos, totalPages);
    }

    // 채팅방 생성 메서드
    @Transactional
    public Long createChatRoom(ChatRoomRequestDto chatRoomRequestDto) {

        // 고수 정보 검증
        userRepository.findById(chatRoomRequestDto.getGosuId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 게시글 정보 가져옴
        Post post = postRepository.findById(chatRoomRequestDto.getPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 해당 입찰에 대한 채팅방이 이미 존재하는 경우
        if(chatRoomRepository.existsByBid_Id(chatRoomRequestDto.getBidId())) {
            // TODO: 존재하는 채팅방으로 들어가야 함
            throw new CustomException(ErrorCode.DUPLICATE_CHAT_ROOM);
        }

        // 입찰 정보 가져옴
        Bid bid = bidRepository.findById(chatRoomRequestDto.getBidId())
                .orElseThrow(() -> new CustomException(ErrorCode.BID_NOT_FOUND));

        ChatRoom chatRoom = ChatRoomMapper.INSTANCE.toEntity(chatRoomRequestDto, getLoginUser().getId());
        chatRoom.setMappings(post, bid);

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        return savedChatRoom.getId();
    }

    // 채팅방 나가기 메서드 - 일반유저, 고수유저 공통
    @Transactional
    public Long quitChatRoom(Long id) {

        // 유저 정보 가져옴
        User user = userRepository.findById(getLoginUser().getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 채팅방 정보 가져옴
        ChatRoom chatRoom = chatRoomRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));

        // TODO: USER 정보 가져오기 확인
        chatRoom.quitChatRoom((user.getUserRole().equals("gosu")));

        return chatRoom.getId();
    }


    // 로그인 유저의 id를 가져오는 임시 메서드
    // TODO: USER 정보 가져오기 확인 + 권한 확인
    public User getLoginUser() {
        return userRepository.findById(2L).orElse(null);
    }
}
