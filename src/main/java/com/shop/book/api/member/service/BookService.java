package com.shop.book.api.member.service;

import com.shop.book.domain.book.dto.BookSearchResponseDto;
import org.springframework.ui.Model;

import java.util.List;

public interface BookService {
    List<BookSearchResponseDto> searchBooks(String query, Model model);
}
