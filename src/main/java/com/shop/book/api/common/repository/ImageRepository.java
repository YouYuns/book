package com.shop.book.api.common.repository;

import com.shop.book.domain.common.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {
}
