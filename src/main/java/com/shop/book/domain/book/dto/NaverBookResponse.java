package com.shop.book.domain.book.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverBookResponse {
    private List<NaverBookItem> items;  // Book Item
    private Integer total;              // 전체 카운트
}
