package com.team2.mosoo_backend.order.entity;

import com.team2.mosoo_backend.chatting.entity.ChatRoom;
import com.team2.mosoo_backend.common.entity.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
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

    @Column(name = "orders_merchantUid", nullable = false)
    private String merchantUid; // 주문번호


    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Setter
    @Column(name = "orders_price", nullable = false)
    private BigDecimal price; // 결제금액

    @Column(name = "orders_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;


    public void setOrderStatus(OrderStatus status) {
        this.orderStatus = status;
    }

}
