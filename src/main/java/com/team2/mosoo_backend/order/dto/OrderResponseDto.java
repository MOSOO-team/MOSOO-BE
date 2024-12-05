package com.team2.mosoo_backend.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {

    private String workDate;

    private Long orderId;

    private BigDecimal price;

    private LocalDateTime paidAt;

    private String gosuName;
}

//고수 이름, 금액, 결제 완료일, 진행일자