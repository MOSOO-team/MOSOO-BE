package com.team2.mosoo_backend.bid.service;


import com.team2.mosoo_backend.bid.repository.BidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BidService {

    private final BidRepository bidRepository;



}
