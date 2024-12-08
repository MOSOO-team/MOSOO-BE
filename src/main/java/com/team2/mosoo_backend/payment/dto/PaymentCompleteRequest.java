package com.team2.mosoo_backend.payment.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@Builder
public class PaymentCompleteRequest {

    private String impUid;
    private String merchantId;
}
