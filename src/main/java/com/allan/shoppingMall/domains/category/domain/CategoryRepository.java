package com.allan.shoppingMall.domains.category.domain;

import com.allan.shoppingMall.domains.item.domain.item.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * @param branch 카테고리 그룹.
     * @param name 카테고리 이름.
     */
    Optional<Category> findByBranchAndName(String branch, String name);

    /**
     * 카테고리 동일 그룹내 같은 이름을 가진 카테고리 존재 유무를 확인하는 메소드.
     * @param branch 카테고리 그룹.
     * @param name 카테고리 이름.
     */
    Boolean existsByBranchAndName(String branch, String name);


    /**
     * @param categoryId 카테고리 id.
     */
    @Query("select c from Category c join fetch c.parentCategory where c.categoryId = :categoryId")
    Optional<Category> getCategory(@Param("categoryId") Long categoryId);
}
