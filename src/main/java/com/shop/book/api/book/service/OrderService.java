package com.shop.book.api.book.service;

import com.shop.book.global.config.security.CustomUserDetails;

public interface OrderService {
    void findUserOrderProcess(CustomUserDetails user);
}
