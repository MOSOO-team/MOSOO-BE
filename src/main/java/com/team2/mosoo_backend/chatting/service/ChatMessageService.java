package com.team2.mosoo_backend.chatting.service;

import com.team2.mosoo_backend.chatting.dto.*;
import com.team2.mosoo_backend.chatting.entity.ChatMessage;
import com.team2.mosoo_backend.chatting.entity.ChatMessageType;
import com.team2.mosoo_backend.chatting.entity.ChatRoom;
import com.team2.mosoo_backend.chatting.mapper.ChatMessageMapper;
import com.team2.mosoo_backend.chatting.repository.ChatMessageRepository;
import com.team2.mosoo_backend.chatting.repository.ChatRoomRepository;
import com.team2.mosoo_backend.exception.CustomException;
import com.team2.mosoo_backend.exception.ErrorCode;
import com.team2.mosoo_backend.user.entity.Users;
import com.team2.mosoo_backend.user.repository.UserRepository;
import com.team2.mosoo_backend.utils.s3bucket.service.S3BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageMapper chatMessageMapper;
    private final UserRepository userRepository;
    private final S3BucketService s3BucketService;
    private final ChatRoomUtils chatRoomUtils;

    // 채팅 저장 메서드
    @Transactional
    public void saveChatMessage(Long chatRoomId, ChatMessageRequestDto chatMessageRequestDto) {

        if(chatMessageRequestDto.getBase64File() != null) {
            try {
                MultipartFile multipartFile = convertToMultipartFile(chatMessageRequestDto.getBase64File(), chatMessageRequestDto.getFileName());

                String uploadFileUrl = s3BucketService.uploadFile(multipartFile);
                chatMessageRequestDto.setContent(uploadFileUrl);

                // 이미지 타입 저장
                if (multipartFile.getContentType() != null && multipartFile.getContentType().startsWith("image/")) {
                    chatMessageRequestDto.setType(ChatMessageType.IMAGE);
                }
                // 비디오 타입 저장
                else if(multipartFile.getContentType() != null && multipartFile.getContentType().startsWith("video/")) {
                    chatMessageRequestDto.setType(ChatMessageType.VIDEO);
                }

            } catch (IOException e) {
                throw new CustomException(ErrorCode.IO_EXCEPTION_ON_IMAGE_UPLOAD);
            }
        }

        ChatMessage createdChatMessage = chatMessageMapper.toEntity(chatMessageRequestDto);

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        createdChatMessage.setChatRoom(chatRoom);

        chatMessageRepository.save(createdChatMessage);
    }

    // 채팅 내역 조회 메서드
    public ChatMessageResponseWrapperDto findChatMessages(Long chatRoomId, int offset) {

        // 로그인 유저 정보 가져옴
        Users loginUser = getLoginUser();

        // 채팅방 접근 권한 검증
        ChatRoom foundChatRoom = chatRoomUtils.validateChatRoomOwnership(chatRoomId, loginUser);

        // 채팅 상대 이름 저장
        String opponentFullName = chatRoomUtils.getOpponentFullName(foundChatRoom, loginUser);

        // 한 페이지에 (offset 부터) 20개, 생성일 기준 내림차순
        int limit = 20;
        PageRequest pageRequest = PageRequest.of(offset/limit, limit,
                Sort.by("createdAt").descending());

        // 채팅 메시지를 생성시간 기준 오름차순으로 조회
        Page<ChatMessage> chatMessageList = chatMessageRepository.findChatMessagesByChatRoomId(pageRequest, chatRoomId);

        // 조회한 채팅 메시지를 dto로 변환
        List<ChatMessageResponseDto> result = new ArrayList<>();
        for (ChatMessage chatMessage : chatMessageList) {

            ChatMessageResponseDto dto = chatMessageMapper.toChatMessageResponseDto(chatMessage);

            // 파일인 경우에만 fileName 필드 set
            if(chatMessage.getType() == ChatMessageType.FILE) {
                String[] split = chatMessage.getContent().split("-");
                dto.setFileName(split[split.length-1]);     // s3 업로드 url 형태 : "url-파일이름"
            }
            result.add(dto);
        }

        return new ChatMessageResponseWrapperDto(opponentFullName, result, result.size());
    }

    // base64 파일 -> MultipartFile 로 변환하는 메서드
    public static MultipartFile convertToMultipartFile(String base64File, String fileName) {

        // Base64 문자열에서 MIME 타입과 데이터 부분 분리
        String[] parts = base64File.split(",");
        String mimeType = parts[0].split(":")[1].split(";")[0]; // MIME 타입 추출
        byte[] fileData = Base64.getDecoder().decode(parts[1]); // 바이트 배열로 변환

        // 변환된 바이트 배열, 파일 이름으로 ByteArrayMultipartFile 생성함
        return new ByteArrayMultipartFile(fileName, fileData, mimeType);
    }

    // TODO: USER 정보 가져오기 확인 + 권한 확인
    public Users getLoginUser() {
        return userRepository.findById(4L).orElse(null);
    }

}
