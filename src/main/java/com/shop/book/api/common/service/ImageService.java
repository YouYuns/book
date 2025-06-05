package com.shop.book.api.common.service;

import com.shop.book.domain.common.entity.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Image uploadImage(MultipartFile file);
}
