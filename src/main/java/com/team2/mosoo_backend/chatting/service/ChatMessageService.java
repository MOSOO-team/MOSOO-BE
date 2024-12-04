package com.team2.mosoo_backend.chatting.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.mosoo_backend.chatting.dto.*;
import com.team2.mosoo_backend.chatting.entity.ChatMessage;
import com.team2.mosoo_backend.chatting.entity.ChatMessageType;
import com.team2.mosoo_backend.chatting.entity.ChatRoom;
import com.team2.mosoo_backend.chatting.mapper.ChatMessageMapper;
import com.team2.mosoo_backend.chatting.repository.ChatMessageQueryRepository;
import com.team2.mosoo_backend.chatting.repository.ChatMessageRepository;
import com.team2.mosoo_backend.chatting.repository.ChatRoomRepository;
import com.team2.mosoo_backend.config.SecurityUtil;
import com.team2.mosoo_backend.exception.CustomException;
import com.team2.mosoo_backend.exception.ErrorCode;
import com.team2.mosoo_backend.user.entity.Users;
import com.team2.mosoo_backend.user.repository.UserRepository;
import com.team2.mosoo_backend.utils.s3bucket.service.S3BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageMapper chatMessageMapper;
    private final SecurityUtil securityUtil;
    private final S3BucketService s3BucketService;
    private final ChatRoomUtils chatRoomUtils;
    private final ChatMessageQueryRepository chatMessageQueryRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;

    // 채팅 저장 메서드 (레디스 -> db)
    @Transactional
    public void saveChatMessages(Long chatRoomId) {

        // 채팅방 정보 조회
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        List<ChatMessageRequestDto> chatMessageRequestDtos = findChatMessagesFromRedis(chatRoomId, false);

        // 저장할 리스트
        List<ChatMessage> chatMessagesToSave = new ArrayList<>();

        // 생성시간 기준 내림차순으로 저장되었으므로 역순으로 순회함
        for (int i = chatMessageRequestDtos.size() - 1; i >= 0; i--) {
            ChatMessageRequestDto chatMessageRequestDto = chatMessageRequestDtos.get(i);

            ChatMessage createdChatMessage = chatMessageMapper.toEntity(chatMessageRequestDto);

            createdChatMessage.setChatRoom(chatRoom);

            chatMessagesToSave.add(createdChatMessage);
        }

        // Batch 처리한 저장
        chatMessageRepository.saveAll(chatMessagesToSave);
    }

    // 레디스에서 채팅 메세지 조회하는 메서드
    public List<ChatMessageRequestDto> findChatMessagesFromRedis(Long chatRoomId, boolean isOne) {
        String redisKey = "chatRoom:" + chatRoomId + ":messages";

        // Redis에서 메세지를 가져옴
        List<Object> messages;
        if(isOne) {     // 찾는 메세지 수가 1개라면
             messages = redisTemplate.opsForList().range(redisKey, -1, -1);
        } else {        // 찾는 메세지 수가 1개가 아니라면
            messages = redisTemplate.opsForList().range(redisKey, 0, -1);
        }

        // LinkedHashMap을 ChatMessageRequestDto로 변환
        return messages.stream()
                .map(msg -> objectMapper.convertValue(msg, ChatMessageRequestDto.class)) // 변환
                .collect(Collectors.toList());
    }

    // 채팅 내역 조회 메서드 (no-offset + Redis)
    // 첫 조회 : 레디스에서 조회
    // 이후 조회 or 레디스의 체팅 메세지가 적은 경우 : db에서 추가적으로 조회
    public ChatMessageResponseWrapperDto findChatMessages(Long chatRoomId, Long index, boolean isInit) {

        // 로그인 유저 정보 가져옴
        Long loginUserId = getAuthenticatedMemberId();
        Users loginUser = userRepository.findById(loginUserId).get();   // getAuthenticatedMemberId() 호출 시 예외 처리 완료

        // 채팅방 접근 권한 검증
        ChatRoom foundChatRoom = chatRoomUtils.validateChatRoomOwnership(chatRoomId, loginUser);

        // 채팅 상대 이름 저장
        String opponentFullName = chatRoomUtils.getOpponentFullName(foundChatRoom, loginUser);

        // 조회한 채팅 메시지를 dto로 변환
        List<ChatMessageResponseDto> result = new ArrayList<>();
        if(isInit) {    // 처음 채팅 메세지 목록 조회 => Redis에서 조회함
            List<ChatMessageRequestDto> chatMessagesFromRedis = findChatMessagesFromRedis(chatRoomId,false);
            for (ChatMessageRequestDto dto : chatMessagesFromRedis) {
                ChatMessageResponseDto responseDto = chatMessageMapper.requestDtoToResponseDto(dto);
                result.add(responseDto);
            }
        }
        if (!isInit || result.size() <= 10) {       // 첫 조회가 아니거나 Redis에서 조회한 목록의 크기가 10개 이하일 때

            // 생성일 기준 내림차순, 페이지 당 20개씩 조회
            Pageable pageable = PageRequest.of(0, 20 - result.size(), Sort.by(Sort.Direction.DESC, "created_at")); // Pageable 객체 생성

            // 채팅 메시지를 생성시간 기준 오름차순으로 조회
            Page<ChatMessage> chatMessageList = chatMessageQueryRepository.findChatMessagesByChatRoomIdUsingNoOffset(pageable, chatRoomId, index);

            for (ChatMessage chatMessage : chatMessageList) {

                ChatMessageResponseDto dto = chatMessageMapper.toChatMessageResponseDto(chatMessage);

                // 파일인 경우에만 fileName 필드 set
                if (chatMessage.getType() == ChatMessageType.FILE) {
                    String[] split = chatMessage.getContent().split("-");
                    dto.setFileName(split[split.length - 1]);     // s3 업로드 url 형태 : "url-파일이름"
                }
                result.add(dto);
            }
        }
        return new ChatMessageResponseWrapperDto(opponentFullName, result, result.size());
    }

    // base64 파일 -> MultipartFile 로 변환하는 메서드
    public ChatMessageRequestDto convertToMultipartFile(ChatMessageRequestDto chatMessageRequestDto) {

        try {
            // Base64 문자열에서 MIME 타입과 데이터 부분 분리
            String[] parts = chatMessageRequestDto.getBase64File().split(",");
            byte[] fileData = Base64.getDecoder().decode(parts[1]); // 바이트 배열로 변환

            // 변환된 바이트 배열, 파일 이름으로 ByteArrayMultipartFile 생성함
            MultipartFile multipartFile = new ByteArrayMultipartFile(chatMessageRequestDto.getFileName(), fileData);

            String uploadFileUrl = s3BucketService.uploadFile(multipartFile);
            chatMessageRequestDto.setContent(uploadFileUrl);

        } catch (IOException e) {
            throw new CustomException(ErrorCode.IO_EXCEPTION_ON_IMAGE_UPLOAD);
        }

        return chatMessageRequestDto;
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
