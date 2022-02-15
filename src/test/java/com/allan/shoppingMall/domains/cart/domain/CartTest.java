package com.allan.shoppingMall.domains.cart.domain;

import com.allan.shoppingMall.common.config.jpa.auditing.JpaAuditingConfig;
import com.allan.shoppingMall.common.value.Address;
import com.allan.shoppingMall.domains.item.domain.Color;
import com.allan.shoppingMall.domains.item.domain.clothes.Clothes;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesSize;
import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import com.allan.shoppingMall.domains.member.domain.Gender;
import com.allan.shoppingMall.domains.member.domain.Member;
import com.allan.shoppingMall.domains.member.domain.MemberRole;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
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

/** 장바구니 도메인 테스트.
 */
@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = JpaAuditingConfig.class
))
public class CartTest {

    //
    private static Member TEST_MEMEMBER = null;
    private static Clothes TEST_CLOTHES = null;
    private static Clothes TEST_CLOTHES_2 = null;

    @Autowired
    TestEntityManager testEntityManager;

    @BeforeEach
    public void setUp(){
        TEST_MEMEMBER = Member.builder()
                .name("testName")
                .authId("testAuthId")
                .pwd("testPwd")
                .dateOfBirth("19920214")
                .phone("01012341234")
                .role(MemberRole.ACTIVATED_USER)
                .email("test@test.com")
                .address(Address.builder()
                        .address("testAddress")
                        .detailAddress("testDetailAddress")
                        .build())
                .age(15)
                .gender(Gender.MAN)
                .build();

        TEST_CLOTHES = Clothes.builder()
                .name("testClothesName")
                .engName("testClothesEngName")
                .color(Color.RED)
                .price(1000l)
                .build();

        TEST_CLOTHES.changeClothesSizes(List.of(
                ClothesSize.builder()
                        .stockQuantity(10l)
                        .sizeLabel(SizeLabel.S)
                        .build(),
                ClothesSize.builder()
                        .stockQuantity(20l)
                        .sizeLabel(SizeLabel.M)
                        .build(),
                ClothesSize.builder()
                        .stockQuantity(30l)
                        .sizeLabel(SizeLabel.L)
                        .build()
        ));

        TEST_CLOTHES_2 = Clothes.builder()
                .name("testClothesName2")
                .engName("testClothesEngName2")
                .color(Color.BLUE)
                .price(2000l)
                .build();

        TEST_CLOTHES_2.changeClothesSizes(List.of(
                ClothesSize.builder()
                        .stockQuantity(10l)
                        .sizeLabel(SizeLabel.S)
                        .build(),
                ClothesSize.builder()
                        .stockQuantity(20l)
                        .sizeLabel(SizeLabel.M)
                        .build()
        ));

        // cartItem 은 itemId와 size 로 동일 비교를 하기 때문에 itemId가 필요합니다.
        // Clothes 도메인의 itemId 는 Identity stratgy 를 사용하였기 때문에 cart 를 생성하기 전에 미리 Clothes를 flush() 를 미리 호출합니다.
        testEntityManager.persist(TEST_MEMEMBER);
        testEntityManager.persist(TEST_CLOTHES);
        testEntityManager.persist(TEST_CLOTHES_2);
        testEntityManager.flush();
    }

    @Test
    @WithMockUser
    public void 장바구니_이미_등록된_상품_등록테스트() throws Exception {
       // given
       Cart TEST_CART = Cart.builder()
                .member(TEST_MEMEMBER)
                .build();

        // when
        TEST_CART.addCartItems(List.of(
                new CartItem(TEST_CLOTHES, 1l, SizeLabel.S),
                new CartItem(TEST_CLOTHES, 2l, SizeLabel.M),
                new CartItem(TEST_CLOTHES, 3l, SizeLabel.L),
                new CartItem(TEST_CLOTHES, 4l, SizeLabel.S)
        ));


        // then
        assertThat(TEST_CART.getCartItems().size(), is(3));
        assertThat(TEST_CART.getCartItems().get(0).getItem().getItemId(), is(TEST_CLOTHES.getItemId()));
        assertThat(TEST_CART.getCartItems().get(0).getCartQuantity(), is(5l));
        assertThat(TEST_CART.getCartItems().get(0).getSize().getDesc(), is(SizeLabel.S.getDesc()));

        assertThat(TEST_CART.getCartItems().get(1).getItem().getItemId(), is(TEST_CLOTHES.getItemId()));
        assertThat(TEST_CART.getCartItems().get(1).getCartQuantity(), is(2l));
        assertThat(TEST_CART.getCartItems().get(1).getSize().getDesc(), is(SizeLabel.M.getDesc()));

        assertThat(TEST_CART.getCartItems().get(2).getItem().getItemId(), is(TEST_CLOTHES.getItemId()));
        assertThat(TEST_CART.getCartItems().get(2).getCartQuantity(), is(3l));
        assertThat(TEST_CART.getCartItems().get(2).getSize().getDesc(), is(SizeLabel.L.getDesc()));
    }

    /**
     * 장바구니에서 일부 장바구니 상품을 삭제하는 메소드.
     */
    @Test
    @WithMockUser
    public void 장바구니_상품_제거테스트() throws Exception {
        //given
        Cart TEST_CART = Cart.builder()
                .member(TEST_MEMEMBER)
                .build();

        TEST_CART.addCartItems(List.of(
                new CartItem(TEST_CLOTHES, 1l, SizeLabel.S),
                new CartItem(TEST_CLOTHES, 1l, SizeLabel.M),
                new CartItem(TEST_CLOTHES, 1l, SizeLabel.L),
                new CartItem(TEST_CLOTHES_2, 2l, SizeLabel.M)
        ));

        testEntityManager.persist(TEST_CART);
        assertThat(TEST_CART.getCartItems().size(), is(4));

        //when
        TEST_CART.substractCartItems(List.of( new CartItem(TEST_CLOTHES, 1l, SizeLabel.S),
                new CartItem(TEST_CLOTHES, 1l, SizeLabel.M),
                new CartItem(TEST_CLOTHES, 1l, SizeLabel.L)));

        //then
        assertThat(TEST_CART.getCartItems().size(), is(1));
        assertThat(TEST_CART.getCartItems().get(0).getItem().getItemId(), is(TEST_CLOTHES_2.getItemId()));
        assertThat(TEST_CART.getCartItems().get(0).getCartQuantity(), is(2l));
        assertThat(TEST_CART.getCartItems().get(0).getSize().getDesc(), is(SizeLabel.M.getDesc()));
    }

    @Test
    @WithMockUser
    public void 장바구니_상품_수정테스트() throws Exception {
        //given
        Cart TEST_CART = Cart.builder()
                .member(TEST_MEMEMBER)
                .build();

        TEST_CART.addCartItems(List.of(
                new CartItem(TEST_CLOTHES, 1l, SizeLabel.S),
                new CartItem(TEST_CLOTHES, 2l, SizeLabel.M),
                new CartItem(TEST_CLOTHES_2, 10l, SizeLabel.L)
        ));

        testEntityManager.persist(TEST_CART);
        assertThat(TEST_CART.getCartItems().size(), is(3));
        assertThat(TEST_CART.getCartItems().get(0).getItem().getItemId(), is(TEST_CLOTHES.getItemId()));
        assertThat(TEST_CART.getCartItems().get(0).getCartQuantity(), is(1l));
        assertThat(TEST_CART.getCartItems().get(0).getSize(), is(SizeLabel.S));

        assertThat(TEST_CART.getCartItems().get(1).getItem().getItemId(), is(TEST_CLOTHES.getItemId()));
        assertThat(TEST_CART.getCartItems().get(1).getCartQuantity(), is(2l));
        assertThat(TEST_CART.getCartItems().get(1).getSize(), is(SizeLabel.M));

        assertThat(TEST_CART.getCartItems().get(2).getItem().getItemId(), is(TEST_CLOTHES_2.getItemId()));
        assertThat(TEST_CART.getCartItems().get(2).getCartQuantity(), is(10l));
        assertThat(TEST_CART.getCartItems().get(2).getSize(), is(SizeLabel.L));

        //when
        TEST_CART.modifyCartItems(List.of(
                new CartItem(TEST_CLOTHES, 3l, SizeLabel.S),
                new CartItem(TEST_CLOTHES, 0l, SizeLabel.M),
                new CartItem(TEST_CLOTHES_2, 20l, SizeLabel.L)
        ));

        //then
        assertThat(TEST_CART.getCartItems().size(), is(2));
        assertThat(TEST_CART.getCartItems().get(0).getItem().getItemId(), is(TEST_CLOTHES.getItemId()));
        assertThat(TEST_CART.getCartItems().get(0).getCartQuantity(), is(3l));
        assertThat(TEST_CART.getCartItems().get(0).getSize(), is(SizeLabel.S));

        assertThat(TEST_CART.getCartItems().get(1).getItem().getItemId(), is(TEST_CLOTHES_2.getItemId()));
        assertThat(TEST_CART.getCartItems().get(1).getCartQuantity(), is(20l));
        assertThat(TEST_CART.getCartItems().get(1).getSize(), is(SizeLabel.L));

    }
}
