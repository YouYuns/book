package com.shop.book.domain.common.domain;

import com.shop.book.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;



    @Getter
    @Setter
    @Entity
    @Table(name = "images")
    public class Image {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String fileName;

        @Column(nullable = false)
        private String fileUrl;

        @Column(nullable = false)
        private String fileType;

        @Column(nullable = false)
        private Long fileSize;

        @OneToOne(mappedBy = "profileImage")
        private Member member;
}
