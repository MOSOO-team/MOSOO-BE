package com.team2.mosoo_backend.order.service;


import com.team2.mosoo_backend.bid.dto.BidResponseDto;
import com.team2.mosoo_backend.bid.mapper.BidMapper;
import com.team2.mosoo_backend.chatting.entity.ChatRoom;
import com.team2.mosoo_backend.chatting.repository.ChatRoomRepository;
import com.team2.mosoo_backend.config.SecurityUtil;
import com.team2.mosoo_backend.exception.CustomException;
import com.team2.mosoo_backend.exception.ErrorCode;
import com.team2.mosoo_backend.order.dto.OrderDetailsResponseDto;
import com.team2.mosoo_backend.order.dto.OrderListResponseDto;
import com.team2.mosoo_backend.order.dto.OrderResponseDto;
import com.team2.mosoo_backend.order.dto.OrderStatusUpdateResponseDto;
import com.team2.mosoo_backend.order.entity.Order;
import com.team2.mosoo_backend.order.entity.OrderStatus;
import com.team2.mosoo_backend.order.mapper.OrderMapper;
import com.team2.mosoo_backend.order.repository.OrderRepository;
import com.team2.mosoo_backend.payment.entity.PaymentEntity;
import com.team2.mosoo_backend.payment.repository.PaymentRepository;
import com.team2.mosoo_backend.post.dto.PostResponseDto;
import com.team2.mosoo_backend.post.mapper.PostMapper;
import com.team2.mosoo_backend.user.dto.GosuResponseDto;
import com.team2.mosoo_backend.user.entity.Gosu;
import com.team2.mosoo_backend.user.entity.UserInfo;
import com.team2.mosoo_backend.user.repository.GosuRepository;
import com.team2.mosoo_backend.user.repository.UserInfoRepository;
import com.team2.mosoo_backend.user.repository.UserRepository;
import java.math.BigDecimal;
import java.util.UUID;
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
    private final PostMapper postMapper;
    private final BidMapper bidMapper;
    private final UserInfoRepository userInfoRepository;
    private final GosuRepository gosuRepository;
    private final SecurityUtil securityUtil;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;


    //todo : 리팩토링 하기..
    public OrderListResponseDto getAllOrders(OrderStatus status) {

        Long userId = getAuthenticatedMemberId();
        List<Order> orders = orderRepository.findOrdersByChatRoomUserIdAndOrderStatus(userId, status); // 사용자가 결제한 주문 조회
        List<OrderResponseDto> orderResponseDtoList = new ArrayList<>();

        for(Order order : orders) {
            ChatRoom chatRoom = order.getChatRoom();
            String workDate;
            if(chatRoom.getBid() != null){
                workDate = chatRoom.getBid().getDate().toString();
            }else{
                workDate = chatRoom.getPost().getDuration();
            }

            PaymentEntity paymentEntity = paymentRepository.findPaymentEntityByOrderId(order.getId()).orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

            UserInfo byUsersId = userInfoRepository.findByUsersId(chatRoom.getGosuId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
            Gosu gosu = gosuRepository.findByUserInfoId(byUsersId.getId()).get(0);
            String gosuName = gosu.getBusinessName();

            orderResponseDtoList.add(new OrderResponseDto(workDate, order.getId(), order.getPrice(), paymentEntity.getCreatedAt(), gosuName, chatRoom.getPost().getId()));
        }

        return new OrderListResponseDto(orderResponseDtoList);

    }

    public OrderDetailsResponseDto createOrder(Long chatroomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatroomId).orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        Order order = Order.builder()
                .merchantUid(UUID.randomUUID().toString())
                .price(BigDecimal.valueOf(chatRoom.getPrice()))
                .orderStatus(OrderStatus.WAITING_PAYMENT)
                .build();

        order.setChatRoom(chatRoom);

        orderRepository.save(order);

        PostResponseDto postResponseDto = postMapper.postToPostResponseDto(chatRoom.getPost());
        BidResponseDto bidResponseDto = bidMapper.bidToBidResponseDto(chatRoom.getBid());

        UserInfo byUsersId = userInfoRepository.findByUsersId(chatRoom.getGosuId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Gosu gosu = gosuRepository.findByUserInfoId(byUsersId.getId()).get(0);
        GosuResponseDto gosuResponseDto = new GosuResponseDto(gosu.getGosuInfoAddress(), gosu.getBusinessName());

        return new OrderDetailsResponseDto(postResponseDto, bidResponseDto, gosuResponseDto, order.getPrice(), order.getMerchantUid());
    }



    public OrderStatusUpdateResponseDto updateOrder(Long orderId){

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        order.setOrderStatus(OrderStatus.SERVICE_COMPLETED);
        orderRepository.save(order);

        return new OrderStatusUpdateResponseDto(order.getId());

    }


    public void updateOrderStatus(String merchantUid, OrderStatus orderStatus){
        Order order = orderRepository.findByMerchantUid(merchantUid);
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
    }

    private Long getAuthenticatedMemberId() {
        try {
            return securityUtil.getCurrentMemberId();
        } catch (RuntimeException e) {
            throw new CustomException(ErrorCode.USER_NOT_AUTHORIZED);
        }
    }


}
