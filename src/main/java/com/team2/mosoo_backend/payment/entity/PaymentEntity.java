package com.team2.mosoo_backend.payment.entity;

import com.team2.mosoo_backend.common.entity.BaseEntity;
import com.team2.mosoo_backend.order.entity.Order;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.joda.time.DateTime;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PaymentEntity extends BaseEntity {

    public static final String ENTITY_PREFIX = "payment";


    //  product, 상품이름, 서비스 완료 상태 여부(이용내역 위한)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private PaymentStatusType status;


    @Column(name = "impUid")
    private String impUid; //포트원 결제 고유 번호

    private String merchantUid; //랜덤으로 만들기
}
