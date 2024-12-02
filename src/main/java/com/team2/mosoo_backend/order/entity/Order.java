package com.team2.mosoo_backend.order.entity;

import com.team2.mosoo_backend.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    private int price;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String method;

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStatus(String status) {
        this.status = Status.valueOf(status);
    }

}
