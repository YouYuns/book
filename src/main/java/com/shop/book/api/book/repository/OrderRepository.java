package com.shop.book.api.book.repository;

import com.shop.book.domain.book.dto.MemberOrderDto;
import com.shop.book.domain.book.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<MemberOrderDto> findByMemberIdOrders(Long memberId);
}
