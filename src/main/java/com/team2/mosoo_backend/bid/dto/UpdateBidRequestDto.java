package com.team2.mosoo_backend.bid.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBidRequestDto {

    private long id;
    private int price;
    private LocalDateTime date;

}
