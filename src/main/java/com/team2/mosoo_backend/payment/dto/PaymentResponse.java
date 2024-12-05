package com.team2.mosoo_backend.payment.dto;

import lombok.Builder;

@Builder
public class PaymentResponse {

    private String impUid;
    private String merchantUid;
    private Long amount;
    private Long price;
    private String status;
}
