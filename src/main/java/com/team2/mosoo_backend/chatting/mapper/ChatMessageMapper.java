package com.team2.mosoo_backend.chatting.mapper;

import com.team2.mosoo_backend.chatting.dto.ChatMessageRequestDto;
import com.team2.mosoo_backend.chatting.dto.ChatMessageResponseDto;
import com.team2.mosoo_backend.chatting.entity.ChatMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {

    @Mapping(target = "chatRoom", ignore = true)
    @Mapping(source = "sourceUserId", target = "sourceUserId")
    @Mapping(source = "content", target = "content")
    ChatMessage toEntity(ChatMessageRequestDto chatMessageRequestDto);

    @Mapping(source = "id", target = "chatMessageId")
    ChatMessageResponseDto toChatMessageResponseDto(ChatMessage chatMessage);

    @Mapping(target = "chatMessageId", ignore = true)
    ChatMessageResponseDto requestDtoToResponseDto(ChatMessageRequestDto chatMessageRequestDto);
}
