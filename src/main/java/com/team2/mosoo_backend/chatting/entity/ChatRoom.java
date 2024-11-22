package com.team2.mosoo_backend.chatting.entity;

import com.team2.mosoo_backend.bid.entity.Bid;
import com.team2.mosoo_backend.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ChatRoom {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @Column(nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime userDeletedAt;

    private LocalDateTime gosuDeletedAt;

    private Long userId;

    private Long gosuId;

    // 단방향 N:1 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    // 단방향 1:1 연관관계
    @OneToOne
    @JoinColumn(name = "bid_id")
    private Bid bid;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ChatMessage> chatMessageList = new ArrayList<>();

    public void setMappings(Post post, Bid bid) {
        this.post = post;
        this.bid = bid;
    }

    // 채팅방 나가기 메서드
    public void quitChatRoom(boolean isGosu) {

        if(isGosu) {
            this.gosuDeletedAt = LocalDateTime.now();
        } else {
            this.userDeletedAt = LocalDateTime.now();
        }
    }
}
