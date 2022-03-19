package com.allan.shoppingMall.domains.cart.domain;

import com.allan.shoppingMall.common.config.jpa.auditing.JpaAuditingConfig;
import com.allan.shoppingMall.domains.item.domain.clothes.Clothes;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesSize;
import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
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
public class CartItemTest {

    @Autowired
    TestEntityManager testEntityManager;

    /**
     * 의류 상품의 경우, 장바구니에 추가할때, 상품 도메인의 아이디와 의상사이즈 도메인의 사이즈 정보로
     * 비교를 한다.
     * 아이디 정보와 사이즈 정보를 동일한 케이스와 상이한 케이스를 넣을 때 잘 비교를 하는지 테스트한다.
     * @param
     * @return
     */
    @Test
    public void 의류_동일한_상품_추가_테스트() throws Exception {
        //given
        Cart TEST_CART = Cart.builder()
                .build();

        ClothesSize TEST_CLOTHES_1_SIZE_S = ClothesSize.builder()
                .stockQuantity(10l)
                .sizeLabel(SizeLabel.S)
                .build();

        ClothesSize TEST_CLOTHES_2_SIZE_M = ClothesSize.builder()
                .stockQuantity(20l)
                .sizeLabel(SizeLabel.M)
                .build();

        Clothes TEST_CLOTHES = Clothes.builder()
                .engName("testClothesEngName")
                .name("testClothesName")
                .price(1000l)
                .build();

        TEST_CLOTHES.changeItemSizes(List.of(TEST_CLOTHES_1_SIZE_S, TEST_CLOTHES_2_SIZE_M));

        Clothes TEST_CLOTHES_2 = Clothes.builder()
                .engName("testClothesEngName2")
                .name("testClothesName2")
                .price(2000l)
                .build();

        TEST_CLOTHES_2.changeItemSizes(List.of(TEST_CLOTHES_1_SIZE_S, TEST_CLOTHES_2_SIZE_M));

        CartItem TEST_CART_ITEM_1 = new CartItem(TEST_CLOTHES, 1l, TEST_CLOTHES.getItemSizes().get(0).getSizeLabel());
        CartItem TEST_CART_ITEM_2 = new CartItem(TEST_CLOTHES, 1l, TEST_CLOTHES.getItemSizes().get(0).getSizeLabel());
        CartItem TEST_CART_ITEM_3 = new CartItem(TEST_CLOTHES, 1l, TEST_CLOTHES.getItemSizes().get(1).getSizeLabel());
        CartItem TEST_CART_ITEM_4 = new CartItem(TEST_CLOTHES_2, 1l, TEST_CLOTHES.getItemSizes().get(0).getSizeLabel());

        // when
        testEntityManager.persist(TEST_CLOTHES);
        testEntityManager.persist(TEST_CLOTHES_2);
        testEntityManager.persist(TEST_CART);
        TEST_CART.addCartItems(List.of(TEST_CART_ITEM_1, TEST_CART_ITEM_2, TEST_CART_ITEM_3, TEST_CART_ITEM_4));

        // then
        assertThat(TEST_CART.getCartItems().size(), is(3));
        assertThat(TEST_CART.getCartItems().get(0), is(TEST_CART_ITEM_1));
        assertThat(TEST_CART.getCartItems().get(1), is(TEST_CART_ITEM_3));
        assertThat(TEST_CART.getCartItems().get(2), is(TEST_CART_ITEM_4));
    }

}
