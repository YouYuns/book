package com.shop.book.api.common.service;

import com.shop.book.domain.common.domain.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Image uploadImage(MultipartFile file);
}
