package com.team2.mosoo_backend.chatting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team2.mosoo_backend.chatting.dto.ChatMessageResponseDto;
import com.team2.mosoo_backend.chatting.dto.ChatMessageResponseWrapperDto;
import com.team2.mosoo_backend.chatting.entity.ChatMessageType;
import com.team2.mosoo_backend.chatting.service.ChatMessageService;
import com.team2.mosoo_backend.jwt.TokenProvider;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatMessageController.class)
@AutoConfigureMockMvc(addFilters = false) // 필터 제외 (JWT 검증 제외)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("채팅 메세지 컨트롤러 단위 테스트")
class ChatMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenProvider tokenProvider;    // TokenProvider 빈을 로드하지 못하는 에러 해결 위해서 명시적으로 빈으로 등록

    @MockBean
    private ChatMessageService chatMessageService;

    @Autowired
    private ObjectMapper objectMapper;

    static Long chatRoomId = 1L;

    private ChatMessageResponseWrapperDto chatMessageResponseWrapperDto() {

        List<ChatMessageResponseDto> list = new ArrayList<>();
        for (Long i = 1L; i <= 20L; i++) {
            ChatMessageResponseDto dto = new ChatMessageResponseDto(i, 1L, Long.toString(i), null, LocalDateTime.now(), ChatMessageType.MESSAGE, true);
            list.add(dto);
        }

        return new ChatMessageResponseWrapperDto("상대 이름", list, 1);
    }

    @Test
    @Order(1)
    @DisplayName("채팅 메세지 조회 메서드 테스트")
    public void findChatRoomTest() throws Exception {

        //given
        given(chatMessageService.findChatMessages(chatRoomId, null, false))
                .willReturn(chatMessageResponseWrapperDto());

        //when
        ResultActions resultActions = mockMvc.perform(get("/api/chatroom/" + chatRoomId)
                .param("isInit", "false"));

        //then
        resultActions.andExpect(status().is(200));
    }

}