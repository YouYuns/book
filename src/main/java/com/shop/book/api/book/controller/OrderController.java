package com.shop.book.api.book.controller;


import com.shop.book.api.book.service.OrderService;
import com.shop.book.global.config.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class OrderController {


    private final OrderService orderService;


    /**
     *
     * @param user
     * @return  MY ORDER LIST
     */
    @GetMapping("/mypage/orders")
    public String orders(@AuthenticationPrincipal CustomUserDetails user) {
        orderService.findUserOrderProcess(user);
        return null;
    }

}
