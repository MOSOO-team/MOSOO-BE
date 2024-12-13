package com.team2.mosoo_backend.payment.controller;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.team2.mosoo_backend.order.service.OrderService;
import com.team2.mosoo_backend.payment.dto.PaymentCancelRequest;
import com.team2.mosoo_backend.payment.dto.PaymentCompleteRequest;
import com.team2.mosoo_backend.payment.dto.PaymentResponse;
import com.team2.mosoo_backend.payment.service.PaymentService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;



    @PostMapping(value = "/complete", produces = "application/json")
    public ResponseEntity<PaymentResponse> completePayment(
            @AuthenticationPrincipal UserDetails userDetails, @RequestBody PaymentCompleteRequest request) {
            PaymentResponse response = paymentService.verifyPayment(request, Long.parseLong(userDetails.getUsername()));
            return ResponseEntity.ok(response);
        }
/*
    @GetMapping("/paymentHistory/{userId}")
    public ResponseEntity<List<PaymentHistoryDto>> paymentList(@PathVariable Long userId){
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.paymentHistoryList(userId));
    }*/


}
