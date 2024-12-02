package com.team2.mosoo_backend.chatting.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team2.mosoo_backend.chatting.entity.ChatMessage;
import com.team2.mosoo_backend.chatting.entity.QChatMessage;
import com.team2.mosoo_backend.chatting.entity.QChatRoom;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatMessageQueryRepository {

    // jpa의 EntityManager 의존 주입
    private final EntityManager em;

    // 특정 채팅방에 대한 채팅 메시지를 no-offset 방식으로 조회하는 메서드
    public Page<ChatMessage> findChatMessagesByChatRoomIdUsingNoOffset(Pageable pageable, Long chatRoomId, @Nullable Long index) {

        // JPAQueryFactory 생성 => QueryDSL 쿼리 작성 가능
        JPAQueryFactory query = new JPAQueryFactory(em);
        QChatRoom chatRoom = QChatRoom.chatRoom;    // QChatRoom 타입 인스턴스 생성
        QChatMessage chatMessage = QChatMessage.chatMessage;    // QChatMessage 타입 인스턴스 생성

        List<ChatMessage> results =
                query.select(chatMessage)
                        .from(chatMessage)
                        .join(chatMessage.chatRoom, chatRoom)
                        .where(chatRoom.id.eq(chatRoomId) // 조인 조건
                                .and(ltChatMessageId(index)))   // no-offset 조건
                        .orderBy(chatMessage.createdAt.desc())
                        .limit(20)
                        .fetch();

        return new PageImpl<>(results, pageable, results.size());
    }

    // 주어진 index 보다 작은 chat_message_id에 대한 조건 생성 메서드
    public BooleanExpression ltChatMessageId(@Nullable Long index) {

        return index == null ? null : QChatMessage.chatMessage.id.lt(index);
    }
}
