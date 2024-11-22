package com.team2.mosoo_backend.chatting.mapper;

import com.team2.mosoo_backend.chatting.dto.ChatMessageRequestDto;
import com.team2.mosoo_backend.chatting.dto.ChatMessageResponseDto;
import com.team2.mosoo_backend.chatting.entity.ChatMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ChatMessageMapper {

    ChatMessageMapper INSTANCE = Mappers.getMapper(ChatMessageMapper.class);

    @Mapping(target = "chatRoom", ignore = true)
    @Mapping(source = "sourceUserId", target = "sourceUserId")
    @Mapping(source = "content", target = "content")
    ChatMessage toEntity(ChatMessageRequestDto chatMessageRequestDto);

    @Mapping(target = "userFullName", ignore = true)
    ChatMessageResponseDto toChatMessageResponseDto(ChatMessage chatMessage);
}
