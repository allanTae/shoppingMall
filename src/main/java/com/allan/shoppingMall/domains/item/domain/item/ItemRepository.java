package com.allan.shoppingMall.domains.item.domain.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * 특정 카테고리의 상품 페이징 정보를 반환하는 메소드.
     * @param categoryId 카테고리 도메인 아이디.
     * @param pageable 페이징 정보.
     */
    @Query("select distinct i from Item i join i.categoryItems c where c.category.categoryId = :categoryId")
    public Page<Item> getItems(@Param("categoryId") Long categoryId, Pageable pageable);

    /**
     * 특정 카테고리 아이디 리스트로 상품 페이징 정보를 반환하는 메소드.
     * @param categoryIds 카테고리 도메인 아이디.
     * @param pageable 페이징 정보.
     */
    @Query("select distinct i from Item i join i.categoryItems c where c.category.categoryId in :categoryIds")
    public Page<Item> getItemsByCategoryIds(@Param("categoryIds") List<Long> categoryIds, Pageable pageable);
}
