package com.allan.shoppingMall.domains.order.service;

import com.allan.shoppingMall.domains.item.domain.Item;
import com.allan.shoppingMall.domains.item.domain.ItemRepository;
import com.allan.shoppingMall.domains.item.domain.clothes.*;
import com.allan.shoppingMall.domains.member.domain.Member;
import com.allan.shoppingMall.domains.order.domain.OrderRepository;
import com.allan.shoppingMall.domains.order.domain.model.OrderLineRequest;
import com.allan.shoppingMall.domains.order.domain.model.OrderRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.hamcrest.Matchers.is;


@ExtendWith(MockitoExtension.class)
@Rollback(value = true)
public class OrderServiceTest {

    @Mock
    ClothesRepository clothesRepository;

    @Mock
    OrderRepository orderRepository;

    @Mock
    ItemRepository itemRepository;

    @Mock
    ClothesSizeRepository clothesSizeRepository;

    @InjectMocks
    OrderService orderService;

    @Test
    public void 주문_테스트() throws Exception {
        //given
        ClothesSize TEST_CLOTHES_SIZE = createClothesSize();
        given(clothesSizeRepository.getClothesSizebySizelabel(any(), any()))
                .willReturn(TEST_CLOTHES_SIZE);

        Clothes TEST_CLOTHES = Clothes.builder()
                .price(15000l)
                .build();
        // clothes 엔티티의 재고량을 조절하기 위해선 changeClotehsSizes() 를 활용해야 합니다.
        // 자세한 정보는 Clothes 엔티티를 참고 해 주세요.
        TEST_CLOTHES.changeClothesSizes(createClothesSizes());
        given(clothesRepository.findById(any()))
                .willReturn(Optional.of(TEST_CLOTHES));

        List<OrderLineRequest> TEST_ORDER_ITEMS_SUMMARY = createOrderLineRequest();

        Member TEST_MEMBER = Member.builder().build();

        OrderRequest TEST_ORDER_REQUEST = new OrderRequest();
        TEST_ORDER_REQUEST.setOrderItems(TEST_ORDER_ITEMS_SUMMARY);
        TEST_ORDER_REQUEST.setOrdererName("testOrdererName");
        TEST_ORDER_REQUEST.setOrdererPhone("0000000000");
        TEST_ORDER_REQUEST.setOrdererEmail("testOrdererEmail");
        TEST_ORDER_REQUEST.setPostcode("222-3333");
        TEST_ORDER_REQUEST.setAddress("testAddress");
        TEST_ORDER_REQUEST.setDetailAddress("testDetailAddress");
        TEST_ORDER_REQUEST.setDeliveryMemo("testMemo");
        TEST_ORDER_REQUEST.setRecipient("testRecipient");
        TEST_ORDER_REQUEST.setRecipientPhone("1111111111");

        //when
        orderService.order(TEST_ORDER_REQUEST, TEST_MEMBER);

        //then
        verify(orderRepository, atLeastOnce()).save(any());
        assertThat(TEST_CLOTHES.getStockQuantity(), is(12l));
        assertThat(TEST_CLOTHES_SIZE.getStockQuantity(), is(2l));
    }

    private ClothesSize createClothesSize() {
        return ClothesSize.builder()
                .stockQuantity(12l)
                .sizeLabel(SizeLabel.M)
                .build();
    }

    private List<ClothesSize> createClothesSizes(){
        return List.of(
                ClothesSize.builder()
                .stockQuantity(10l)
                .build(),

                createClothesSize()
        );
    }

    private List<OrderLineRequest> createOrderLineRequest() {
        return List.of(
                OrderLineRequest.builder()
                        .itemId(1l)
                        .size(SizeLabel.M)
                        .orderQuantity(10l)
                        .build());
    }
}
