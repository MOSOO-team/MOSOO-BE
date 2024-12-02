package com.team2.mosoo_backend.order.mapper;

import com.team2.mosoo_backend.order.dto.CreateOrderRequestDto;
import com.team2.mosoo_backend.order.dto.OrderResponseDto;
import com.team2.mosoo_backend.order.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponseDto orderToOrderResponseDto(Order order);

    Order createOrderRequestDtoToOrder(CreateOrderRequestDto createOrderRequestDto);
}
