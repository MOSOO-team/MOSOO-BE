package com.team2.mosoo_backend.order.controller;

import com.team2.mosoo_backend.order.dto.OrderDetailsResponseDto;
import com.team2.mosoo_backend.order.dto.OrderListResponseDto;
import com.team2.mosoo_backend.order.dto.OrderResponseDto;
import com.team2.mosoo_backend.order.dto.OrderStatusUpdateResponseDto;
import com.team2.mosoo_backend.order.dto.UpdateOrderRequestDto;
import com.team2.mosoo_backend.order.entity.OrderStatus;
import com.team2.mosoo_backend.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;


    //조회
    @Operation(summary = "주문서 조회", description = "사용자의 결제된 주문 전체 내역을 조회합니다.")
    @GetMapping
    public ResponseEntity<OrderListResponseDto> getAllOrders(
            @RequestParam(value = "orderStatus") OrderStatus orderStatus
    ) {
        OrderListResponseDto responseDto = orderService.getAllOrders(orderStatus);

        return ResponseEntity.status(200).body(responseDto);
    }

    @Operation(summary = "주문서 생성", description = "chatRoomID를 받아 주문서를 저장한다.")
    @PostMapping
    public ResponseEntity<OrderDetailsResponseDto> createOrder(
            @RequestParam(value = "chatroomId") Long chatroomId) {
        OrderDetailsResponseDto orderDetailsResponseDto = orderService.createOrder(chatroomId);

        return ResponseEntity.status(201).body(orderDetailsResponseDto);
    }

    @Operation(summary = "주문상태 update", description = "서비스완료 버튼을 누르는경우 상태가 이용완료 상태로 변경됩니다.")
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderStatusUpdateResponseDto> updateOrder(
            @PathVariable("orderId") Long orderId
    ) {
        OrderStatusUpdateResponseDto orderStatusUpdateResponse = orderService.updateOrder(orderId);
        return ResponseEntity.status(201).body(orderStatusUpdateResponse);
    }



}
