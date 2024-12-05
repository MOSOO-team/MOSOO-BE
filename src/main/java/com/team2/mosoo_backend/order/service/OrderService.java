package com.team2.mosoo_backend.order.service;


import com.team2.mosoo_backend.chatting.entity.ChatRoom;
import com.team2.mosoo_backend.chatting.repository.ChatRoomRepository;
import com.team2.mosoo_backend.exception.CustomException;
import com.team2.mosoo_backend.exception.ErrorCode;
import com.team2.mosoo_backend.order.dto.CreateOrderRequestDto;
import com.team2.mosoo_backend.order.dto.OrderListResponseDto;
import com.team2.mosoo_backend.order.dto.OrderResponseDto;
import com.team2.mosoo_backend.order.dto.UpdateOrderRequestDto;
import com.team2.mosoo_backend.order.entity.Order;
import com.team2.mosoo_backend.order.mapper.OrderMapper;
import com.team2.mosoo_backend.order.repository.OrderRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ChatRoomRepository chatRoomRepository;


    public OrderListResponseDto getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponseDto> orderResponseDtoList = new ArrayList<>();

        for(Order order : orders) {
            orderResponseDtoList.add(orderMapper.orderToOrderResponseDto(order));
        }

        return new OrderListResponseDto(orderResponseDtoList);

    }

    public OrderResponseDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        return orderMapper.orderToOrderResponseDto(order);
    }


    public OrderResponseDto createOrder(Long chatroomId, CreateOrderRequestDto createOrderRequestDto) {
        Order order = orderMapper.createOrderRequestDtoToOrder(createOrderRequestDto);
        ChatRoom chatRoom = chatRoomRepository.findById(chatroomId).orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        // 연관 매핑
        order.setMapping(chatRoom);

        return orderMapper.orderToOrderResponseDto(orderRepository.save(order));
    }


    public OrderResponseDto updateOrder(Long orderId, UpdateOrderRequestDto updateOrderRequestDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        if(updateOrderRequestDto.getPrice() != 0){
            order.setPrice(BigDecimal.valueOf(updateOrderRequestDto.getPrice()));
        }
        if(updateOrderRequestDto.getStatus() != null){
            order.setStatus(updateOrderRequestDto.getStatus());
        }

        Order updatedOrder = orderRepository.save(order);
        return orderMapper.orderToOrderResponseDto(updatedOrder);
    }

    public void deleteOrder(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new CustomException(ErrorCode.ORDER_NOT_FOUND);
        }
        orderRepository.deleteById(orderId);
    }

    public void updateOrderStatus(String merchantUid, String orderStatus){
        Order order = orderRepository.findByMerchantUid(merchantUid);
        order.setStatus(orderStatus);
        orderRepository.save(order);
    }


}
