package com.shop.book.domain.book.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_detail")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderDetailNum; //주문상세번호

    @ManyToOne // FK 단방향
    @JoinColumn(name = "merchantUid", nullable = false)
    private Order order; // 주문번호 fk

    @ManyToOne // FK 단방향
    @JoinColumn(name = "bookId", nullable = false)
    private Book book ; // 도서번호 fk

    private long orderCnt; //주문수량

}
