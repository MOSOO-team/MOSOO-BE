package com.team2.mosoo_backend.payment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@Data
public class PaymentCompleteRequest {

    @JsonProperty("impUid")
    private String impUid;

    @JsonProperty("merchantUid")
    private String merchantUid;

}
