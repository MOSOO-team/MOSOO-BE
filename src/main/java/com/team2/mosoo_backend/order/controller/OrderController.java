package com.team2.mosoo_backend.order.controller;

import com.team2.mosoo_backend.order.dto.CreateOrderRequestDto;
import com.team2.mosoo_backend.order.dto.OrderListResponseDto;
import com.team2.mosoo_backend.order.dto.OrderResponseDto;
import com.team2.mosoo_backend.order.dto.UpdateOrderRequestDto;
import com.team2.mosoo_backend.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;


    @GetMapping
    public ResponseEntity<OrderListResponseDto> getALlOrders() {
        OrderListResponseDto responseDto = orderService.getAllOrders();

        return ResponseEntity.status(200).body(responseDto);
    }

    @GetMapping("/{ordersId}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable("ordersId") Long orderId) {
        OrderResponseDto orderResponseDto = orderService.getOrderById(orderId);

        return ResponseEntity.status(200).body(orderResponseDto);
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody CreateOrderRequestDto createOrderRequestDto) {
        OrderResponseDto orderResponseDto = orderService.createOrder(createOrderRequestDto);

        return ResponseEntity.status(201).body(orderResponseDto);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrder(
            @PathVariable("orderId") Long orderId,
            @RequestBody UpdateOrderRequestDto updateOrderRequestDto
    ) {
        OrderResponseDto updatedOrderResponse = orderService.updateOrder(orderId, updateOrderRequestDto);
        return ResponseEntity.status(201).body(updatedOrderResponse);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("orderId") Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.status(204).build();
    }


}
