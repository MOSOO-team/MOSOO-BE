package com.team2.mosoo_backend.chatting.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "chatRoomConnection")
public class ChatRoomConnection {

    @Id
    private String id;

    private int connectionCount; // 연결된 유저 수

    // 연결 수 증가 메서드
    public void incrementConnectionCount() {
        if(this.connectionCount == 2) return;
        this.connectionCount++;
    }

    // 연결 수 감소 메서드
    public void decrementConnectionCount() {
        if(this.connectionCount == 0) return;
        this.connectionCount--;
    }
}
