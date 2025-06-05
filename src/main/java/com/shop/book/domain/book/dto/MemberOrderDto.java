package com.shop.book.domain.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberOrderDto {

    private Long merchantUid;
    private LocalDateTime orderDate;
    private Integer orderStatus;
    private Integer paidAmount;
    private String BookImg;
    private String BookName;
}
