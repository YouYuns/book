package com.shop.book.api.member.controller;

import com.shop.book.api.member.service.BookService;
import com.shop.book.domain.book.dto.BookSearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookController {

    private static final String path = "/book";

    private final BookService bookService;

    //검색 정보
    @GetMapping("/search")
    public List<BookSearchResponseDto> search(@RequestParam("query") String query, Model model) {
        return bookService.searchBooks(query, model);
    }
}
