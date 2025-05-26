package com.shop.book.domain.category.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; //카테고리 번호

    @ManyToOne
    @JoinColumn(name = "id2")//셀프조인
    private Category parent; //부모번호

    @Column(nullable=false)//not null
    private String name; //이름
}
