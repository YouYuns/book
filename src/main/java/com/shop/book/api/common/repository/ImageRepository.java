package com.shop.book.api.common.repository;

import com.shop.book.domain.common.domain.Image;
import com.shop.book.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {
}
