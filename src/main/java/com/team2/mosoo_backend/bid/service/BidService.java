package com.team2.mosoo_backend.bid.service;


import com.team2.mosoo_backend.bid.dto.BidListResponseDto;
import com.team2.mosoo_backend.bid.dto.BidResponseDto;
import com.team2.mosoo_backend.bid.dto.CreateBidRequestDto;
import com.team2.mosoo_backend.bid.dto.UpdateBidRequestDto;
import com.team2.mosoo_backend.bid.entity.Bid;
import com.team2.mosoo_backend.bid.mapper.BidMapper;
import com.team2.mosoo_backend.bid.repository.BidRepository;
import com.team2.mosoo_backend.exception.CustomException;
import com.team2.mosoo_backend.exception.ErrorCode;
import com.team2.mosoo_backend.post.entity.Post;
import com.team2.mosoo_backend.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BidService {

    private final BidRepository bidRepository;
    private final PostRepository postRepository;
    private final BidMapper bidMapper;


    public BidListResponseDto getBidByPost(Long postId) {

        List<BidResponseDto> dtoList = new ArrayList<>();
        List<Bid> bidList = bidRepository.findAllByPostId(postId);

        for(Bid bid : bidList) {
            BidResponseDto bidResponseDto = bidMapper.bidToBidResponseDto(bid);
            dtoList.add(bidResponseDto);
        }

        return new BidListResponseDto(dtoList);
    }


    public BidResponseDto createBidByPost(Long postId, CreateBidRequestDto createBidRequestDto) {

        Bid bid = bidMapper.createBidRequestDtoToBid(createBidRequestDto);
        Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        bid.setPost(post);

        Bid createdBid = bidRepository.save(bid);

        return bidMapper.bidToBidResponseDto(createdBid);
    }

    public BidResponseDto updateBid(UpdateBidRequestDto updateBidRequestDto) {

        Bid bid = bidRepository.findById(updateBidRequestDto.getId()).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        bid.updateBid(updateBidRequestDto);

        return bidMapper.bidToBidResponseDto(bidRepository.save(bid));

    }

    public BidResponseDto deleteBid(Long bidId) {
        Bid bid = bidRepository.findById(bidId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        BidResponseDto bidResponseDto = bidMapper.bidToBidResponseDto(bid);

        bidRepository.delete(bid);

        return bidResponseDto;
    }
}