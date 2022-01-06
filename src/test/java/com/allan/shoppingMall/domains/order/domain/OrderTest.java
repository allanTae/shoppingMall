package com.allan.shoppingMall.domains.order.domain;

import com.allan.shoppingMall.common.config.jpa.auditing.JpaAuditingConfig;
import com.allan.shoppingMall.common.exception.order.OrderCancelFailException;
import com.allan.shoppingMall.common.exception.order.payment.PaymentFailByValidatedOrderStatusException;
import com.allan.shoppingMall.common.value.Address;
import com.allan.shoppingMall.domains.delivery.domain.Delivery;
import com.allan.shoppingMall.domains.delivery.domain.DeliveryStatus;
import com.allan.shoppingMall.domains.item.domain.Color;
import com.allan.shoppingMall.domains.item.domain.Item;
import com.allan.shoppingMall.domains.item.domain.clothes.Clothes;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesSize;
import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import com.allan.shoppingMall.domains.member.domain.Gender;
import com.allan.shoppingMall.domains.member.domain.Member;
import com.allan.shoppingMall.domains.member.domain.MemberRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = JpaAuditingConfig.class
))
@WithMockUser
public class OrderTest {

    @Autowired
    TestEntityManager testEntityManager;

    /**
     * 의상 상품의 경우, OrderItem 대신 OrderClothes 가 들어가게 되고,
     * 의상의 경우, 동일한 상품 아이디와 동일한 사이즈의 제품이 주문되는 경우는 없기때문에
     * itemId, SizeLabel을 통해 상품의 동일성 여부를 판단하고, 동일하다고 판단하는 경우 동일 제품을 주문하지 않는다.
     */
    @Test
    public void 주문시_옷_상품_중복_추가_테스트() throws Exception {
        //given
        Member TEST_ORDERER = createMember();
        ClothesSize TEST_CLOTHES_SIZE1 = createClothesSize(SizeLabel.S, 15l);
        ClothesSize TEST_CLOTHES_SIZE2 = createClothesSize(SizeLabel.M, 15l);
        Clothes TEST_CLOTHES = createClothes(List.of(TEST_CLOTHES_SIZE1, TEST_CLOTHES_SIZE2));

        Order TEST_ORDER = createOrderByMember(TEST_ORDERER);
        TEST_ORDER.changeOrderItems(List.of(new OrderClothes(2l, TEST_CLOTHES, TEST_CLOTHES_SIZE1),
                                            new OrderClothes(2l, TEST_CLOTHES, TEST_CLOTHES_SIZE2)));
        testEntityManager.persist(TEST_ORDER);

        //when
        TEST_ORDER.changeOrderItems(List.of(new OrderClothes(2l, TEST_CLOTHES, TEST_CLOTHES_SIZE1)));

        //then
        assertThat(TEST_ORDER.getOrderItems().size(), is(2));
        OrderClothes orderClothes1 = (OrderClothes) TEST_ORDER.getOrderItems().get(0);
        OrderClothes orderClothes2 = (OrderClothes) TEST_ORDER.getOrderItems().get(1);
        assertThat(orderClothes1.getClothesSize().getSizeLabel(), is(SizeLabel.S));
        assertThat(orderClothes2.getClothesSize().getSizeLabel(), is(SizeLabel.M));
    }

    /**
     * 결제를 완료한 상품준비중 상태의 주문을 취소하는 테스트입니다.
     */
    @Test
    public void 임시저장된_주문_결제_및_취소_테스트() throws Exception {
        //given
        Member TEST_ORDERER = createMember();
        ClothesSize TEST_CLOTHES_SIZE1 = createClothesSize(SizeLabel.S, 15l);
        ClothesSize TEST_CLOTHES_SIZE2 = createClothesSize(SizeLabel.M, 15l);
        Clothes TEST_CLOTHES = createClothes(List.of(TEST_CLOTHES_SIZE1, TEST_CLOTHES_SIZE2));

        Order TEST_ORDER = createOrderByMember(TEST_ORDERER);
        TEST_ORDER.changeOrderItems(List.of(new OrderClothes(2l, TEST_CLOTHES, TEST_CLOTHES_SIZE1),
                                            new OrderClothes(2l, TEST_CLOTHES, TEST_CLOTHES_SIZE2)));

        testEntityManager.persist(TEST_ORDERER);
        testEntityManager.persist(TEST_CLOTHES);
        testEntityManager.persist(TEST_ORDER);
        testEntityManager.flush();
        testEntityManager.clear();

        // 주문 시 재고량 확인.
        assertThat(TEST_CLOTHES.getStockQuantity(), is(26l));
        assertThat(TEST_CLOTHES_SIZE1.getStockQuantity(), is(13l));
        assertThat(TEST_CLOTHES_SIZE2.getStockQuantity(), is(13l));

        // 주문 결제.
        TEST_ORDER.payOrder();
        assertThat(TEST_ORDER.getOrderStatus(), is(OrderStatus.ORDER_ITEM_READY));

        //when
        TEST_ORDER.cancelOrder();

        //then
        // 주문 취소 시 재고량 복구 확인.
        assertThat(TEST_CLOTHES.getStockQuantity(), is(30l));
        assertThat(TEST_CLOTHES_SIZE1.getStockQuantity(), is(15l));
        assertThat(TEST_CLOTHES_SIZE2.getStockQuantity(), is(15l));
    }

    /**
     * 임시저장 상태의 주문을 취소하는 테스트 입니다.
     */
    @Test
    public void 임시저장_주문_취소_테스트() throws Exception {
        //given
        Member TEST_ORDERER = createMember();
        ClothesSize TEST_CLOTHES_SIZE1 = createClothesSize(SizeLabel.S, 15l);
        ClothesSize TEST_CLOTHES_SIZE2 = createClothesSize(SizeLabel.M, 15l);
        Clothes TEST_CLOTHES = createClothes(List.of(TEST_CLOTHES_SIZE1, TEST_CLOTHES_SIZE2));

        Order TEST_ORDER = createOrderByMember(TEST_ORDERER);
        TEST_ORDER.changeOrderItems(List.of(new OrderClothes(2l, TEST_CLOTHES, TEST_CLOTHES_SIZE1),
                new OrderClothes(2l, TEST_CLOTHES, TEST_CLOTHES_SIZE2)));

        testEntityManager.persist(TEST_ORDERER);
        testEntityManager.persist(TEST_CLOTHES);
        testEntityManager.persist(TEST_ORDER);
        testEntityManager.flush();
        testEntityManager.clear();
        assertThat(TEST_CLOTHES.getStockQuantity(), is(26l));
        assertThat(TEST_CLOTHES_SIZE1.getStockQuantity(), is(13l));
        assertThat(TEST_CLOTHES_SIZE2.getStockQuantity(), is(13l));

        //when
        TEST_ORDER.cancelOrder();

        //then
        assertThat(TEST_CLOTHES.getStockQuantity(), is(30l));
        assertThat(TEST_CLOTHES_SIZE1.getStockQuantity(), is(15l));
        assertThat(TEST_CLOTHES_SIZE2.getStockQuantity(), is(15l));
    }

    @Test
    public void 주문_취소_실패_테스트() throws Exception {
        //given
        Member TEST_ORDERER = createMember();
        ClothesSize TEST_CLOTHES_SIZE1 = createClothesSize(SizeLabel.S, 15l);
        ClothesSize TEST_CLOTHES_SIZE2 = createClothesSize(SizeLabel.M, 15l);
        Clothes TEST_CLOTHES = createClothes(List.of(TEST_CLOTHES_SIZE1, TEST_CLOTHES_SIZE2));

        Order TEST_ORDER = createOrderByMember(TEST_ORDERER);
        TEST_ORDER.changeOrderItems(List.of(new OrderClothes(2l, TEST_CLOTHES, TEST_CLOTHES_SIZE1),
                new OrderClothes(2l, TEST_CLOTHES, TEST_CLOTHES_SIZE2)));

        testEntityManager.persist(TEST_ORDERER);
        testEntityManager.persist(TEST_CLOTHES);
        testEntityManager.persist(TEST_ORDER);
        testEntityManager.flush();
        testEntityManager.clear();

        // 주문취소 불가능한 상태로 주문상태 변경.
        ReflectionTestUtils.setField(TEST_ORDER, "orderStatus", OrderStatus.ORDER_READY);

        //when, then
        assertThrows(OrderCancelFailException.class, () ->{
            TEST_ORDER.cancelOrder();
        });
    }

    @Test
    public void 결제_실패_테스트() throws Exception {
        //given
        Member TEST_ORDERER = createMember();
        ClothesSize TEST_CLOTHES_SIZE1 = createClothesSize(SizeLabel.S, 15l);
        ClothesSize TEST_CLOTHES_SIZE2 = createClothesSize(SizeLabel.M, 15l);
        Clothes TEST_CLOTHES = createClothes(List.of(TEST_CLOTHES_SIZE1, TEST_CLOTHES_SIZE2));

        Order TEST_ORDER = createOrderByMember(TEST_ORDERER);
        TEST_ORDER.changeOrderItems(List.of(new OrderClothes(2l, TEST_CLOTHES, TEST_CLOTHES_SIZE1),
                new OrderClothes(2l, TEST_CLOTHES, TEST_CLOTHES_SIZE2)));

        testEntityManager.persist(TEST_ORDERER);
        testEntityManager.persist(TEST_CLOTHES);
        testEntityManager.persist(TEST_ORDER);
        testEntityManager.flush();
        testEntityManager.clear();

        // 결제 불가능한 상태로 주문상태 변경.
        ReflectionTestUtils.setField(TEST_ORDER, "orderStatus", OrderStatus.ORDER_READY);

        //when, then
        assertThrows(PaymentFailByValidatedOrderStatusException.class, () ->{
            TEST_ORDER.payOrder();
        });
    }

    private Member createMember() {
        return Member.builder()
                .name("testMemberName")
                .age(10)
                .role(MemberRole.ACTIVATED_USER)
                .email("testEmail")
                .phone("000-0000-0000")
                .nickName("testNickName")
                .authId("testAuthId")
                .pwd("testPwd")
                .address(new Address("", "", "", "", ""))
                .dateOfBirth("2000-10-10")
                .milege(10l)
                .gender(Gender.MAN)
                .build();
    }

    private List<OrderItem> createdOrderItems(Item item
    ){
        return List.of(
                // 주문량 10개 아이템.
                new OrderItem(10l, item)
        );
    }


    private Clothes createClothes(List<ClothesSize> clothesSizes){
        Clothes clothes = Clothes.builder()
                .name("testClothesName")
                .price(100l)
                .engName("testEngname")
                .color(Color.RED)
                .build();



        clothes.changeClothesSizes(clothesSizes);

        return clothes;
    }

    private ClothesSize createClothesSize(SizeLabel sizeLabel, Long stockQuantity){
        return ClothesSize.builder()
                .sizeLabel(sizeLabel)
                .stockQuantity(stockQuantity)
                .build();
    }

    private Order createOrderByMember() {
        Order order = Order.builder()
                .orderer(createMember())
                .ordererInfo(OrdererInfo.builder()
                        .ordererName("testOrdererName")
                        .ordererEmail("test@email.email")
                        .ordererPhone("010-2222-2222")
                        .build())
                .delivery(Delivery.builder()
                        .address(new Address("", "", "", "", ""))
                        .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                        .deliveryMemo("testDeliveryMemeo")
                        .recipient("testRecipient")
                        .recipientPhone("000-0000-0000")
                        .build())
                .build();

        return order;
    }

    private Order createOrderByMember(Member orderer) {
        Order order = Order.builder()
                .orderer(orderer)
                .ordererInfo(OrdererInfo.builder()
                        .ordererName("testOrdererName")
                        .ordererEmail("test@email.email")
                        .ordererPhone("010-2020-2020")
                        .build())
                .delivery(Delivery.builder()
                        .address(new Address("", "", "", "", ""))
                        .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                        .deliveryMemo("testDeliveryMemo")
                        .recipient("testRecipient")
                        .recipientPhone("000-0000-0000")
                        .build())
                .build();

        return order;
    }
}
