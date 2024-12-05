package com.team2.mosoo_backend.payment.service;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.team2.mosoo_backend.exception.CustomException;
import com.team2.mosoo_backend.exception.ErrorCode;
import com.team2.mosoo_backend.order.entity.Order;
import com.team2.mosoo_backend.order.entity.OrderStatus;
import com.team2.mosoo_backend.order.repository.OrderRepository;
import com.team2.mosoo_backend.order.service.OrderService;
import com.team2.mosoo_backend.payment.config.PortoneProperties;
import com.team2.mosoo_backend.payment.dto.PaymentCancelRequest;
import com.team2.mosoo_backend.payment.dto.PaymentCompleteRequest;
import com.team2.mosoo_backend.payment.dto.PaymentResponse;
import com.team2.mosoo_backend.payment.entity.PaymentEntity;
import com.team2.mosoo_backend.payment.entity.PaymentStatusType;
import com.team2.mosoo_backend.payment.repository.PaymentRepository;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;

import com.siot.IamportRestClient.request.CancelData;
import java.math.BigDecimal;

@Data
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PortoneProperties portoneProperties;
    //private IamportClient iamportClient;
    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final OrderRepository orderRepository;


    private static String impSecret;
    private static String impKey;



    @Value("${secret.impSecret}")
    public void setKey(String value) {
        impSecret = value;
    }

    @Value("${secret.impKey}")
    public void setImpKey(String value) {
        impKey = value;
    }


    public PaymentResponse verifyPayment(PaymentCompleteRequest request){
            IamportClient iamportClient = new IamportClient(impKey, impSecret);

            String impUid = request.getImpUid();
            String merchantUid = request.getMerchantId();
            Payment importPayment;


            //1. 포트원 접근 및 포트원에서 결제 정보 조회
            try {
                IamportResponse<Payment> paymentResponse = iamportClient.paymentByImpUid(impUid);
                importPayment = paymentResponse.getResponse();
            }catch (Exception e) {
                throw new CustomException(ErrorCode.PAYMENT_VALID_IMP_NOT_FOUND);
            }//고치기


            //2. 사용된 merchantUid 로 db 금액 조회후 결제 금액과 비교
            Order order = orderRepository.findByMerchantUid(merchantUid);
            if(order == null){
                throw new CustomException(ErrorCode.MERCHANT_NOT_FOUND_ERROR);
            }

            BigDecimal orderPrice = order.getPrice();
            if(orderPrice.compareTo(importPayment.getAmount())!=0){
                throw new CustomException(ErrorCode.INVALID_PAYMENT_AMOUNT);
            }

            //3. 완료된 결제 정보 PaymentEntity 에 저장
            PaymentEntity paymentEntity = PaymentEntity.builder()
                    .impUid(request.getImpUid())
                    .merchantUid(importPayment.getMerchantUid())
                    .price(importPayment.getAmount())
                    .status(PaymentStatusType.valueOf(importPayment.getStatus()))
                    .build();

            paymentRepository.save(paymentEntity);


            //4. 포트원에서 반환해준 결제 정보의 상태를 가져와서, 상태의 값에 따라 주문 상태 업데이트
            switch(importPayment.getStatus()){
                case "ready":
                    orderService.updateOrderStatus(merchantUid, OrderStatus.PAY_READY);
                    break;
                case "paid" :
                    orderService.updateOrderStatus(merchantUid, OrderStatus.PAID);
                    break;
                default :
                    throw new CustomException(ErrorCode.PAYMENT_STATUS_NOT_FOUND);
            }

            return PaymentResponse.builder()
                    .impUid(importPayment.getImpUid())
                    .merchantUid(importPayment.getMerchantUid())
                    .price(importPayment.getAmount().longValue())
                    .status(importPayment.getStatus())
                    .build();
    }




}