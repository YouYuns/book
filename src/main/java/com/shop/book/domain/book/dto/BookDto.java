package com.shop.book.domain.book.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BookDto {
    private String title;           // 책 제목
    private String author;          // 저자
    private String publisher;       // 출판사
    private String pubdate;         // 출간일
    private String description;     // 책 소개
    private String isbn;            // ISBN
    private String image;           // 표지 이미지 URL
    private String link;            // 상세 정보 URL
    private String discount;        // 판매가
    private String category;        // 카테고리 정보 (선택적)
    private double averageRating;   // 평점
    private int reviewCount;        // 리뷰 개수
}
