package com.team2.mosoo_backend.order.service;


import com.team2.mosoo_backend.exception.CustomException;
import com.team2.mosoo_backend.exception.ErrorCode;
import com.team2.mosoo_backend.order.dto.CreateOrderRequestDto;
import com.team2.mosoo_backend.order.dto.OrderResponseDto;
import com.team2.mosoo_backend.order.entity.Order;
import com.team2.mosoo_backend.order.mapper.OrderMapper;
import com.team2.mosoo_backend.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;


    public OrderResponseDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        return orderMapper.orderToOrderResponseDto(order);
    }

    public OrderResponseDto createOrder(CreateOrderRequestDto createOrderRequestDto) {
        Order order = orderMapper.createOrderRequestDtoToOrder(createOrderRequestDto);

        return orderMapper.orderToOrderResponseDto(orderRepository.save(order));
    }
}
