package com.team2.mosoo_backend.payment.dto;

import lombok.Builder;

@Builder
public class PaymentResponse {

    private String impUid;
    private String merchantUid;
    private Long price;
    private String status;
}
//진행날짜, 금액, 결제완료 날짜 추가