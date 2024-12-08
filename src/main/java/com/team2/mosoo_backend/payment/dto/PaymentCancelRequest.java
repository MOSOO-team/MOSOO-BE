package com.team2.mosoo_backend.payment.dto;

import lombok.Getter;

@Getter
public class PaymentCancelRequest {

    private String impUid;
    private Long amount;
    private String reason;

}
