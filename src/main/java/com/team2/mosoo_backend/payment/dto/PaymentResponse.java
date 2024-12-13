package com.team2.mosoo_backend.payment.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {

    private String impUid;
    private String merchantUid;
    private Long price;
    private String status;
    private LocalDateTime createTime;
}
//진행날짜, 금액, 결제완료 날짜 추가