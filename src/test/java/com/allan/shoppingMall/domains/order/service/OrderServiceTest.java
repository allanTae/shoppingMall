package com.allan.shoppingMall.domains.order.service;

import com.allan.shoppingMall.common.exception.order.payment.PaymentFailByValidatedOrderStatusException;
import com.allan.shoppingMall.common.exception.order.payment.PaymentFailException;
import com.allan.shoppingMall.common.value.Address;
import com.allan.shoppingMall.domains.delivery.domain.Delivery;
import com.allan.shoppingMall.domains.delivery.domain.DeliveryStatus;
import com.allan.shoppingMall.domains.item.domain.item.Color;
import com.allan.shoppingMall.domains.item.domain.item.ItemImage;
import com.allan.shoppingMall.domains.item.domain.clothes.*;
import com.allan.shoppingMall.domains.item.domain.item.ItemSize;
import com.allan.shoppingMall.domains.item.domain.item.ItemSizeRepository;
import com.allan.shoppingMall.domains.member.domain.Member;
import com.allan.shoppingMall.domains.mileage.domain.model.MileageContent;
import com.allan.shoppingMall.domains.mileage.domain.model.MileageDTO;
import com.allan.shoppingMall.domains.mileage.service.MileageService;
import com.allan.shoppingMall.domains.order.domain.*;
import com.allan.shoppingMall.domains.order.domain.model.OrderLineRequest;
import com.allan.shoppingMall.domains.order.domain.model.OrderRequest;
import com.allan.shoppingMall.domains.payment.domain.PaymentRepository;
import com.allan.shoppingMall.domains.payment.domain.model.PaymentDTO;
import com.allan.shoppingMall.domains.payment.domain.model.iamport.PaymentIamportDTO;
import com.allan.shoppingMall.domains.payment.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Rollback(value = true)
public class OrderServiceTest {

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    PaymentService paymentService;

    @Mock
    ClothesRepository clothesRepository;

    @Mock
    OrderRepository orderRepository;

    @Mock
    ItemSizeRepository itemSizeRepository;

    @Mock
    MileageService mileageService;

    @InjectMocks
    OrderService orderService;

    @Test
    public void 주문_테스트() throws Exception {
        //given
        ItemSize TEST_ITEM_SIZE = createItemSize();

        ClothesSize TEST_CLOTHES_SIZE_1 = ClothesSize.builder()
                .sizeLabel(SizeLabel.M)
                .stockQuantity(20l)
                .build();

        ClothesSize TEST_CLOTHES_SIZE_2 = ClothesSize.builder()
                .sizeLabel(SizeLabel.S)
                .stockQuantity(10l)
                .build();

        given(itemSizeRepository.getItemSizebySizelabel(any(), any()))
                .willReturn(TEST_CLOTHES_SIZE_1);

        Clothes TEST_CLOTHES = Clothes.builder()
                .price(15000l)
                .build();
        // clothes 엔티티의 재고량을 조절하기 위해선 changeClotehsSizes() 를 활용해야 합니다.
        // 자세한 정보는 Clothes 엔티티를 참고 해 주세요.
        TEST_CLOTHES.changeClothesSize(List.of(TEST_CLOTHES_SIZE_1, TEST_CLOTHES_SIZE_2));
        given(clothesRepository.findById(any()))
                .willReturn(Optional.of(TEST_CLOTHES));

        assertThat(TEST_CLOTHES.getStockQuantity(), is(30l));

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
        TEST_ORDER_REQUEST.setRecipientName("testRecipient");
        TEST_ORDER_REQUEST.setRecipientPhone("1111111111");
        TEST_ORDER_REQUEST.setUsedMileage(1000l);

        //when
        orderService.order(TEST_ORDER_REQUEST, TEST_MEMBER);

        //then
        verify(orderRepository, atLeastOnce()).save(any());
        verify(mileageService, atLeastOnce()).deductMileage(any(), any(), any(), any());
        assertThat(TEST_CLOTHES.getStockQuantity(), is(20l));
        assertThat(TEST_CLOTHES_SIZE_1.getStockQuantity(), is(10l));
        assertThat(TEST_CLOTHES_SIZE_2.getStockQuantity(), is(10l));
    }


    @Test
    public void 주문취소_테스트() throws Exception {
        //given
        Order TEST_ORDER = mock(Order.class);

        given(orderRepository.findByOrderNumAndAuthId(any(), any()))
                .willReturn(Optional.of(TEST_ORDER));

        doNothing().when(TEST_ORDER).cancelOrder();

        //when
        orderService.cancelMyOrder(any(), any());

        //then
        verify(orderRepository, atLeastOnce()).findByOrderNumAndAuthId(any(), any());
        verify(TEST_ORDER, atLeastOnce()).cancelOrder();
        verify(mileageService,atLeastOnce()).deleteMileage(any());
    }

    @Test
    public void 현재_자신의_주문_목록_페이징_테스트() throws Exception {
        //given
        ItemImage TEST_ITEM_IMAGE = ItemImage.builder()
                .build();

        ClothesSize TEST_CLOTHES_SIZE = ClothesSize.builder()
                .stockQuantity(10l)
                .build();

        Clothes TEST_CLOTHES = Clothes.builder()
                .name("testClothesName")
                .price(1000l)
                .build();
        TEST_CLOTHES.changeItemSizes(List.of(TEST_CLOTHES_SIZE));
        TEST_CLOTHES.changeItemImages(List.of(TEST_ITEM_IMAGE));

        OrderItem TEST_ORDER_CLOTHES = new OrderItem(10l, TEST_CLOTHES, TEST_CLOTHES_SIZE);

        Order TEST_ORDER = Order.builder()
                .build();

        TEST_ORDER.changeOrderItems(List.of(TEST_ORDER_CLOTHES));
        Page<Order> TEST_PAGE_RESPONSE = new PageImpl<>(List.of(TEST_ORDER));

        given(orderRepository.getOrderListByAuthId(any(), any(), any()))
                .willReturn(TEST_PAGE_RESPONSE);

        PageRequest TEST_PAGE_REQUEST = PageRequest.of(1, 10);

        //when
        Page<Order> page = orderService.getMyOrderSummaryList("testAuthId", TEST_PAGE_REQUEST);

        //then
        verify(orderRepository, atLeastOnce()).getOrderListByAuthId(any(), any(), any());
    }

    @Test
    public void 상세_주문_내역_테스트() throws Exception {
        //given
        ClothesSize TEST_CLOTHES_SIZE = ClothesSize.builder()
                .stockQuantity(20l)
                .build();
        ItemImage TEST_ITEM_IMAGE = ItemImage.builder().build();
        Clothes TEST_CLOTHES = Clothes.builder()
                .name("testClothesName")
                .color(Color.RED)
                .price(1000l)
                .build();

        TEST_CLOTHES.changeItemImages(List.of(TEST_ITEM_IMAGE));
        TEST_CLOTHES.changeItemSizes(List.of(TEST_CLOTHES_SIZE));

        OrderItem TEST_ORDER_CLOTHES = new OrderItem(10l, TEST_CLOTHES, TEST_CLOTHES_SIZE);

        Order TEST_ORDER = Order.builder()
                .orderNum("testOrderNum")
                .delivery(Delivery.builder()
                        .address(Address.builder()
                                .postCode("65000")
                                .build())
                        .recipient("testRecipient")
                        .recipientPhone("000-0000-0000")
                        .deliveryMemo("testDeliveryMemo")
                        .build())
                .ordererInfo(OrdererInfo.builder()
                        .ordererName("testOrdererName")
                        .ordererEmail("test@test.test")
                        .ordererPhone("000-0000-0000")
                        .build())
                .build();
        TEST_ORDER.changeOrderItems(List.of(TEST_ORDER_CLOTHES));

        // OrderStatus 는 insert 시점에 자동으로 설정되기 때문에
        // 테스트를 위해서 따로 값을 추가.
        ReflectionTestUtils.setField(TEST_ORDER, "orderStatus", OrderStatus.ORDER_TEMP);

        given(orderRepository.findByOrderNumAndAuthId("testAuthId", "testOrderNum"))
                .willReturn(Optional.of(TEST_ORDER));

        PaymentDTO TEST_PAYMENT_DTO = PaymentDTO.builder()
                .payMethod("card")
                .build();
        given(paymentService.getPamentDetail(any()))
                .willReturn(TEST_PAYMENT_DTO);

        MileageDTO TEST_MILEAGE_DTO = new MileageDTO(300l, MileageContent.USED_MILEAGE_DEDUCTION.getDesc());
        given(mileageService.getMileageByOrderNum(any(), any()))
                .willReturn(TEST_MILEAGE_DTO);

        //when
        orderService.getOrderDetailDTO("testAuthId", "testOrderNum");

        //then
        verify(orderRepository, atLeastOnce()).findByOrderNumAndAuthId(any(), any());
        verify(paymentService, atLeastOnce()).getPamentDetail(any());
        verify(mileageService, atLeastOnce()).getMileageByOrderNum(any(), any());
    }

    @Test
    public void 아임포트_결제_주문_유효성검사_주문금액과_결제금액_불일치로인한_결제실패_테스트() throws Exception {
        //given
        ClothesSize TEST_CLOTHES_SIZE = ClothesSize.builder()
                .sizeLabel(SizeLabel.S)
                .stockQuantity(10l)
                .build();

        Clothes TEST_CLOTHES = Clothes.builder()
                .price(3000l)
                .build();

        TEST_CLOTHES.changeItemSizes(List.of(TEST_CLOTHES_SIZE));

        OrderItem TEST_ORDER_CLOTHES = new OrderItem(3l, TEST_CLOTHES, TEST_CLOTHES_SIZE);

        Order TEST_ORDER = Order.builder()
                .orderNum("testOrderNum")
                .delivery(Delivery.builder()
                        .address(Address.builder()
                                .postCode("34000")
                                .build())
                        .build())
                .build();
        TEST_ORDER.changeOrderItems(List.of(TEST_ORDER_CLOTHES));

        // OrderStatus 는 insert 시점에 자동으로 설정되기 때문에
        // 테스트를 위해서 따로 값을 추가.
        ReflectionTestUtils.setField(TEST_ORDER, "orderStatus", OrderStatus.ORDER_TEMP);

        given(orderRepository.findByOrderNumAndAuthId(any(), any()))
                .willReturn(Optional.of(TEST_ORDER));

        MileageDTO TEST_MILEAGE_DTO = new MileageDTO(0l, "");
        given(mileageService.getMileageByOrderNum(TEST_ORDER.getOrderNum(), MileageContent.USED_MILEAGE_DEDUCTION))
                .willReturn(TEST_MILEAGE_DTO);

        //when, then

        assertThrows(PaymentFailException.class, () -> {
            orderService.validatePaymentByIamport(PaymentIamportDTO.builder().paymentAmount(2000l).build(), "testAuthId");
        });
        verify(orderRepository, atLeastOnce()).findByOrderNumAndAuthId(any(),any());
    }

    @Test
    public void 아임포트_결제_주문_유효성검사_성공_테스트() throws Exception {
        //given
        ClothesSize TEST_CLOTHES_SIZE = ClothesSize.builder()
                .sizeLabel(SizeLabel.S)
                .stockQuantity(10l)
                .build();

        Clothes TEST_CLOTHES = Clothes.builder()
                .price(3000l)
                .build();

        TEST_CLOTHES.changeItemSizes(List.of(TEST_CLOTHES_SIZE));

        OrderItem TEST_ORDER_CLOTHES = new OrderItem(3l, TEST_CLOTHES, TEST_CLOTHES_SIZE);

        ClothesSize TEST_CLOTHES_SIZE_2 = ClothesSize.builder()
                .sizeLabel(SizeLabel.M)
                .stockQuantity(10l)
                .build();

        Clothes TEST_CLOTHES_2 = Clothes.builder()
                .price(4000l)
                .build();

        TEST_CLOTHES_2.changeItemSizes(List.of(TEST_CLOTHES_SIZE_2));

        OrderItem TEST_ORDER_CLOTHES_2 = new OrderItem(4l, TEST_CLOTHES_2, TEST_CLOTHES_SIZE_2);

        List<OrderItem> TEST_ORDER_CLOTHES_LIST = List.of(TEST_ORDER_CLOTHES, TEST_ORDER_CLOTHES_2);

        Order TEST_ORDER = Order.builder()
                .orderNum("testOrderNum")
                .delivery(Delivery.builder()
                        .address(Address.builder()
                                .postCode("63500") // 배송비 3000으로 자동 설정.
                                .build())
                        .build())
                .build();

        TEST_ORDER.changeOrderItems(TEST_ORDER_CLOTHES_LIST);

        Long paymentAmount = TEST_ORDER_CLOTHES_LIST.stream()
                .mapToLong(orderItem ->{
                    return orderItem.getOrderQuantity() * orderItem.getItem().getPrice();
                })
                .sum();

        MileageDTO TEST_MILEAGE_DTO = new MileageDTO(-200l, "");
        given(mileageService.getMileageByOrderNum(TEST_ORDER.getOrderNum(), MileageContent.USED_MILEAGE_DEDUCTION))
                .willReturn(TEST_MILEAGE_DTO);

        PaymentIamportDTO TEST_PAYMENT_DTO = PaymentIamportDTO.builder()
                .payStatus("paid")
                .payMethod("card")
                .merchantUid("testOrderNum")
                .impUid("testPaymentNum")
                .paymentAmount(paymentAmount + TEST_ORDER.getDelivery().getDeliveryAmount() + TEST_MILEAGE_DTO.getMileagePoint())
                .build();

        given(orderRepository.findByOrderNumAndAuthId(any(), any()))
                .willReturn(Optional.of(TEST_ORDER));

        // OrderStatus 는 insert 시점에 자동으로 설정되기 때문에
        // 테스트를 위해서 따로 값을 추가.
        ReflectionTestUtils.setField(TEST_ORDER, "orderStatus", OrderStatus.ORDER_TEMP);

        //when
        orderService.validatePaymentByIamport(TEST_PAYMENT_DTO, "testAuthId");

        //then
        verify(orderRepository, atLeastOnce()).findByOrderNumAndAuthId("testAuthId", TEST_PAYMENT_DTO.getMerchantUid());
        verify(mileageService, atLeastOnce()).getMileageByOrderNum(TEST_ORDER.getOrderNum(), MileageContent.USED_MILEAGE_DEDUCTION);
        long accumulatePoint = (long) (TEST_PAYMENT_DTO.getPaymentAmount() * 0.1);
        verify(mileageService, atLeastOnce()).accumulateMileage(TEST_PAYMENT_DTO.getMerchantUid(), "testAuthId", accumulatePoint, MileageContent.PAYMENT_MILEAGE_ACCUMULATE);
        verify(paymentRepository, atLeastOnce()).save(any());
        assertThat(TEST_ORDER.getOrderStatus(), is(OrderStatus.ORDER_COMPLETE));
    }

    @Test
    public void 아임포트_결제_주문_유효성검사_주문상태_부적합으로인한_결제실패_테스트() throws Exception {
        //given
        ClothesSize TEST_CLOTHES_SIZE = ClothesSize.builder()
                .sizeLabel(SizeLabel.S)
                .stockQuantity(10l)
                .build();

        Clothes TEST_CLOTHES = Clothes.builder()
                .price(3000l)
                .build();

        TEST_CLOTHES.changeItemSizes(List.of(TEST_CLOTHES_SIZE));

        OrderItem TEST_ORDER_CLOTHES = new OrderItem(3l, TEST_CLOTHES, TEST_CLOTHES_SIZE);

        Order TEST_ORDER = Order.builder()
                .orderNum("testOrderNum")
                .delivery(Delivery.builder()
                        .address(Address.builder()
                                .postCode("65033")
                                .build())
                        .build())
                .build();

        ReflectionTestUtils.setField(TEST_ORDER, "orderStatus", OrderStatus.ORDER_ITEM_READY);
        List<OrderItem> TEST_ORDER_ITEM_LIST = List.of(TEST_ORDER_CLOTHES);
        long orderItemAmount = TEST_ORDER_ITEM_LIST.stream()
                .mapToLong(orderItem -> {
                    return orderItem.getItem().getPrice() * orderItem.getOrderQuantity();
                }).sum();

        TEST_ORDER.changeOrderItems(TEST_ORDER_ITEM_LIST);

        MileageDTO TEST_MILEAGE_DTO = new MileageDTO(-300l, "");
        given(mileageService.getMileageByOrderNum(TEST_ORDER.getOrderNum(), MileageContent.USED_MILEAGE_DEDUCTION))
                .willReturn(TEST_MILEAGE_DTO);

        PaymentIamportDTO TEST_PAYMENT_DTO = PaymentIamportDTO.builder()
                .payStatus("paid")
                .payMethod("card")
                .merchantUid("testOrderNum")
                .impUid("testPaymentNum")
                .paymentAmount(orderItemAmount + TEST_ORDER.getDelivery().getDeliveryAmount() + TEST_MILEAGE_DTO.getMileagePoint())
                .build();

        given(orderRepository.findByOrderNumAndAuthId(any(), any()))
                .willReturn(Optional.of(TEST_ORDER));


        // OrderStatus 는 insert 시점에 자동으로 설정되기 때문에(자세한 사항은 Order domain 참조)
        // 테스트를 위해서 따로 값을 추가.
        ReflectionTestUtils.setField(TEST_ORDER, "orderStatus", OrderStatus.ORDER_ITEM_READY);

        //when, then
        assertThrows(PaymentFailByValidatedOrderStatusException.class, () -> {
            orderService.validatePaymentByIamport(TEST_PAYMENT_DTO, "testAuthId");
        });
        verify(orderRepository, atLeastOnce()).findByOrderNumAndAuthId("testAuthId", TEST_PAYMENT_DTO.getMerchantUid());
        verify(mileageService, atLeastOnce()).getMileageByOrderNum(TEST_ORDER.getOrderNum(), MileageContent.USED_MILEAGE_DEDUCTION);

    }

    @Test
    public void 임시_주문_삭제_테스트() throws Exception {
        //given

        Clothes TEST_CLOTHES = Clothes.builder()
                .price(1000l)
                .build();

        ClothesSize TEST_CLOTHES_SIZE = ClothesSize.builder()
                .sizeLabel(SizeLabel.S)
                .stockQuantity(10l)
                .build();

        TEST_CLOTHES.changeItemSizes(List.of(TEST_CLOTHES_SIZE));

        Order TEST_TEMP_ORDER = Order.builder()
                .delivery(Delivery.builder()
                        .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                        .address(Address.builder()
                                .postCode("63400")
                                .build())
                        .build())
                .build();

        TEST_TEMP_ORDER.changeOrderItems(List.of(
                new OrderItem(5l, TEST_CLOTHES, TEST_CLOTHES_SIZE)
        ));

        ReflectionTestUtils.setField(TEST_TEMP_ORDER, "orderStatus", OrderStatus.ORDER_TEMP);
        given(orderRepository.findByOrderNumAndAuthId(any(), any()))
                .willReturn(Optional.of(TEST_TEMP_ORDER));

        //when
        orderService.deleteTempOrder(any(), any());

        //then
        verify(orderRepository, atLeastOnce()).delete(any());
        verify(orderRepository, atLeastOnce()).findByOrderNumAndAuthId(any(), any());
        verify(mileageService, atLeastOnce()).deleteMileage(any());
    }


    @Test
    public void 로그인한_회원_임시주문목록_삭제_테스트() throws Exception {
        //given
        Clothes TEST_CLOTHES = Clothes.builder()
                .price(1000l)
                .build();

        ClothesSize TEST_CLOTHES_SIZE = ClothesSize.builder()
                .sizeLabel(SizeLabel.S)
                .stockQuantity(100l)
                .build();

        TEST_CLOTHES.changeItemSizes(List.of(TEST_CLOTHES_SIZE));

        Member TEST_ORDERER = Member.builder()
                .authId("testAuthId")
                .build();

        Order TEST_ORDER = Order.builder()
                .orderer(TEST_ORDERER)
                .delivery(Delivery.builder()
                        .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                        .address(Address.builder()
                                .postCode("65000")
                                .build())
                        .build())
                .build();

        ReflectionTestUtils.setField(TEST_ORDER, "orderStatus", OrderStatus.ORDER_TEMP);

        TEST_ORDER.changeOrderItems(List.of(
                new OrderItem(10l, TEST_CLOTHES, TEST_CLOTHES_SIZE)
        ));

        given(orderRepository.getOrderIdsByAuthId(any(String.class), any()))
                .willReturn(List.of(TEST_ORDER));

        //when
        orderService.deleteAllTempOrder(TEST_ORDERER.getAuthId());

        //then
        verify(orderRepository, atLeastOnce()).getOrderIdsByAuthId(any(), any());
        verify(orderRepository, atLeastOnce()).delete(any());
        verify(mileageService, atLeastOnce()).deleteMileage(any());
    }

    private ItemSize createItemSize() {
        return new ItemSize(SizeLabel.M, 12l);
    }

    private List<ClothesSize> createClothesSizes(){
        return List.of(
                ClothesSize.builder()
                .sizeLabel(SizeLabel.M)
                .stockQuantity(20l)
                .build()

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
