package com.team2.mosoo_backend.chatting.service;

import com.team2.mosoo_backend.bid.dto.BidResponseDto;
import com.team2.mosoo_backend.bid.entity.Bid;
import com.team2.mosoo_backend.bid.mapper.BidMapper;
import com.team2.mosoo_backend.bid.repository.BidRepository;
import com.team2.mosoo_backend.chatting.dto.*;
import com.team2.mosoo_backend.chatting.entity.ChatMessage;
import com.team2.mosoo_backend.chatting.entity.ChatMessageType;
import com.team2.mosoo_backend.chatting.entity.ChatRoom;
import com.team2.mosoo_backend.chatting.mapper.ChatMessageMapper;
import com.team2.mosoo_backend.chatting.mapper.ChatRoomMapper;
import com.team2.mosoo_backend.chatting.repository.ChatMessageRepository;
import com.team2.mosoo_backend.chatting.repository.ChatRoomRepository;
import com.team2.mosoo_backend.config.SecurityUtil;
import com.team2.mosoo_backend.exception.CustomException;
import com.team2.mosoo_backend.exception.ErrorCode;
import com.team2.mosoo_backend.post.dto.PostResponseDto;
import com.team2.mosoo_backend.post.entity.Post;
import com.team2.mosoo_backend.post.mapper.PostMapper;
import com.team2.mosoo_backend.post.repository.PostRepository;
import com.team2.mosoo_backend.user.entity.Authority;
import com.team2.mosoo_backend.user.entity.Users;
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
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final BidRepository bidRepository;
    private final ChatRoomMapper chatRoomMapper;
    private final PostMapper postMapper;
    private final BidMapper bidMapper;
    private final ChatRoomUtils chatRoomUtils;
    private final ChatMessageMapper chatMessageMapper;
    private final SecurityUtil securityUtil;
    private final ChatMessageService chatMessageService;

    // 채팅방 목록 조회 메서드
    public ChatRoomResponseWrapperDto findAllChatRooms(int page) {

        // 페이지 당 채팅 10개, 최근 수정시간 기준 내림차순 정렬
        PageRequest pageRequest = PageRequest.of(page - 1, 10,
                Sort.by("updatedAt").descending());

        // 로그인 유저 정보 가져옴
        Long loginUserId = getAuthenticatedMemberId();
        Users loginUser = userRepository.findById(loginUserId).get();   // getAuthenticatedMemberId() 호출 시 예외 처리 완료

        Page<ChatRoom> chatRooms;

        // 로그인 한 유저의 id가 채팅방의 고수 id 이거나 일반유저 id 인 채팅방 (즉, 참여하는 채팅방) 조회
        chatRooms = chatRoomRepository.findActiveChatRoomsByUserId(pageRequest, loginUser.getId());

        List<ChatRoomResponseDto> result = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {
            ChatRoomResponseDto dto = chatRoomMapper.toChatRoomResponseDto(chatRoom);

            // 로그인한 유저가 고수인 경우 (상대방이 일반 유저인 경우)
            dto.setOpponentFullName(chatRoomUtils.getOpponentFullName(chatRoom, loginUser));

            ChatMessage chatMessage;

            List<ChatMessageRequestDto> chatMessagesFromRedis = chatMessageService.findChatMessagesFromRedis(chatRoom.getId(), true);
            if(chatMessagesFromRedis.size() > 0) {  // 최신 채팅 메세지가 레디스에 존재할 때
                 chatMessage = chatMessageMapper.toEntity(chatMessagesFromRedis.get(0));
            } else {                                // 최신 채팅 메세지가 db에 존재할 때
                chatMessage = chatMessageRepository.findTopByChatRoomIdOrderByCreatedAtDesc(chatRoom.getId())
                        .orElse(null);
            }

            if(chatMessage.getType() == ChatMessageType.IMAGE) { // 메시지 타입이 이미지 인 경우: 가장 최근 채팅을 "이미지"로 설정
                dto.setLastChatMessage("이미지");
            } else if(chatMessage.getType() == ChatMessageType.VIDEO) { // 메시지 타입이 동영상 인 경우: 가장 최근 채팅을 "동영상"으로 설정
                dto.setLastChatMessage("동영상");
            } else if(chatMessage.getType() == ChatMessageType.FILE) { // 메시지 타입이 파일 인 경우: 가장 최근 채팅을 "파일"로 설정
                dto.setLastChatMessage("파일");
            } else {    // 메시지 타입이 메시지 인 경우: 가장 최근 채팅을 메시지 내용으로 설정
                dto.setLastChatMessage(chatMessage.getContent());
            }

            // 가장 마지막 채팅 시간 설정
            dto.setLastChatDate(chatMessage.getCreatedAt());

            result.add(dto);

        }

        // 총 페이지 수
        int totalPages = (chatRooms.getTotalPages()==0 ? 1 : chatRooms.getTotalPages());

        return new ChatRoomResponseWrapperDto(result, totalPages);
    }

    // 특정 채팅방 관련 세부 정보 조회 메서드
    public ChatRoomInfoResponseDto findChatRoomInfo(Long chatRoomId) {

        // 로그인 유저 정보 가져옴
        Long loginUserId = getAuthenticatedMemberId();
        Users loginUser = userRepository.findById(loginUserId).get();   // getAuthenticatedMemberId() 호출 시 예외 처리 완료

        // 채팅방 접근 권한 검증 후 찾은 채팅방 받아옴
        ChatRoom foundChatRoom = chatRoomUtils.validateChatRoomOwnership(chatRoomId, loginUser);

        // 고수 여부 저장
        boolean isGosu = (loginUser.getId().equals(foundChatRoom.getGosuId()));

        // 게시글 정보
        PostResponseDto postResponseDto = postMapper.postToPostResponseDto(foundChatRoom.getPost());

        // 입찰 정보가 있다면 입찰 정보
        BidResponseDto bidResponseDto = null;
        if(foundChatRoom.getBid() != null) {
            bidResponseDto = bidMapper.bidToBidResponseDto(foundChatRoom.getBid());
        }

        return new ChatRoomInfoResponseDto(postResponseDto, bidResponseDto,
                isGosu, foundChatRoom.getPrice());
    }

    // 특정 채팅방 관련 상대 정보 조회 메서드
    public ChatRoomOpponentInfoResponseDto findChatRoomOpponentInfo(Long chatRoomId) {

        // 로그인 유저 정보 가져옴
        Long loginUserId = getAuthenticatedMemberId();
        Users loginUser = userRepository.findById(loginUserId).get();   // getAuthenticatedMemberId() 호출 시 예외 처리 완료

        // 채팅방 접근 권한 검증 후 찾은 채팅방 받아옴
        ChatRoom foundChatRoom = chatRoomUtils.validateChatRoomOwnership(chatRoomId, loginUser);

        // 채팅 상대 이름 저장
        String opponentFullName = chatRoomUtils.getOpponentFullName(foundChatRoom, loginUser);

        return new ChatRoomOpponentInfoResponseDto(opponentFullName);
    }

    // 채팅방 생성 메서드
    @Transactional
    public ChatRoomCreateResponseDto createChatRoom(ChatRoomRequestDto chatRoomRequestDto) {

        // 로그인 유저 정보 가져옴
        Long loginUserId = getAuthenticatedMemberId();
        Users loginUser = userRepository.findById(loginUserId).get();   // getAuthenticatedMemberId() 호출 시 예외 처리 완료

        // 고수 정보 검증, 고수 유저가 존재하지 않으면 404 에러 반환
        Users gosu = userRepository.findById(chatRoomRequestDto.getGosuId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 게시글 정보 가져옴, 존재하지 않으면 404 에러 반환
        Post post = postRepository.findById(chatRoomRequestDto.getPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        Bid bid = null;
        if(post.isOffer()) {    // 고수가 작성한 게시글인 경우 (게시글로부터 채팅방이 파생되어야 함)

            // 해당 게시글에 대한 채팅방이 이미 존재하는 경우
            if(chatRoomRepository.existsByPostIdAndUserIdAndGosuId(post.getId(), loginUserId, chatRoomRequestDto.getGosuId())) {
                ChatRoom existChatRoom = chatRoomRepository.findByPostIdAndGosuId(post.getId(), chatRoomRequestDto.getGosuId()).get();
                existChatRoom.reCreate();
                return new ChatRoomCreateResponseDto(existChatRoom.getId());
            }
        } else { // 유저가 작성한 게시글인 경우 (입찰로부터 채팅방이 파생되어야 함)

            // 입찰 정보 가져옴, 존재하지 않으면 404 에러 반환
            bid = bidRepository.findById(chatRoomRequestDto.getBidId())
                    .orElseThrow(() -> new CustomException(ErrorCode.BID_NOT_FOUND));

            // 해당 입찰에 대한 채팅방이 이미 존재하는 경우
            if(chatRoomRepository.existsByBidIdAndUserIdAndGosuId(bid.getId(), loginUserId, chatRoomRequestDto.getGosuId())) {
                ChatRoom existChatRoom = chatRoomRepository.findByBidIdAndGosuId(bid.getId(), chatRoomRequestDto.getGosuId()).get();
                existChatRoom.reCreate();
                return new ChatRoomCreateResponseDto(existChatRoom.getId());
            }
        }

        // 유저정보를 포함한 채팅방 생성
        ChatRoom chatRoom = chatRoomMapper.toEntity(chatRoomRequestDto, loginUserId);

        // 채팅방 <-> 게시글 + 입찰 (필요한 경우만) 연관관계 설정
        chatRoom.setPost(post);
        if(bid != null) {   // 입찰 정보 존재하는 경우
            chatRoom.setBid(bid);
        }

        // 채팅방에 초기 가격 설정
        chatRoom.setPrice( (bid != null) ? bid.getPrice() : post.getPrice());

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        // 채팅방 입장 메세지 생성
        ChatMessage userEnterChatMessage = chatMessageMapper.toEntity(ChatMessageRequestDto.builder()
                .sourceUserId(loginUserId).type(ChatMessageType.ENTER).content(loginUser.getFullName() + " 님이 입장했습니다.").build());
        ChatMessage gosuEnterChatMessage = chatMessageMapper.toEntity(ChatMessageRequestDto.builder()
                .sourceUserId(chatRoom.getGosuId()).type(ChatMessageType.ENTER).content(gosu.getFullName() + " 님이 입장했습니다.").build());

        userEnterChatMessage.setChatRoom(savedChatRoom);
        gosuEnterChatMessage.setChatRoom(savedChatRoom);

        // 채팅방 입장 메세지 저장
        chatMessageRepository.save(userEnterChatMessage);
        chatMessageRepository.save(gosuEnterChatMessage);

        return new ChatRoomCreateResponseDto(savedChatRoom.getId());
    }

    // 채팅방 나가기 메서드 - 일반유저, 고수유저 공통
    @Transactional
    public ChatRoomDeleteResponseDto quitChatRoom(Long chatRoomId) {

        // 로그인 유저 정보 가져옴
        Long loginUserId = getAuthenticatedMemberId();
        Users loginUser = userRepository.findById(loginUserId).get();   // getAuthenticatedMemberId() 호출 시 예외 처리 완료

        // 채팅방 정보 가져옴, 존재하지 않다면 404 에러 반환
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        // 유저 정보와 채팅방 정보가 일치하지 않다면 403 에러 반환
        if(!chatRoom.getUserId().equals(loginUser.getId())
                && !chatRoom.getGosuId().equals(loginUser.getId())) {
            throw new CustomException(ErrorCode.USER_NOT_AUTHORIZED);
        }

        // 이미 채팅방을 나간 이력이 있는 경우 410 에러 반환
        if( (loginUser.getAuthority() == Authority.ROLE_GOSU && chatRoom.getGosuDeletedAt() != null) ||
                (loginUser.getAuthority() == Authority.ROLE_USER && chatRoom.getUserDeletedAt() != null) ) {
            throw new CustomException(ErrorCode.CHAT_ROOM_DELETED);
        }

        // USER의 고수 여부 포함하여 채팅방 나가기
        chatRoom.quitChatRoom((loginUser.getAuthority() == Authority.ROLE_GOSU));

        // TODO: 바로 새로고침 되게
        // 채팅방 퇴장 메세지 생성
        ChatMessageRequestDto chatMessageRequestDto = ChatMessageRequestDto.builder()
                .sourceUserId(loginUser.getId())
                .type(ChatMessageType.QUIT)
                .content(loginUser.getFullName() + " 님이 채팅방을 나갔습니다.").build();
        ChatMessage quitChatMessage = chatMessageMapper.toEntity(chatMessageRequestDto);
        quitChatMessage.setChatRoom(chatRoom);
        chatMessageRepository.save(quitChatMessage);
        return new ChatRoomDeleteResponseDto(chatRoomId);
    }

    // 채팅방에서 가격 변경
    @Transactional
    public ChatRoomPriceResponseDto updatePrice(Long chatRoomId, int price) {

        // 가격이 음수인 경우 400 에러 반환
        if(price < 0) {
            throw new CustomException(ErrorCode.INVALID_PRODUCT_PRICE);
        }

        // 로그인 유저 정보 가져옴
        Long loginUserId = getAuthenticatedMemberId();
        Users loginUser = userRepository.findById(loginUserId).get();   // getAuthenticatedMemberId() 호출 시 예외 처리 완료

        // 채팅방 정보가 없다면 404 에러 반환
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        // 고수가 아니거나 채팅방 참여 인원이 아니면 403 에러 반환
        if(loginUser.getAuthority() != Authority.ROLE_GOSU || !chatRoom.getGosuId().equals(loginUser.getId())) {
            throw new CustomException(ErrorCode.USER_NOT_AUTHORIZED);
        }

        chatRoom.setPrice(price);

        return new ChatRoomPriceResponseDto(price);
    }

    // 사용자의 권환 확인 + userId 가져오는 메서드
    // 따로 분리한 이유 : RuntimeException이 아닌 커스텀 예외 처리 위해서
    private Long getAuthenticatedMemberId() {
        try {
            return securityUtil.getCurrentMemberId();
        } catch (RuntimeException e) {
            throw new CustomException(ErrorCode.USER_NOT_AUTHORIZED);
        }
    }
}
