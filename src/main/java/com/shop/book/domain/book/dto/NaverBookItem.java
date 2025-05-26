package com.shop.book.domain.book.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverBookItem {
    private String title;       // 제목
    private String author;      // 작가
    private String publisher;   // 출판사
    private String pubdate;     // 출판날짜
    private String description; // 설명
    private String isbn;        // ISBN
    private String image;       // 이미지 URL
    private String link;        // 상세 정보 URL
    private String discount;    // 재고
}
