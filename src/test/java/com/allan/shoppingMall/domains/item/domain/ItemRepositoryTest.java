package com.allan.shoppingMall.domains.item.domain;

import com.allan.shoppingMall.common.config.jpa.auditing.JpaAuditingConfig;
import com.allan.shoppingMall.domains.category.domain.Category;
import com.allan.shoppingMall.domains.category.domain.CategoryCode;
import com.allan.shoppingMall.domains.category.domain.CategoryItem;
import com.allan.shoppingMall.domains.item.domain.clothes.Clothes;
import com.allan.shoppingMall.domains.item.domain.item.Color;
import com.allan.shoppingMall.domains.item.domain.item.Item;
import com.allan.shoppingMall.domains.item.domain.item.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;

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
@Rollback(value = true)
public class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    TestEntityManager testEntityManager;

    Category TEST_CATEGORY_SHOP;
    Category TEST_CATEGORY_TOP;
    Category TEST_CATEGORY_BOTTOM;
    Category TEST_CATEGORY_ACCESSORY;
    Category TEST_CATEGORY_GOODS;
    Category TEST_CATEGORY_SHIRT;
    Category TEST_CATEGORY_KNIT;

    @BeforeEach
    public void setUp(){
        TEST_CATEGORY_SHOP = Category.builder()
                .name("shop")
                .branch("shop")
                .depth(1)
                .categoryCode(CategoryCode.CLOTHES)
                .build();

        TEST_CATEGORY_TOP = Category.builder()
                .name("top")
                .branch("shop")
                .depth(2)
                .parentCategory(TEST_CATEGORY_SHOP)
                .categoryCode(CategoryCode.CLOTHES)
                .build();

        TEST_CATEGORY_BOTTOM =Category.builder()
                .name("bottom")
                .branch("shop")
                .depth(2)
                .parentCategory(TEST_CATEGORY_SHOP)
                .categoryCode(CategoryCode.CLOTHES)
                .build();

        TEST_CATEGORY_ACCESSORY = Category.builder()
                .name("accessory")
                .branch("shop")
                .parentCategory(TEST_CATEGORY_SHOP)
                .categoryCode(CategoryCode.ACCESSORY)
                .depth(1)
                .build();

        TEST_CATEGORY_GOODS =Category.builder()
                .name("goods")
                .branch("shop")
                .parentCategory(TEST_CATEGORY_ACCESSORY)
                .categoryCode(CategoryCode.ACCESSORY)
                .depth(2)
                .build();

        TEST_CATEGORY_SHIRT = Category.builder()
                .name("shirt")
                .branch("shop")
                .depth(3)
                .parentCategory(TEST_CATEGORY_TOP)
                .categoryCode(CategoryCode.CLOTHES)
                .build();

        TEST_CATEGORY_KNIT = Category.builder()
                .name("knit")
                .branch("shop")
                .depth(3)
                .parentCategory(TEST_CATEGORY_TOP)
                .categoryCode(CategoryCode.CLOTHES)
                .build();

        testEntityManager.persist(TEST_CATEGORY_SHOP);
        testEntityManager.persist(TEST_CATEGORY_TOP);
        testEntityManager.persist(TEST_CATEGORY_BOTTOM);
        testEntityManager.persist(TEST_CATEGORY_ACCESSORY);
        testEntityManager.persist(TEST_CATEGORY_GOODS);
        testEntityManager.persist(TEST_CATEGORY_SHIRT);
        testEntityManager.persist(TEST_CATEGORY_KNIT);
    }

    @Test
    public void 상품_페이징_조회_테스트() throws Exception {
        //given
        Clothes TEST_CLOTHES_1 = createClothes("testName1", "testEngName1", TEST_CATEGORY_SHOP);
        Clothes TEST_CLOTHES_2 = createClothes("testName2", "testEngName2", TEST_CATEGORY_TOP);
        Clothes TEST_CLOTHES_3 = createClothes("testName3", "testEngName3", TEST_CATEGORY_BOTTOM);
        Clothes TEST_CLOTHES_4 = createClothes("testName4", "testEngName4", TEST_CATEGORY_ACCESSORY);
        Clothes TEST_CLOTHES_5 = createClothes("testName5", "testEngName5", TEST_CATEGORY_SHOP);
        Clothes TEST_CLOTHES_6 = createClothes("testName6", "testEngName6", TEST_CATEGORY_GOODS);
        Clothes TEST_CLOTHES_7 = createClothes("testName7", "testEngName7", TEST_CATEGORY_SHOP);

        //when
        testEntityManager.persist(TEST_CLOTHES_1);
        testEntityManager.persist(TEST_CLOTHES_2);
        testEntityManager.persist(TEST_CLOTHES_3);
        testEntityManager.persist(TEST_CLOTHES_4);
        testEntityManager.persist(TEST_CLOTHES_5);
        testEntityManager.persist(TEST_CLOTHES_6);
        testEntityManager.persist(TEST_CLOTHES_7);

        Page<Item> page = itemRepository.getItems(TEST_CATEGORY_SHOP.getCategoryId(), PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, "createdDate")));

        //then
        List<Item> items = page.getContent();
        assertThat(items.size(), is(3)); // 3개만 페이징 했는가.?
        assertThat(page.getNumber(), is(0)); // 첫페이지가 맞는가.? (jpa paging 은 0번 페이지부터 시작)
        assertThat(page.getTotalPages(), is(1)); // 총 1페이지가 맞는가.?
        if(items.get(0) instanceof Clothes) {
            assertThat(items.get(0).getItemId(), is(TEST_CLOTHES_1.getItemId()));
            assertThat(items.get(0).getName(), is(TEST_CLOTHES_1.getName()));
            assertThat(((Clothes) items.get(0)).getEngName(), is(TEST_CLOTHES_1.getEngName()));
        }

        if(items.get(1) instanceof Clothes) {
            assertThat(items.get(1).getItemId(), is(TEST_CLOTHES_5.getItemId()));
            assertThat(items.get(1).getName(), is(TEST_CLOTHES_5.getName()));
            assertThat(((Clothes) items.get(1)).getEngName(), is(TEST_CLOTHES_5.getEngName()));
        }

        if(items.get(2) instanceof Clothes) {
            assertThat(items.get(2).getItemId(), is(TEST_CLOTHES_7.getItemId()));
            assertThat(items.get(2).getName(), is(TEST_CLOTHES_7.getName()));
            assertThat(((Clothes) items.get(2)).getEngName(), is(TEST_CLOTHES_7.getEngName()));
        }

    }

    /**
     * 특정 카테고리에 해당하는 상품 도메인 페이징하는 테스트입니다.
     */
    @Test
    public void 상품_페이징_조회_테스트_2() throws Exception {
        //given
        Clothes TEST_CLOTHES_1 = createClothes("testName1", "testEngName1", TEST_CATEGORY_SHOP);
        Clothes TEST_CLOTHES_2 = createClothes("testName2", "testEngName2", TEST_CATEGORY_TOP);
        Clothes TEST_CLOTHES_3 = createClothes("testName3", "testEngName3", TEST_CATEGORY_BOTTOM);
        Clothes TEST_CLOTHES_4 = createClothes("testName4", "testEngName4", TEST_CATEGORY_ACCESSORY);
        Clothes TEST_CLOTHES_5 = createClothes("testName5", "testEngName5", TEST_CATEGORY_SHIRT);
        Clothes TEST_CLOTHES_6 = createClothes("testName6", "testEngName6", TEST_CATEGORY_GOODS);
        Clothes TEST_CLOTHES_7 = createClothes("testName7", "testEngName7", TEST_CATEGORY_KNIT);
        Clothes TEST_CLOTHES_8 = createClothes("testName8", "testEngName8", TEST_CATEGORY_KNIT);
        Clothes TEST_CLOTHES_9 = createClothes("testName9", "testEngName9", TEST_CATEGORY_BOTTOM);
        Clothes TEST_CLOTHES_10 = createClothes("testName10", "testEngName10", TEST_CATEGORY_TOP);
        Clothes TEST_CLOTHES_11 = createClothes("testName11", "testEngName11", TEST_CATEGORY_TOP);
        Clothes TEST_CLOTHES_12 = createClothes("testName12", "testEngName12", TEST_CATEGORY_KNIT);

        //when
        testEntityManager.persist(TEST_CLOTHES_1);
        testEntityManager.persist(TEST_CLOTHES_2);
        testEntityManager.persist(TEST_CLOTHES_3);
        testEntityManager.persist(TEST_CLOTHES_4);
        testEntityManager.persist(TEST_CLOTHES_5);
        testEntityManager.persist(TEST_CLOTHES_6);
        testEntityManager.persist(TEST_CLOTHES_7);
        testEntityManager.persist(TEST_CLOTHES_8);
        testEntityManager.persist(TEST_CLOTHES_9);
        testEntityManager.persist(TEST_CLOTHES_10);
        testEntityManager.persist(TEST_CLOTHES_11);
        testEntityManager.persist(TEST_CLOTHES_12);

        Page<Item> page = itemRepository.getItemsByCategoryIds(List.of(TEST_CATEGORY_TOP.getCategoryId(),
                TEST_CATEGORY_SHIRT.getCategoryId(), TEST_CATEGORY_KNIT.getCategoryId()), PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "createdDate")));

        //then
        List<Item> items = page.getContent();
        assertThat(items.size(), is(5)); // 3개만 페이징 했는가.?
        assertThat(page.getNumber(), is(0)); // 첫페이지가 맞는가.? (jpa paging 은 0번 페이지부터 시작)
        assertThat(page.getTotalPages(), is(2)); // 총 2페이지가 맞는가.?
        if(items.get(0) instanceof Clothes) {
            assertThat(items.get(0).getItemId(), is(TEST_CLOTHES_2.getItemId()));
            assertThat(items.get(0).getName(), is(TEST_CLOTHES_2.getName()));
            assertThat(((Clothes) items.get(0)).getEngName(), is(TEST_CLOTHES_2.getEngName()));
        }

        if(items.get(1) instanceof Clothes) {
            assertThat(items.get(1).getItemId(), is(TEST_CLOTHES_5.getItemId()));
            assertThat(items.get(1).getName(), is(TEST_CLOTHES_5.getName()));
            assertThat(((Clothes) items.get(1)).getEngName(), is(TEST_CLOTHES_5.getEngName()));
        }

        if(items.get(2) instanceof Clothes) {
            assertThat(items.get(2).getItemId(), is(TEST_CLOTHES_7.getItemId()));
            assertThat(items.get(2).getName(), is(TEST_CLOTHES_7.getName()));
            assertThat(((Clothes) items.get(2)).getEngName(), is(TEST_CLOTHES_7.getEngName()));
        }

        if(items.get(3) instanceof Clothes) {
            assertThat(items.get(3).getItemId(), is(TEST_CLOTHES_8.getItemId()));
            assertThat(items.get(3).getName(), is(TEST_CLOTHES_8.getName()));
            assertThat(((Clothes) items.get(3)).getEngName(), is(TEST_CLOTHES_8.getEngName()));
        }

        if(items.get(4) instanceof Clothes) {
            assertThat(items.get(4).getItemId(), is(TEST_CLOTHES_10.getItemId()));
            assertThat(items.get(4).getName(), is(TEST_CLOTHES_10.getName()));
            assertThat(((Clothes) items.get(4)).getEngName(), is(TEST_CLOTHES_10.getEngName()));
        }
    }

    private Clothes createClothes(String name, String engName, Category category){
        Clothes clothes = Clothes.builder()
                .name(name)
                .price(1000l)
                .engName(engName)
                .color(Color.RED)
                .build();

        clothes.changeCategoryItems(List.of(new CategoryItem(category)));

        return clothes;
    }

}
