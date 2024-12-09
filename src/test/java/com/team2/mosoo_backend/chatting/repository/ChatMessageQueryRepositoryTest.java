package com.team2.mosoo_backend.chatting.repository;

import com.team2.mosoo_backend.chatting.entity.ChatMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 전체 컨텍스트를 로드
@DisplayName("(QueryDSL 적용) 채팅 메세지 레포지토리 단위 테스트")
class ChatMessageQueryRepositoryTest {

    @Autowired
    private ChatMessageQueryRepository chatMessageQueryRepository;

    @Test
    @DisplayName("성능 개선 후 조회 시간 테스트")
    public void findChatMessagesNoOffset() throws Exception {

        //given
        int limit = 20;
        Pageable pageable = Pageable.ofSize(limit);

        //when
        long startTime = System.nanoTime(); // 시작 시간 기록
        Page<ChatMessage> chatMessagesNoOffset = chatMessageQueryRepository.findChatMessagesByChatRoomIdUsingNoOffset(
                pageable, 1L, 101L);
        long endTime = System.nanoTime(); // 종료 시간 기록
        System.out.println("성능 개선 후 실행 시간: " + (double) (endTime - startTime)/1000000000 + "s");

        //then
        assertThat(chatMessagesNoOffset).isNotEqualTo(null);

        // 추가: 메시지 내용 비교
        List<ChatMessage> noOffsetMessages = chatMessagesNoOffset.getContent();
        for (ChatMessage noOffsetMessage : noOffsetMessages) {
            System.out.println("noOffsetMessage.getContent() = " + noOffsetMessage.getContent());
        }
    }
}