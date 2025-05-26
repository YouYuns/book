package com.shop.book.api.book.service;

import com.shop.book.domain.book.dto.BookDetailDto;
import com.shop.book.domain.book.dto.BookDto;
import com.shop.book.domain.book.dto.BookSearchResponseDto;
import org.springframework.ui.Model;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BookService {
    List<BookDto> searchBooks(String query, Integer display);


    BookDetailDto getBookDetailByIsbn(String isbn);
}
