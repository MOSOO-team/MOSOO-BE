package com.team2.mosoo_backend.order.dto;

import com.team2.mosoo_backend.bid.dto.BidResponseDto;
import com.team2.mosoo_backend.post.dto.PostResponseDto;
import com.team2.mosoo_backend.user.dto.GosuResponseDto;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class OrderDetailsResponseDto {

    private PostResponseDto postResponseDto;
    private BidResponseDto bidResponseDto; // 금액, 날짜
    private GosuResponseDto gosuResponseDto;
    private BigDecimal price;




}