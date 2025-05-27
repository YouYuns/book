package com.shop.book.domain.book.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@XmlRootElement(name = "rss")
public class BookSearchXmlResponse {
    @Getter(onMethod_ = @XmlElement(name = "channel"))
    @Setter
    private Channel channel;

    @Getter
    @Setter
    public static class Channel {
        @Getter(onMethod_ = @XmlElement(name = "item"))
        @Setter
        private List<Item> items;
    }

    @Getter(onMethod_ = @XmlElement)
    @Setter
    public static class Item {
        private String title;
        private String link;
        private String image;
        private String author;
        private String discount;
        private String publisher;
        private String pubdate;
        private String isbn;
        private String description;

        @XmlTransient // XML 파싱에서 제외
        private double averageRating;

        @XmlTransient // XML 파싱에서 제외
        private int reviewCount;
    }
}
