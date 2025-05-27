package com.shop.book.api.book.controller;

import com.shop.book.api.book.service.BookService;
import com.shop.book.domain.book.dto.BookDetailDto;
import com.shop.book.domain.book.dto.BookDto;
import com.shop.book.domain.book.dto.BookSearchResponseDto;
import com.shop.book.domain.book.dto.BookSearchXmlResponse;
import com.shop.book.global.error.ErrorCode;
import com.shop.book.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookController {

    private static final String path = "/book";

    private final BookService bookService;

    //검색 정보
    @GetMapping("/search")
    public List<BookDto> search(@RequestParam("query") String query,
                                @RequestParam("display")Integer display)  {
        return bookService.searchBooks(query, display);
    }

    //도서 상세 정보
    @GetMapping("/detail/{isbn}")
    public BookSearchXmlResponse.Item detail(@PathVariable("isbn") String isbn)  {
        return bookService.getBookDetailByIsbn(isbn);
    }
}
