package com.shop.book.api.book.service.impl;

import com.shop.book.api.book.repository.OrderRepository;
import com.shop.book.api.book.service.OrderService;
import com.shop.book.api.member.repository.MemberRepository;
import com.shop.book.domain.book.dto.MemberOrderDto;
import com.shop.book.global.config.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public void findUserOrderProcess(CustomUserDetails user) {
       Long memberId =  user.getUserId();

       //내가 주문한 목록
        List<MemberOrderDto> memberOrderDtos = orderRepository.findByMemberIdOrders(memberId);




    }
}
