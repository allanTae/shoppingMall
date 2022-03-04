package com.allan.shoppingMall.domains.category.domain;

import com.allan.shoppingMall.common.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 카테고리 정보를 저장하기 위한 도메인 클래스입니다.
 * (Item, CategoryItem 도메인 클래스를 참고 해 주세요.)
 */
@Entity
@Table(name = "categories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Category extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private List<Category> childCategory = new ArrayList<>();

    // 카테고리 그룹정보를 저장하기위한 필드(카테고리 조회용).
    @Column(nullable = false)
    private String branch;

    // 카테고리 테이블의 depth 정보 필드.
    @Column(nullable = false)
    private Integer depth;

    // 카테고리 구분 필드(상품 도메인 등록시, 상품 도메인 종류 구분용 예) Clothes, Accessory 구분).
    @Enumerated(value = EnumType.STRING)
    private CategoryCode categoryCode;

    @Builder
    public Category(String name, Category parentCategory, String branch, Integer depth, CategoryCode categoryCode) {
        this.name = name;
        this.parentCategory = parentCategory;
        this.branch = branch;
        this.depth = depth;
        this.categoryCode = categoryCode;
    }

    public void changeName(String name){
        this.name= name;
    }

}
