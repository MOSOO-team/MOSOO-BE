package com.team2.mosoo_backend.chatting.service;

import com.team2.mosoo_backend.chatting.dto.ByteArrayMultipartFile;
import com.team2.mosoo_backend.chatting.dto.ChatMessageRequestDto;
import com.team2.mosoo_backend.chatting.dto.ChatMessageResponseDto;
import com.team2.mosoo_backend.chatting.dto.ChatMessageResponseWrapperDto;
import com.team2.mosoo_backend.chatting.entity.ChatMessage;
import com.team2.mosoo_backend.chatting.entity.ChatMessageType;
import com.team2.mosoo_backend.chatting.entity.ChatRoom;
import com.team2.mosoo_backend.chatting.mapper.ChatMessageMapper;
import com.team2.mosoo_backend.chatting.repository.ChatMessageRepository;
import com.team2.mosoo_backend.chatting.repository.ChatRoomRepository;
import com.team2.mosoo_backend.exception.CustomException;
import com.team2.mosoo_backend.exception.ErrorCode;
//import com.team2.mosoo_backend.user.entity.User;
//import com.team2.mosoo_backend.user.repository.UserRepository;
import com.team2.mosoo_backend.user.entity.User;
import com.team2.mosoo_backend.utils.s3bucket.service.S3BucketService;
import lombok.RequiredArgsConstructor;
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
//    private final UserRepository userRepository;
    private final S3BucketService s3BucketService;

    // 채팅 저장 메서드
    @Transactional
    public void saveChatMessage(Long chatRoomId, ChatMessageRequestDto chatMessageRequestDto) {

        if(chatMessageRequestDto.getBase64File() != null) {
            try {
                MultipartFile multipartFile = convertToMultipartFile(chatMessageRequestDto.getBase64File(), "chattingUploadFile");

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
    public ChatMessageResponseWrapperDto findChatMessages(Long chatRoomId) {

        // 채팅방이 존재하지 않으면 404 에러 반환
        ChatRoom foundChatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        // TODO: 유저 정보 가져옴
//        User loginUser = getLoginUser();

        // TODO: 유저 정보가 일치하지 않으면 403 에러 반환
//        if(chatRoom.getUserId() != loginUser.getId() && chatRoom.getGosuId() != loginUser.getId()) {
//            throw new CustomException(ErrorCode.USER_NOT_AUTHORIZED);
//        }

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

        return new ChatMessageResponseWrapperDto(result, result.size());
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
}
