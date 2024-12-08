package com.team2.mosoo_backend.chatting.repository;

import com.team2.mosoo_backend.chatting.entity.ChatMessage;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest // 전체 컨텍스트를 로드
@DisplayName("(JPA) 채팅 메세지 레포지토리 단위 테스트")
class ChatMessageRepositoryTest {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Test
    @DisplayName("성능 개선 전 조회 시간 테스트")
    public void findChatMessagesLegacy() throws Exception {

        //given
        int limit = 20;
        int offset = 1089900;
        PageRequest pageRequest = PageRequest.of(offset / limit, limit,
                Sort.by("createdAt").descending());

        //when
        long startTime = System.nanoTime(); // 시작 시간 기록
        Page<ChatMessage> chatMessagesLegacy = chatMessageRepository.findChatMessagesByChatRoomId(pageRequest, 1L);
        long endTime = System.nanoTime(); // 종료 시간 기록
        System.out.println("성능 개선 전 실행 시간: " + (double) (endTime - startTime)/1000000000 + "s");


        //then
        assertThat(chatMessagesLegacy).hasSize(20);

        List<ChatMessage> legacyMessages = chatMessagesLegacy.getContent();
        for (ChatMessage legacyMessage : legacyMessages) {
            System.out.println("legacyMessage.getContent() = " + legacyMessage.getContent());
        }
    }
}
