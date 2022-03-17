package com.allan.shoppingMall.domains.cart.domain;

import com.allan.shoppingMall.common.config.jpa.auditing.JpaAuditingConfig;
import com.allan.shoppingMall.domains.item.domain.clothes.Clothes;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesSize;
import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import com.allan.shoppingMall.domains.item.domain.item.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = JpaAuditingConfig.class
))
public class CartItemRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    CartItemRepository cartItemRepository;

    Clothes TEST_CLOTHES;
    ClothesSize TEST_CLOTHES_SIZE_S;
    ClothesSize TEST_CLOTHES_SIZE_M;
    ClothesSize TEST_CLOTHES_SIZE_L;

    @BeforeEach
    public void setUp(){
        Cart TEST_TEMP_CART = Cart.builder()
                .ckId("testCkId")
                .build();


        TEST_CLOTHES = Clothes.builder()
                .name("testClothesName")
                .price(1000l)
                .engName("testEngName")
                .color(Color.RED)
                .build();
        ReflectionTestUtils.setField(TEST_CLOTHES, "createdBy", "testSystem");
        ReflectionTestUtils.setField(TEST_CLOTHES, "createdDate", LocalDateTime.now());

        TEST_CLOTHES_SIZE_S = ClothesSize.builder()
                .sizeLabel(SizeLabel.S)
                .stockQuantity(10l)
                .build();
        ReflectionTestUtils.setField(TEST_CLOTHES_SIZE_S, "createdBy", "testSystem");
        ReflectionTestUtils.setField(TEST_CLOTHES_SIZE_S, "createdDate", LocalDateTime.now());

        TEST_CLOTHES_SIZE_M = ClothesSize.builder()
                .sizeLabel(SizeLabel.S)
                .stockQuantity(20l)
                .build();
        ReflectionTestUtils.setField(TEST_CLOTHES_SIZE_M, "createdBy", "testSystem");
        ReflectionTestUtils.setField(TEST_CLOTHES_SIZE_M, "createdDate", LocalDateTime.now());

        TEST_CLOTHES_SIZE_L = ClothesSize.builder()
                .sizeLabel(SizeLabel.L)
                .stockQuantity(30l)
                .build();
        ReflectionTestUtils.setField(TEST_CLOTHES_SIZE_M, "createdBy", "testSystem");
        ReflectionTestUtils.setField(TEST_CLOTHES_SIZE_M, "createdDate", LocalDateTime.now());

        TEST_CLOTHES.changeClothesSize(List.of(TEST_CLOTHES_SIZE_S, TEST_CLOTHES_SIZE_M));

        testEntityManager.persist(TEST_CLOTHES);

        TEST_TEMP_CART.addCartItems(List.of(
                new CartItem(TEST_CLOTHES, 10l, SizeLabel.S),
                new CartItem(TEST_CLOTHES, 20l, SizeLabel.M),
                new CartItem(TEST_CLOTHES, 30l, SizeLabel.L)
        ));

        testEntityManager.persist(TEST_TEMP_CART);
        testEntityManager.clear();
    }


    @Test
    public void 장바구니_상품_특정_사이즈_상품_삭제_테스트() throws Exception {
        //given

        // 장바구니 상품 등록 확인.
        List<CartItem> cartItems = cartItemRepository.findAll();
        assertThat(cartItems.size(), is(3));

        //when
        cartItemRepository.deleteCartItems(TEST_CLOTHES, List.of(SizeLabel.S, SizeLabel.M));

        //then
        List<CartItem> afterDeleteCartItems = cartItemRepository.findAll();
        assertThat(afterDeleteCartItems.size(), is(2));
        assertThat(afterDeleteCartItems.get(0).getItem().getItemId(), is(TEST_CLOTHES.getItemId()));
        assertThat(afterDeleteCartItems.get(0).getSize(), is(SizeLabel.S));
        assertThat(afterDeleteCartItems.get(1).getItem().getItemId(), is(TEST_CLOTHES.getItemId()));
        assertThat(afterDeleteCartItems.get(1).getSize(), is(SizeLabel.M));
    }
}
