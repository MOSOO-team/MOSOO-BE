package com.team2.mosoo_backend.order.dto;

import com.team2.mosoo_backend.order.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequestDto {

    private int price;

    private Status status;

    private String method;
}
