package com.shop.book.domain.book.entity;


import com.shop.book.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order")
public class Order {

    @Id
    private Long merchantUid; //주문번호

    @ManyToOne // FK 단방향
    @JoinColumn(name = "userId", nullable = false)
    private Member member; // 사용자ID fk

    @CreationTimestamp
    @Column(columnDefinition = "timestamp")
    private LocalDateTime orderDate; //주문날짜

    @Column(nullable = true)
    private long paidAmount; //총 결제금액

    private String cardName; //카드사 이름

    @Column(name = "card_number", nullable = true)
    private int cardNumber; //결제 카드번호

    @Column(nullable=true)
    @ColumnDefault("0")
    private String orderStatus; //주문상태
    //0:주문대기, 1:주문완료, 2:배송중, 3:배송완료, 4:환불, 5:교환, 6:취소

    private String field; //환불사유

    private String exchangeReason; //교환사유
}
