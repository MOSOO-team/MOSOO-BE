package com.team2.mosoo_backend.payment.repository;

import com.siot.IamportRestClient.response.Payment;
import com.team2.mosoo_backend.payment.entity.PaymentEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {


    Optional<PaymentEntity> findPaymentEntityByOrderId(Long orderId);
}
