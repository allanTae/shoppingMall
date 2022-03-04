package com.allan.shoppingMall.domains.item.domain;

import com.allan.shoppingMall.common.config.jpa.auditing.JpaAuditingConfig;
import com.allan.shoppingMall.domains.category.domain.Category;
import com.allan.shoppingMall.domains.category.domain.CategoryCode;
import com.allan.shoppingMall.domains.category.domain.CategoryItem;
import com.allan.shoppingMall.domains.item.domain.item.Color;
import com.allan.shoppingMall.domains.item.domain.item.Item;
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

@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = JpaAuditingConfig.class
))
@WithMockUser
public class ItemTest {

    @Autowired
    TestEntityManager testEntityManager;

    /**
     * 동일한 카테고리 상품이 등록 여부를 확인하는 테스트 입니다.
     */
    @Test
    public void 동일_카테고리_등록_테스트() throws Exception {
        //given
        Category TEST_CATEGORY = Category.builder()
                .name("testCategory1")
                .categoryCode(CategoryCode.CLOTHES)
                .branch("testBranch")
                .depth(1)
                .build();

        Category TEST_CATEGORY_2 = Category.builder()
                .name("testCategory1-1")
                .categoryCode(CategoryCode.CLOTHES)
                .branch("testBranch")
                .depth(2)
                .build();

        testEntityManager.persist(TEST_CATEGORY);
        testEntityManager.persist(TEST_CATEGORY_2);
        testEntityManager.flush();

        Item TEST_ITEM = new Item("testItemName", 10000l, Color.RED);

        TEST_ITEM.changeCategoryItems(List.of(new CategoryItem(TEST_CATEGORY)));
        assertThat(TEST_ITEM.getCategoryItems().size(), is(1));

        //when
        TEST_ITEM.changeCategoryItems(List.of(new CategoryItem(TEST_CATEGORY), new CategoryItem(TEST_CATEGORY_2)));

        //then
        assertThat(TEST_ITEM.getCategoryItems().size(), is(2));
        assertThat(TEST_ITEM.getCategoryItems().get(0).getCategory().getName(), is(TEST_CATEGORY.getName()));
        assertThat(TEST_ITEM.getCategoryItems().get(1).getCategory().getName(), is(TEST_CATEGORY_2.getName()));
    }
}
