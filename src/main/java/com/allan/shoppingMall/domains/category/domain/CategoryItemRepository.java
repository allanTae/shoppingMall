package com.allan.shoppingMall.domains.category.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryItemRepository extends JpaRepository<CategoryItem, Long> {


    /**
     * item과 CategoryCode 로 카테고리 상품 정보를 반환하는 메소드.
     * @param
     * @return
     */
    @Query("select ci from CategoryItem ci join fetch ci.category where ci.category.categoryCode in :categoryCodes AND ci.item.itemId = :itemId")
    public Optional<CategoryItem> getCategoryItem(@Param("categoryCodes") List<CategoryCode> categoryCodes, @Param("itemId") Long itemId);
}
