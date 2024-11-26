package com.team2.mosoo_backend.payment.entity;

import com.team2.mosoo_backend.common.entity.BaseEntity;
import com.team2.mosoo_backend.post.entity.Post;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity {

    public static final String ENTITY_PREFIX = "payment";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Column(nullable = false)
    private Long payment_uid;

    @Column(nullable = false)
    private Long userId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private String status;

    private DateTime requested_at;

    private DateTime paid_at;
}
