package com.shop.book.api.member.service.impl;

import com.shop.book.api.member.service.BookService;
import com.shop.book.domain.book.dto.BookSearchResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;

import java.util.List;

public class BookServiceImpl implements BookService {

    @Value("${naver.openapi.clientId}")
    private String clientId;

    @Value("${naver.openapi.clientSecret}")
    private String clientSecret;

    private static final String NAVER_BOOK_API_URL = "https://openapi.naver.com/v1/search/book.json";


    @Override
    public List<BookSearchResponseDto> searchBooks(String query, Model model) {
        return null;
    }
}
