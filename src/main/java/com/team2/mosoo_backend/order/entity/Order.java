package com.team2.mosoo_backend.order.entity;

import com.team2.mosoo_backend.chatting.entity.ChatRoom;
import com.team2.mosoo_backend.common.entity.BaseEntity;
import com.team2.mosoo_backend.user.entity.Users;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.catalina.User;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id", nullable = false)
    private Long id;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String method;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_romm_id", nullable = false)
    private ChatRoom chatRoom;


    private String merchantUid; // 주문번호

    @ManyToOne
    @JoinColumn(name = "user_user_id")
    private Users user;


    public void setMerchantUid(String merchantUid) {
        this.merchantUid = merchantUid;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setStatus(String status) {
        this.status = Status.valueOf(status);
    }

    public void setMapping(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }
}
