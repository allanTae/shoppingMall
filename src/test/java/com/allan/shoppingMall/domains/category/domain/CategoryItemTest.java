package com.allan.shoppingMall.domains.category.domain;

import com.allan.shoppingMall.common.config.jpa.auditing.JpaAuditingConfig;
import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.category.CategoryNotFoundException;
import com.allan.shoppingMall.domains.item.domain.clothes.Clothes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@DataJpaTest(
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JpaAuditingConfig.class
        )
)
@WithMockUser
public class CategoryItemTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    CategoryItemRepository categoryItemRepository;

    /**
     * 상품 도메인의 아이디와 카테고리 코드 값으로 카테고리 상품 도메인을 조회하는 테스트 입니다.
     */
    @Test
    public void 단일_카테고리_상품_조회테스트() throws Exception {
        //given
        Category TEST_CATEGORY_SHOP = Category.builder()
                .name("testCategoryShop")
                .branch("shop")
                .depth(0)
                .categoryCode(CategoryCode.SHOP)
                .build();

        Category TEST_CATEGORY_TOP = Category.builder()
                .name("testCategoryTop")
                .branch("shop")
                .depth(1)
                .categoryCode(CategoryCode.CLOTHES)
                .build();

        Category TEST_CATEGORY_ACCESSORY = Category.builder()
                .branch("shop")
                .name("testCategoryAccesory")
                .depth(1)
                .categoryCode(CategoryCode.ACCESSORY)
                .build();

        Clothes TEST_CLOTHES_1 = Clothes.builder()
                .name("testName")
                .engName("testEngName")
                .price(1000l)
                .build();
        TEST_CLOTHES_1.changeCategoryItems(List.of(new CategoryItem(TEST_CATEGORY_TOP)));

        Clothes TEST_CLOTHES_2 = Clothes.builder()
                .name("testName2")
                .engName("testEngName2")
                .price(2000l)
                .build();
        TEST_CLOTHES_2.changeCategoryItems(List.of(new CategoryItem(TEST_CATEGORY_TOP)));

        testEntityManager.persist(TEST_CATEGORY_SHOP);
        testEntityManager.persist(TEST_CATEGORY_TOP);
        testEntityManager.persist(TEST_CATEGORY_ACCESSORY);
        testEntityManager.persist(TEST_CLOTHES_1);
        testEntityManager.persist(TEST_CLOTHES_2);
        testEntityManager.clear();

        //when
        CategoryItem categoryItem = categoryItemRepository.getCategoryItem(List.of(CategoryCode.CLOTHES), TEST_CLOTHES_1.getItemId()).orElseThrow(() ->
                new CategoryNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        //then
        assertThat(categoryItem.getItem().getItemId(), is(TEST_CLOTHES_1.getItemId()));
    }
}
