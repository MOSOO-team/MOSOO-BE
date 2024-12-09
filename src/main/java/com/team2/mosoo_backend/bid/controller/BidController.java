package com.team2.mosoo_backend.bid.controller;

import com.team2.mosoo_backend.bid.dto.*;
import com.team2.mosoo_backend.bid.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bid")
public class BidController {

    private final BidService bidService;


    @GetMapping("/{postId}")
    public ResponseEntity<BidListResponseDto> getBidByPost(@PathVariable("postId") Long postId) {
        BidListResponseDto bidListResponseDto = bidService.getBidByPost(postId);
        return ResponseEntity.status(200).body(bidListResponseDto);
    }

    @PostMapping("/{postId}")
    public ResponseEntity<BidResponseDto> createBid(
            @PathVariable("postId") Long postId,
            @RequestBody CreateBidRequestDto createBidRequestDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        BidResponseDto bidResponseDto = bidService.createBidByPost(Long.parseLong(userDetails.getUsername()), postId, createBidRequestDto);

        return ResponseEntity.status(201).body(bidResponseDto);
    }

    @PutMapping
    public ResponseEntity<BidResponseDto> updateBid(@RequestBody UpdateBidRequestDto updateBidRequestDto) {

        BidResponseDto bidResponseDto = bidService.updateBid(updateBidRequestDto);

        return ResponseEntity.status(201).body(bidResponseDto);
    }

    @DeleteMapping("/{bidId}")
    public ResponseEntity<BidResponseDto> deleteBid(@PathVariable("bidId") Long bidId) {

        BidResponseDto bidResponseDto = bidService.deleteBid(bidId);

        return ResponseEntity.status(200).body(bidResponseDto);
    }


}
