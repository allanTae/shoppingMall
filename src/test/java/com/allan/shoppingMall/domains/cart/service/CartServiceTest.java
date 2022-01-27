package com.allan.shoppingMall.domains.cart.service;

import com.allan.shoppingMall.domains.cart.domain.Cart;
import com.allan.shoppingMall.domains.cart.domain.CartItem;
import com.allan.shoppingMall.domains.cart.domain.CartRepository;
import com.allan.shoppingMall.domains.cart.domain.model.CartItemSummary;
import com.allan.shoppingMall.domains.cart.domain.model.CartRequest;
import com.allan.shoppingMall.domains.item.domain.clothes.Clothes;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesRepository;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesSize;
import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import com.allan.shoppingMall.domains.member.domain.Member;
import com.allan.shoppingMall.domains.member.domain.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
@Rollback(value = true)
public class CartServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    CartRepository cartRepository;

    @Mock
    ClothesRepository clothesRepository;

    @InjectMocks
    CartService cartService;

    @Test
    public void 쿠키정보가_존재하는_경우_비회원_장바구니_추가테스트() throws Exception {
        //given
        Cart TEST_CART = Cart.builder().build();

        given(cartRepository.findByCkId(any()))
                .willReturn(Optional.of(TEST_CART));

        CartRequest TEST_CARTREQUEST = CartRequest.builder()
                .cartCkId("testCkId")
                .cartItems(List.of(
                        new CartItemSummary(1l, 1l, SizeLabel.S),
                        new CartItemSummary(2l, 2l, SizeLabel.M)

                ))
                .build();

        Clothes TEST_CLOTHES_1 = Clothes.builder()
                .name("testClothes1")
                .build();

        Clothes TEST_CLOTHES_2 = Clothes.builder()
                .name("testClothes2")
                .build();

        given(clothesRepository.findById(1l))
                .willReturn(Optional.of(TEST_CLOTHES_1));
        given(clothesRepository.findById(2l))
                .willReturn(Optional.of(TEST_CLOTHES_2));

        //when
        cartService.updatempCart(TEST_CARTREQUEST);

        //then
        verify(cartRepository, atLeastOnce()).findByCkId(any());
        verify(clothesRepository, atLeastOnce()).findById(1l);
        verify(clothesRepository, atLeastOnce()).findById(2l);
        assertThat(TEST_CART.getCartItems().size(), is(2));
        assertThat(TEST_CART.getCartItems().get(0).getItem().getName(), is(TEST_CLOTHES_1.getName()));
        assertThat(TEST_CART.getCartItems().get(1).getItem().getName(), is(TEST_CLOTHES_2.getName()));
    }

    @Test
    public void 쿠키_정보가_없는경우_비회원_장바구니_추가테스트() throws Exception {
        //given
        CartRequest TEST_CARTREQUEST = CartRequest.builder()
                .cartCkId("testCkId")
                .cartItems(List.of(
                        new CartItemSummary(1l, 1l, SizeLabel.S),
                        new CartItemSummary(2l, 2l, SizeLabel.M)

                ))
                .build();

        Clothes TEST_CLOTHES_1 = Clothes.builder()
                .name("testClothes1")
                .build();

        Clothes TEST_CLOTHES_2 = Clothes.builder()
                .name("testClothes2")
                .build();

        given(clothesRepository.findById(1l))
                .willReturn(Optional.of(TEST_CLOTHES_1));
        given(clothesRepository.findById(2l))
                .willReturn(Optional.of(TEST_CLOTHES_2));

        //when
        cartService.addTempCart(TEST_CARTREQUEST);

        //then
        verify(clothesRepository, atLeastOnce()).findById(1l);
        verify(clothesRepository, atLeastOnce()).findById(2l);
        verify(cartRepository, atLeastOnce()).save(any());
    }

    @Test
    public void 회원_장바구니_추가테스트() throws Exception {
        //given
        CartRequest TEST_CARTREQUEST = CartRequest.builder()
                .cartCkId("testCkId")
                .cartItems(List.of(
                        new CartItemSummary(1l, 1l, SizeLabel.S),
                        new CartItemSummary(2l, 2l, SizeLabel.M)

                ))
                .build();

        Member TEST_MEMBER = Member.builder()
                .authId("testAuthId")
                .build();

        given(memberRepository.findByAuthIdLike(any()))
                .willReturn(Optional.of(TEST_MEMBER));

        Clothes TEST_CLOTHES_1 = Clothes.builder()
                .name("testClothes1")
                .build();

        Clothes TEST_CLOTHES_2 = Clothes.builder()
                .name("testClothes2")
                .build();

        given(clothesRepository.findById(1l))
                .willReturn(Optional.of(TEST_CLOTHES_1));
        given(clothesRepository.findById(2l))
                .willReturn(Optional.of(TEST_CLOTHES_2));

        Cart TEST_CART = Cart.builder().build();
        given(cartRepository.findByAuthId(TEST_MEMBER.getAuthId()))
                .willReturn(Optional.of(TEST_CART));

        //when
        cartService.addMemberCart(TEST_CARTREQUEST, TEST_MEMBER.getAuthId());

        //then
        verify(clothesRepository, atLeastOnce()).findById(1l);
        verify(clothesRepository, atLeastOnce()).findById(2l);
        verify(cartRepository, atLeastOnce()).findByAuthId(TEST_MEMBER.getAuthId());
        assertThat(TEST_CART.getCartItems().size(), is(2));
        assertThat(TEST_CART.getCartItems().get(0).getItem().getName(), is(TEST_CLOTHES_1.getName()));
        assertThat(TEST_CART.getCartItems().get(1).getItem().getName(), is(TEST_CLOTHES_2.getName()));
    }

    @Test
    public void 비회원장바구니_상품들을_회원장바구니로_추가_테스트() throws Exception {
        //given
        Cart TEST_COOKIE_CART = createCookieCart();
        Cart TEST_MEMBER_CART = createMemberCart();
        given(cartRepository.findByCkId(TEST_COOKIE_CART.getCkId()))
                .willReturn(Optional.of(TEST_COOKIE_CART));
        given(cartRepository.findByAuthId("testAuthId"))
                .willReturn(Optional.of(TEST_MEMBER_CART));

        //when
        cartService.updateMemberCartByTempCart(TEST_COOKIE_CART.getCkId(), "testAuthId");

        //then
        verify(cartRepository, atLeastOnce()).findByCkId(any());
        verify(cartRepository, atLeastOnce()).findByAuthId(any());
        assertThat(TEST_MEMBER_CART.getCartItems().size(), is(2));
        assertThat(TEST_MEMBER_CART.getCartItems().get(1).getCartQuantity(), is(TEST_COOKIE_CART.getCartItems().get(0).getCartQuantity()));
        assertThat(TEST_MEMBER_CART.getCartItems().get(1).getSize(), is(TEST_COOKIE_CART.getCartItems().get(0).getSize()));
        verify(cartRepository, atLeastOnce()).delete(any(Cart.class));
    }

    private Cart createCookieCart(){
        // 테스트 의상 상품.
        Clothes TEST_CLOTHES = Clothes.builder().build();
        ReflectionTestUtils.setField(TEST_CLOTHES, "itemId", 1l);

        ClothesSize TEST_CLOTHES_SIZE_S = ClothesSize.builder()
                .stockQuantity(1l)
                .sizeLabel(SizeLabel.S)
                .build();

        TEST_CLOTHES.changeClothesSizes(List.of(TEST_CLOTHES_SIZE_S));

        // 장바구니.
        Cart TEST_COOKIE_CART = Cart.builder()
                .ckId("testCkId")
                .build();

        // 장바구니 상품 목록.
        List<CartItem> TEST_CART_ITEM_LIST = List.of(
                new CartItem(TEST_CLOTHES, 1l, TEST_CLOTHES_SIZE_S.getSizeLabel())
        );

        TEST_COOKIE_CART.addCartItems(TEST_CART_ITEM_LIST);

        return TEST_COOKIE_CART;
    }

    private Cart createMemberCart(){
        // 테스트 의상 상품.
        Clothes TEST_CLOTHES = Clothes.builder().build();
        ReflectionTestUtils.setField(TEST_CLOTHES, "itemId", 1l);

        ClothesSize TEST_CLOTHES_SIZE_M = ClothesSize.builder()
                .stockQuantity(2l)
                .sizeLabel(SizeLabel.M)
                .build();

        TEST_CLOTHES.changeClothesSizes(List.of(TEST_CLOTHES_SIZE_M));

        // 장바구니.
        Cart TEST_MEMBER_CART = Cart.builder().build();

        // 장바구니 상품 목록.
        List<CartItem> TEST_CART_ITEM_LIST = List.of(
                new CartItem(TEST_CLOTHES, 2l, TEST_CLOTHES_SIZE_M.getSizeLabel())
        );

        TEST_MEMBER_CART.addCartItems(TEST_CART_ITEM_LIST);

        return TEST_MEMBER_CART;
    }
}
