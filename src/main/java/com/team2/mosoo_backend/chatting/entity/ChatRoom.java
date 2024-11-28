package com.team2.mosoo_backend.chatting.entity;

import com.team2.mosoo_backend.bid.entity.Bid;
import com.team2.mosoo_backend.common.entity.BaseEntity;
import com.team2.mosoo_backend.post.entity.Post;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ChatRoom extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    private LocalDateTime userDeletedAt;

    private LocalDateTime gosuDeletedAt;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long gosuId;

    @Column(nullable = false)
    @Min(value = 0)
    @Setter
    private int price;

    // 단방향 N:1 연관관계
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    // 단방향 1:1 연관관계
    @Setter
    @OneToOne
    @JoinColumn(name = "bid_id")
    private Bid bid;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ChatMessage> chatMessageList = new ArrayList<>();

    // 채팅방 나가기 메서드
    public void quitChatRoom(boolean isGosu) {

        if(isGosu) {
            this.gosuDeletedAt = LocalDateTime.now();
        } else {
            this.userDeletedAt = LocalDateTime.now();
        }
    }
}
