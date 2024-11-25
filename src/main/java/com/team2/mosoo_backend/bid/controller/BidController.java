package com.team2.mosoo_backend.bid.controller;

import com.team2.mosoo_backend.bid.dto.BidListResponseDto;
import com.team2.mosoo_backend.bid.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bid")
public class BidController {

    private final BidService bidService;


    @GetMapping("/{postId}")
    public ResponseEntity<BidListResponseDto> getBidByPost(@PathVariable("postId") Long postId) {

        BidListResponseDto bidListResponseDto = new BidListResponseDto();

        return ResponseEntity.status(200).body(bidListResponseDto);

    }

}
