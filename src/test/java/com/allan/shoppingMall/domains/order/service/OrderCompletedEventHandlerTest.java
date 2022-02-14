package com.allan.shoppingMall.domains.order.service;

import com.allan.shoppingMall.domains.cart.domain.Cart;
import com.allan.shoppingMall.domains.cart.domain.CartItem;
import com.allan.shoppingMall.domains.cart.domain.CartRepository;
import com.allan.shoppingMall.domains.item.domain.clothes.Clothes;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesSize;
import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import com.allan.shoppingMall.domains.order.domain.*;
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

@ExtendWith(MockitoExtension.class)
@Rollback(value = true)
public class OrderCompletedEventHandlerTest {

    @Mock
    CartRepository cartRepository;

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderCompletedEventHandler orderCompletedEventHandler;

    @Test
    public void 주문완료_이벤트_핸들러_테스트() throws Exception {
        //given
        Order TEST_ORDER = Order.builder()
                .build();

        Clothes TEST_CLOTHES = Clothes.builder()
                .price(1000l)
                .build();
        ReflectionTestUtils.setField(TEST_CLOTHES, "itemId", 1l);

        ClothesSize TEST_CLOTHES_SIZE_1 = ClothesSize.builder()
                .sizeLabel(SizeLabel.S)
                .stockQuantity(10l)
                .build();

        ClothesSize TEST_CLOTHES_SIZE_2 = ClothesSize.builder()
                .sizeLabel(SizeLabel.M)
                .stockQuantity(20l)
                .build();

        ClothesSize TEST_CLOTHES_SIZE_3 = ClothesSize.builder()
                .sizeLabel(SizeLabel.L)
                .stockQuantity(30l)
                .build();

        TEST_CLOTHES.changeClothesSizes(List.of(
                TEST_CLOTHES_SIZE_1, TEST_CLOTHES_SIZE_2, TEST_CLOTHES_SIZE_3
        ));

        Clothes TEST_CLOTHES_2 = Clothes.builder()
                .price(2000l)
                .build();
        ReflectionTestUtils.setField(TEST_CLOTHES, "itemId", 2l);
        ClothesSize TEST_CLOTHES_SIZE_4 = ClothesSize.builder()
                .sizeLabel(SizeLabel.S)
                .stockQuantity(100l)
                .build();
        ClothesSize TEST_CLOTHES_SIZE_5 = ClothesSize.builder()
                .sizeLabel(SizeLabel.M)
                .stockQuantity(200l)
                .build();

        TEST_CLOTHES_2.changeClothesSizes(List.of(
            TEST_CLOTHES_SIZE_4, TEST_CLOTHES_SIZE_5
        ));

        TEST_ORDER.changeOrderItems(List.of(
                new OrderClothes(1l, TEST_CLOTHES, TEST_CLOTHES_SIZE_1),
                new OrderClothes(1l, TEST_CLOTHES, TEST_CLOTHES_SIZE_2),
                new OrderClothes(1l, TEST_CLOTHES, TEST_CLOTHES_SIZE_3)
        ));

        given(orderRepository.findById(any()))
                .willReturn(Optional.of(TEST_ORDER));

        Cart TEST_CART = Cart.builder().build();
        TEST_CART.addCartItems(List.of(
                CartItem.builder()
                        .item(TEST_CLOTHES)
                        .cartQuantity(10l)
                        .size(TEST_CLOTHES_SIZE_1.getSizeLabel())
                        .build(),
                CartItem.builder()
                        .item(TEST_CLOTHES)
                        .cartQuantity(20l)
                        .size(TEST_CLOTHES_SIZE_2.getSizeLabel())
                        .build(),
                CartItem.builder()
                        .item(TEST_CLOTHES)
                        .cartQuantity(30l)
                        .size(TEST_CLOTHES_SIZE_3.getSizeLabel())
                        .build(),
                CartItem.builder()
                        .item(TEST_CLOTHES_2)
                        .cartQuantity(100l)
                        .size(TEST_CLOTHES_SIZE_4.getSizeLabel())
                        .build(),
                CartItem.builder()
                        .item(TEST_CLOTHES_2)
                        .cartQuantity(200l)
                        .size(TEST_CLOTHES_SIZE_5.getSizeLabel())
                        .build()
        ));
        given(cartRepository.findByAuthId(any()))
                .willReturn(Optional.of(TEST_CART));


        OrderCompletedEvent TEST_EVENT= new OrderCompletedEvent(1l, "testAuthId");

        //when
        orderCompletedEventHandler.handleEvent(TEST_EVENT);

        //then
        assertThat(TEST_CART.getCartItems().size(), is(2));
        assertThat(TEST_CART.getCartItems().get(0).getItem().getItemId(), is(TEST_CLOTHES_2.getItemId()));
        assertThat(TEST_CART.getCartItems().get(0).getCartQuantity(), is(100l));
        assertThat(TEST_CART.getCartItems().get(0).getSize(), is(TEST_CLOTHES_SIZE_4.getSizeLabel()));

        assertThat(TEST_CART.getCartItems().get(1).getItem().getItemId(), is(TEST_CLOTHES_2.getItemId()));
        assertThat(TEST_CART.getCartItems().get(1).getCartQuantity(), is(200l));
        assertThat(TEST_CART.getCartItems().get(1).getSize(), is(TEST_CLOTHES_SIZE_5.getSizeLabel()));
    }
}
