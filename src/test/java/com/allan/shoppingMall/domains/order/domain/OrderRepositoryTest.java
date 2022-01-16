package com.allan.shoppingMall.domains.order.domain;

import com.allan.shoppingMall.common.config.jpa.auditing.JpaAuditingConfig;
import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.order.OrderNotFoundException;
import com.allan.shoppingMall.common.value.Address;
import com.allan.shoppingMall.domains.delivery.domain.Delivery;
import com.allan.shoppingMall.domains.delivery.domain.DeliveryStatus;
import com.allan.shoppingMall.domains.item.domain.Color;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JpaAuditingConfig.class
        )
)
@WithMockUser
public class OrderRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 회원아이디로_주문_조회_테스트() throws Exception {
        //given
        Member TEST_MEMBER_1 = Member.builder()
                .name("testName")
                .authId("testId")
                .pwd("testPwd")
                .age(10)
                .gender(Gender.MAN)
                .email("testEmail@email")
                .phone("000-0000-0000")
                .dateOfBirth("1993-09-09")
                .role(MemberRole.ACTIVATED_USER)
                .address(new Address("", "", "", "", ""))
                .build();

        Member TEST_MEMBER_2 = Member.builder()
                .name("testName2")
                .authId("testId2")
                .pwd("testPwd2")
                .age(12)
                .gender(Gender.MAN)
                .email("testEmail@email2")
                .phone("000-0000-0000")
                .dateOfBirth("1993-09-09")
                .role(MemberRole.ACTIVATED_USER)
                .address(new Address("", "", "", "", ""))
                .build();

        Clothes TEST_CLOTHES1 = createClothes(1000l, "testClothesName", "testClothesEngName", Color.RED);

        Clothes TEST_CLOTHES2 = createClothes(100l, "testClothesName2", "testClothesEngName2", Color.BLUE);

        ClothesSize TEST_CLOTEHS_SIZE_1 = createClothesSize(SizeLabel.S, 10l);
        ClothesSize TEST_CLOTEHS_SIZE_2 = createClothesSize(SizeLabel.M, 20l);
        ClothesSize TEST_CLOTEHS_SIZE_3 = createClothesSize(SizeLabel.L, 30l);

        ClothesSize TEST_CLOTHES_SIZE_4 = createClothesSize(SizeLabel.S, 100l);
        ClothesSize TEST_CLOTHES_SIZE_5 = createClothesSize(SizeLabel.M, 200l);
        ClothesSize TEST_CLOTHES_SIZE_6 = createClothesSize(SizeLabel.L, 300l);

        TEST_CLOTHES1.changeClothesSizes(List.of(TEST_CLOTEHS_SIZE_1, TEST_CLOTEHS_SIZE_2, TEST_CLOTEHS_SIZE_3));
        TEST_CLOTHES2.changeClothesSizes(List.of(TEST_CLOTHES_SIZE_4, TEST_CLOTHES_SIZE_5, TEST_CLOTHES_SIZE_6));

        Order TEST_ORDER = Order.builder()
                .orderer(TEST_MEMBER_1)
                .delivery(Delivery.builder()
                        .deliveryMemo("testMemo1")
                        .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                        .address(new Address("", "", "65700", "", ""))
                        .recipient("testRecipient")
                        .recipientPhone("000-0000-0000")
                        .build())
                .ordererInfo(
                        OrdererInfo.builder()
                                .ordererName("testOrdererName1")
                                .ordererEmail("testOrdererEmail1")
                                .ordererPhone("000-0000-0000")
                                .build()
                )
                .build();

        TEST_ORDER.changeOrderItems(List.of(
                new OrderClothes(10l, TEST_CLOTHES1, TEST_CLOTEHS_SIZE_1),
                new OrderClothes(20l, TEST_CLOTHES1, TEST_CLOTEHS_SIZE_2)));

        Order TEST_ORDER_2 = Order.builder()
                .orderer(TEST_MEMBER_2)
                .delivery(Delivery.builder()
                        .deliveryMemo("testMemo2")
                        .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                        .address(new Address("", "", "65232", "", ""))
                        .recipient("testRecipient")
                        .recipientPhone("000-0000-0000")
                        .build())
                .ordererInfo(
                        OrdererInfo.builder()
                                .ordererName("testOrdererName2")
                                .ordererEmail("testOrdererEmail2")
                                .ordererPhone("000-0000-0000")
                                .build()
                )
                .build();

        TEST_ORDER_2.changeOrderItems(List.of(
                new OrderClothes(100l, TEST_CLOTHES2, TEST_CLOTHES_SIZE_4)
        ));

        Order TEST_ORDER_3 = Order.builder()
                .orderer(TEST_MEMBER_1)
                .delivery(Delivery.builder()
                        .deliveryMemo("testMemo3")
                        .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                        .address(new Address("", "", "65000", "", ""))
                        .recipient("testRecipient")
                        .recipientPhone("000-0000-0000")
                        .build())
                .ordererInfo(
                        OrdererInfo.builder()
                                .ordererName("testOrdererName2")
                                .ordererEmail("testOrdererEmail2")
                                .ordererPhone("000-0000-0000")
                                .build()
                )
                .build();

        TEST_ORDER_3.changeOrderItems(List.of(
                new OrderClothes(100l, TEST_CLOTHES2, TEST_CLOTHES_SIZE_5)
        ));

        Order TEST_ORDER_4 = Order.builder()
                .orderer(TEST_MEMBER_1)
                .delivery(Delivery.builder()
                        .deliveryMemo("testMemo3")
                        .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                        .address(new Address("", "", "65000", "", ""))
                        .recipient("testRecipient")
                        .recipientPhone("000-0000-0000")
                        .build())
                .ordererInfo(
                        OrdererInfo.builder()
                                .ordererName("testOrdererName2")
                                .ordererEmail("testOrdererEmail2")
                                .ordererPhone("000-0000-0000")
                                .build()
                )
                .build();

        TEST_ORDER_4.changeOrderItems(List.of(
                new OrderClothes(10l, TEST_CLOTHES2, TEST_CLOTHES_SIZE_5)
        ));

        Order TEST_ORDER_5 = Order.builder()
                .orderer(TEST_MEMBER_1)
                .delivery(Delivery.builder()
                        .deliveryMemo("testMemo3")
                        .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                        .address(new Address("", "", "65000", "", ""))
                        .recipient("testRecipient")
                        .recipientPhone("000-0000-0000")
                        .build())
                .ordererInfo(
                        OrdererInfo.builder()
                                .ordererName("testOrdererName2")
                                .ordererEmail("testOrdererEmail2")
                                .ordererPhone("000-0000-0000")
                                .build()
                )
                .build();

        TEST_ORDER_5.changeOrderItems(List.of(
                new OrderClothes(10l, TEST_CLOTHES2, TEST_CLOTHES_SIZE_5)
        ));

        Order TEST_ORDER_6 = Order.builder()
                .orderer(TEST_MEMBER_1)
                .delivery(Delivery.builder()
                        .deliveryMemo("testMemo3")
                        .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                        .address(new Address("", "", "65000", "", ""))
                        .recipient("testRecipient")
                        .recipientPhone("000-0000-0000")
                        .build())
                .ordererInfo(
                        OrdererInfo.builder()
                                .ordererName("testOrdererName2")
                                .ordererEmail("testOrdererEmail2")
                                .ordererPhone("000-0000-0000")
                                .build()
                )
                .build();

        TEST_ORDER_6.changeOrderItems(List.of(
                new OrderClothes(10l, TEST_CLOTHES2, TEST_CLOTHES_SIZE_5)
        ));

        Order TEST_ORDER_7 = Order.builder()
                .orderer(TEST_MEMBER_1)
                .delivery(Delivery.builder()
                        .deliveryMemo("testMemo3")
                        .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                        .address(new Address("", "", "65000", "", ""))
                        .recipient("testRecipient")
                        .recipientPhone("000-0000-0000")
                        .build())
                .ordererInfo(
                        OrdererInfo.builder()
                                .ordererName("testOrdererName2")
                                .ordererEmail("testOrdererEmail2")
                                .ordererPhone("000-0000-0000")
                                .build()
                )
                .build();

        TEST_ORDER_7.changeOrderItems(List.of(
                new OrderClothes(10l, TEST_CLOTHES2, TEST_CLOTHES_SIZE_5)
        ));


        testEntityManager.persist(TEST_MEMBER_1);
        testEntityManager.persist(TEST_MEMBER_2);
        testEntityManager.persist(TEST_CLOTHES1);
        testEntityManager.persist(TEST_CLOTHES2);

        testEntityManager.persist(TEST_ORDER);

        testEntityManager.persist(TEST_ORDER_2);

        testEntityManager.persist(TEST_ORDER_3);

        testEntityManager.persist(TEST_ORDER_4);

        testEntityManager.persist(TEST_ORDER_5);

        testEntityManager.persist(TEST_ORDER_6);

        testEntityManager.persist(TEST_ORDER_7);

        // TEST_ORDER_2 => 주문자. TEST_MEMBER_2
        // TEST_ORDER_2 를 제외한 모든 주문들 => 주문자. TEST_MEMBER_1
        testEntityManager.flush();
        testEntityManager.clear();

        //when
        // TEST_MEMBER_1 이 주문한 주문들만 조회.
        Page<Order> page = orderRepository.getOrderListByAuthId(TEST_MEMBER_1.getAuthId(), PageRequest.of(0, 3,  Sort.by(Sort.Direction.ASC, "createdDate")));

        //then
        List<Order> orders = page.getContent();
        assertThat(orders.size(), is(3)); // 3개만 페이징 했는가.?
        assertThat(page.getNumber(), is(0)); // 첫페이지가 맞는가.? (jpa paging 은 0번 페이지부터 시작)
        assertThat(page.getTotalPages(), is(2)); // 총 2페이지가 맞는가.? (jpa paging 0번 부터 시작하기 때문에 실제 페이지-1 한것이 페이지가 됩니다.)
        assertThat(orders.get(0).getOrderId(), is(TEST_ORDER.getOrderId()));
        assertThat(orders.get(0).getOrderer().getAuthId(), is(TEST_MEMBER_1.getAuthId()));
        assertThat(orders.get(0).getOrderItems().get(0).getItem().getName(), is(TEST_ORDER.getOrderItems().get(0).getItem().getName()));

        assertThat(orders.get(1).getOrderId(), is(TEST_ORDER_3.getOrderId()));
        assertThat(orders.get(1).getOrderer().getAuthId(), is(TEST_MEMBER_1.getAuthId()));
        assertThat(orders.get(1).getOrderItems().get(0).getItem().getName(), is(TEST_ORDER_3.getOrderItems().get(0).getItem().getName()));

        assertThat(orders.get(2).getOrderId(), is(TEST_ORDER_4.getOrderId()));
        assertThat(orders.get(2).getOrderer().getAuthId(), is(TEST_MEMBER_1.getAuthId()));
        assertThat(orders.get(2).getOrderItems().get(0).getItem().getName(), is(TEST_ORDER_4.getOrderItems().get(0).getItem().getName()));
    }

    @Test
    public void 로그인_아이디와_주문번호로_주문조회_성공_테스트() throws Exception {
        //given
        Member TEST_MEMBER_1 = Member.builder()
                .name("testName")
                .authId("testId")
                .pwd("testPwd")
                .age(10)
                .gender(Gender.MAN)
                .email("testEmail@email")
                .phone("000-0000-0000")
                .dateOfBirth("1993-09-09")
                .role(MemberRole.ACTIVATED_USER)
                .address(new Address("", "", "", "", ""))
                .build();

        Member TEST_MEMBER_2 = Member.builder()
                .name("testName2")
                .authId("testId2")
                .pwd("testPwd2")
                .age(12)
                .gender(Gender.MAN)
                .email("testEmail@email2")
                .phone("000-0000-0000")
                .dateOfBirth("1993-09-09")
                .role(MemberRole.ACTIVATED_USER)
                .address(new Address("", "", "", "", ""))
                .build();

        Clothes TEST_CLOTHES1 = createClothes(1000l, "testClothesName", "testClothesEngName", Color.RED);

        Clothes TEST_CLOTHES2 = createClothes(100l, "testClothesName2", "testClothesEngName2", Color.BLUE);

        ClothesSize TEST_CLOTEHS_SIZE_1 = createClothesSize(SizeLabel.S, 10l);
        ClothesSize TEST_CLOTEHS_SIZE_2 = createClothesSize(SizeLabel.M, 20l);
        ClothesSize TEST_CLOTEHS_SIZE_3 = createClothesSize(SizeLabel.L, 30l);

        ClothesSize TEST_CLOTHES_SIZE_4 = createClothesSize(SizeLabel.S, 100l);
        ClothesSize TEST_CLOTHES_SIZE_5 = createClothesSize(SizeLabel.M, 200l);
        ClothesSize TEST_CLOTHES_SIZE_6 = createClothesSize(SizeLabel.L, 300l);

        TEST_CLOTHES1.changeClothesSizes(List.of(TEST_CLOTEHS_SIZE_1, TEST_CLOTEHS_SIZE_2, TEST_CLOTEHS_SIZE_3));
        TEST_CLOTHES2.changeClothesSizes(List.of(TEST_CLOTHES_SIZE_4, TEST_CLOTHES_SIZE_5, TEST_CLOTHES_SIZE_6));

        Order TEST_ORDER = Order.builder()
                .orderer(TEST_MEMBER_1)
                .delivery(Delivery.builder()
                        .deliveryMemo("testMemo1")
                        .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                        .address(new Address("", "", "65000", "", ""))
                        .recipient("testRecipient")
                        .recipientPhone("000-0000-0000")
                        .build())
                .ordererInfo(
                        OrdererInfo.builder()
                                .ordererName("testOrdererName1")
                                .ordererEmail("testOrdererEmail1")
                                .ordererPhone("000-0000-0000")
                                .build()
                )
                .build();

        TEST_ORDER.changeOrderItems(List.of(
                new OrderClothes(10l, TEST_CLOTHES1, TEST_CLOTEHS_SIZE_1),
                new OrderClothes(20l, TEST_CLOTHES1, TEST_CLOTEHS_SIZE_2)));

        Order TEST_ORDER_2 = Order.builder()
                .orderer(TEST_MEMBER_2)
                .delivery(Delivery.builder()
                        .deliveryMemo("testMemo2")
                        .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                        .address(new Address("", "", "65000", "", ""))
                        .recipient("testRecipient")
                        .recipientPhone("000-0000-0000")
                        .build())
                .ordererInfo(
                        OrdererInfo.builder()
                                .ordererName("testOrdererName2")
                                .ordererEmail("testOrdererEmail2")
                                .ordererPhone("000-0000-0000")
                                .build()
                )
                .build();

        TEST_ORDER_2.changeOrderItems(List.of(
                new OrderClothes(100l, TEST_CLOTHES2, TEST_CLOTHES_SIZE_4)
        ));

        testEntityManager.persist(TEST_MEMBER_1);
        testEntityManager.persist(TEST_MEMBER_2);
        testEntityManager.persist(TEST_CLOTHES1);
        testEntityManager.persist(TEST_CLOTHES2);
        testEntityManager.persist(TEST_ORDER);
        testEntityManager.persist(TEST_ORDER_2);

        //when
        Order findOrder = orderRepository.findByOrderNumAndAuthId(TEST_MEMBER_1.getAuthId(), TEST_ORDER.getOrderNum()).get();

        //then
        assertThat(findOrder.getOrderNum(), is(TEST_ORDER.getOrderNum()));
        assertThat(findOrder.getOrderer().getAuthId(), is(TEST_MEMBER_1.getAuthId()));
    }

    @Test
    public void 존재하지_회원아이디_주문번호_조회실패_테스트() throws Exception {
        // given
        Member TEST_MEMBER_1 = Member.builder()
                .name("testName")
                .authId("testId")
                .pwd("testPwd")
                .age(10)
                .gender(Gender.MAN)
                .email("testEmail@email")
                .phone("000-0000-0000")
                .dateOfBirth("1993-09-09")
                .role(MemberRole.ACTIVATED_USER)
                .address(new Address("", "", "", "", ""))
                .build();

        Member TEST_MEMBER_2 = Member.builder()
                .name("testName2")
                .authId("testId2")
                .pwd("testPwd2")
                .age(12)
                .gender(Gender.MAN)
                .email("testEmail@email2")
                .phone("000-0000-0000")
                .dateOfBirth("1993-09-09")
                .role(MemberRole.ACTIVATED_USER)
                .address(new Address("", "", "", "", ""))
                .build();

        Clothes TEST_CLOTHES1 = createClothes(1000l, "testClothesName", "testClothesEngName", Color.RED);

        Clothes TEST_CLOTHES2 = createClothes(100l, "testClothesName2", "testClothesEngName2", Color.BLUE);

        ClothesSize TEST_CLOTEHS_SIZE_1 = createClothesSize(SizeLabel.S, 10l);
        ClothesSize TEST_CLOTEHS_SIZE_2 = createClothesSize(SizeLabel.M, 20l);
        ClothesSize TEST_CLOTEHS_SIZE_3 = createClothesSize(SizeLabel.L, 30l);

        ClothesSize TEST_CLOTHES_SIZE_4 = createClothesSize(SizeLabel.S, 100l);
        ClothesSize TEST_CLOTHES_SIZE_5 = createClothesSize(SizeLabel.M, 200l);
        ClothesSize TEST_CLOTHES_SIZE_6 = createClothesSize(SizeLabel.L, 300l);

        TEST_CLOTHES1.changeClothesSizes(List.of(TEST_CLOTEHS_SIZE_1, TEST_CLOTEHS_SIZE_2, TEST_CLOTEHS_SIZE_3));
        TEST_CLOTHES2.changeClothesSizes(List.of(TEST_CLOTHES_SIZE_4, TEST_CLOTHES_SIZE_5, TEST_CLOTHES_SIZE_6));

        Order TEST_ORDER = Order.builder()
                .orderer(TEST_MEMBER_1)
                .delivery(Delivery.builder()
                        .deliveryMemo("testMemo1")
                        .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                        .address(new Address("", "", "65999", "", ""))
                        .recipient("testRecipient")
                        .recipientPhone("000-0000-0000")
                        .build())
                .ordererInfo(
                        OrdererInfo.builder()
                                .ordererName("testOrdererName1")
                                .ordererEmail("testOrdererEmail1")
                                .ordererPhone("000-0000-0000")
                                .build()
                )
                .build();

        TEST_ORDER.changeOrderItems(List.of(
                new OrderClothes(10l, TEST_CLOTHES1, TEST_CLOTEHS_SIZE_1),
                new OrderClothes(20l, TEST_CLOTHES1, TEST_CLOTEHS_SIZE_2)));

        Order TEST_ORDER_2 = Order.builder()
                .orderer(TEST_MEMBER_2)
                .delivery(Delivery.builder()
                        .deliveryMemo("testMemo2")
                        .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                        .address(new Address("", "", "65033", "", ""))
                        .recipient("testRecipient")
                        .recipientPhone("000-0000-0000")
                        .build())
                .ordererInfo(
                        OrdererInfo.builder()
                                .ordererName("testOrdererName2")
                                .ordererEmail("testOrdererEmail2")
                                .ordererPhone("000-0000-0000")
                                .build()
                )
                .build();

        TEST_ORDER_2.changeOrderItems(List.of(
                new OrderClothes(100l, TEST_CLOTHES2, TEST_CLOTHES_SIZE_4)
        ));

        testEntityManager.persist(TEST_MEMBER_1);
        testEntityManager.persist(TEST_MEMBER_2);
        testEntityManager.persist(TEST_CLOTHES1);
        testEntityManager.persist(TEST_CLOTHES2);
        testEntityManager.persist(TEST_ORDER);
        testEntityManager.persist(TEST_ORDER_2);

        //when, then
        assertThrows(OrderNotFoundException.class, () -> {
            orderRepository.findByOrderNumAndAuthId("invalid_authId", TEST_ORDER.getOrderNum()).orElseThrow(()
                -> new OrderNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        });
    }

    @Test
    public void 로그인한_회원의_주문번호_목록반환_테스트() throws Exception {
        // given
        Member TEST_MEMBER_1 = Member.builder()
                .name("testName")
                .authId("testId")
                .pwd("testPwd")
                .age(10)
                .gender(Gender.MAN)
                .email("testEmail@email")
                .phone("000-0000-0000")
                .dateOfBirth("1993-09-09")
                .role(MemberRole.ACTIVATED_USER)
                .address(new Address("", "", "", "", ""))
                .build();

        Member TEST_MEMBER_2 = Member.builder()
                .name("testName2")
                .authId("testId2")
                .pwd("testPwd2")
                .age(12)
                .gender(Gender.MAN)
                .email("testEmail@email2")
                .phone("000-0000-0000")
                .dateOfBirth("1993-09-09")
                .role(MemberRole.ACTIVATED_USER)
                .address(new Address("", "", "", "", ""))
                .build();

        Clothes TEST_CLOTHES1 = createClothes(1000l, "testClothesName", "testClothesEngName", Color.RED);

        Clothes TEST_CLOTHES2 = createClothes(100l, "testClothesName2", "testClothesEngName2", Color.BLUE);

        ClothesSize TEST_CLOTEHS_SIZE_1 = createClothesSize(SizeLabel.S, 10l);
        ClothesSize TEST_CLOTEHS_SIZE_2 = createClothesSize(SizeLabel.M, 20l);
        ClothesSize TEST_CLOTEHS_SIZE_3 = createClothesSize(SizeLabel.L, 30l);

        ClothesSize TEST_CLOTHES_SIZE_4 = createClothesSize(SizeLabel.S, 100l);
        ClothesSize TEST_CLOTHES_SIZE_5 = createClothesSize(SizeLabel.M, 200l);
        ClothesSize TEST_CLOTHES_SIZE_6 = createClothesSize(SizeLabel.L, 300l);

        TEST_CLOTHES1.changeClothesSizes(List.of(TEST_CLOTEHS_SIZE_1, TEST_CLOTEHS_SIZE_2, TEST_CLOTEHS_SIZE_3));
        TEST_CLOTHES2.changeClothesSizes(List.of(TEST_CLOTHES_SIZE_4, TEST_CLOTHES_SIZE_5, TEST_CLOTHES_SIZE_6));

        Order TEST_ORDER = Order.builder()
                .orderer(TEST_MEMBER_1)
                .delivery(Delivery.builder()
                        .deliveryMemo("testMemo1")
                        .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                        .address(new Address("", "", "65000", "", ""))
                        .recipient("testRecipient")
                        .recipientPhone("000-0000-0000")
                        .build())
                .ordererInfo(
                        OrdererInfo.builder()
                                .ordererName("testOrdererName1")
                                .ordererEmail("testOrdererEmail1")
                                .ordererPhone("000-0000-0000")
                                .build()
                )
                .build();

        TEST_ORDER.changeOrderItems(List.of(
                new OrderClothes(10l, TEST_CLOTHES1, TEST_CLOTEHS_SIZE_1),
                new OrderClothes(20l, TEST_CLOTHES1, TEST_CLOTEHS_SIZE_2)));

        Order TEST_ORDER_2 = Order.builder()
                .orderer(TEST_MEMBER_2)
                .delivery(Delivery.builder()
                        .deliveryMemo("testMemo2")
                        .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                        .address(new Address("", "", "67000", "", ""))
                        .recipient("testRecipient")
                        .recipientPhone("000-0000-0000")
                        .build())
                .ordererInfo(
                        OrdererInfo.builder()
                                .ordererName("testOrdererName2")
                                .ordererEmail("testOrdererEmail2")
                                .ordererPhone("000-0000-0000")
                                .build()
                )
                .build();

        TEST_ORDER_2.changeOrderItems(List.of(
                new OrderClothes(100l, TEST_CLOTHES2, TEST_CLOTHES_SIZE_4)
        ));

        Order TEST_ORDER_3 = Order.builder()
                .orderer(TEST_MEMBER_1)
                .delivery(Delivery.builder()
                        .deliveryMemo("testMemo1")
                        .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                        .address(new Address("", "", "65000", "", ""))
                        .recipient("testRecipient")
                        .recipientPhone("000-0000-0000")
                        .build())
                .ordererInfo(
                        OrdererInfo.builder()
                                .ordererName("testOrdererName1")
                                .ordererEmail("testOrdererEmail1")
                                .ordererPhone("000-0000-0000")
                                .build()
                )
                .build();

        TEST_ORDER_3.changeOrderItems(List.of(
                new OrderClothes(100l, TEST_CLOTHES2, TEST_CLOTHES_SIZE_5)
        ));

        testEntityManager.persist(TEST_MEMBER_1);
        testEntityManager.persist(TEST_MEMBER_2);
        testEntityManager.persist(TEST_CLOTHES1);
        testEntityManager.persist(TEST_CLOTHES2);
        testEntityManager.persist(TEST_ORDER);
        testEntityManager.persist(TEST_ORDER_2);
        testEntityManager.persist(TEST_ORDER_3);

        //when
        // 올바른 authId(주문과 관련된 아이디)로 조회.
        List<Order> findCorrectOrders = orderRepository.getOrderIdsByAuthId(TEST_ORDER.getOrderer().getAuthId(), OrderStatus.ORDER_TEMP);

        // 올바르지 않은 authId(주문과 관련되지 않은 아이디)로 조회.
        List<Order> findInCorrectOrders = orderRepository.getOrderIdsByAuthId("incorrectAuthId", OrderStatus.ORDER_TEMP);


        //then
        // 올바른 authId(주문과 관련된 아이디)로 조회 결과.
        assertThat(findCorrectOrders.size(), is(2));
        assertThat(findCorrectOrders.get(0).getOrderId(), is(TEST_ORDER.getOrderId()));
        assertThat(findCorrectOrders.get(1).getOrderId(), is(TEST_ORDER_3.getOrderId()));

        // 올바르지 않은 authId(주문과 관련되지 않은 아이디)로 조회 결과.
        assertNotNull(findInCorrectOrders);
        assertTrue(findInCorrectOrders.isEmpty());
        assertThat(findInCorrectOrders.size(), is(0));
    }

    @Test
    public void 주문목록_삭제_테스트() throws Exception {

    }

    @Test
    public void 주문목록_삭제_테스트2() throws Exception {
        // given
        Member TEST_MEMBER_1 = Member.builder()
                .name("testName")
                .authId("testId")
                .pwd("testPwd")
                .age(10)
                .gender(Gender.MAN)
                .email("testEmail@email")
                .phone("000-0000-0000")
                .dateOfBirth("1993-09-09")
                .role(MemberRole.ACTIVATED_USER)
                .address(new Address("", "", "", "", ""))
                .build();

        Member TEST_MEMBER_2 = Member.builder()
                .name("testName2")
                .authId("testId2")
                .pwd("testPwd2")
                .age(12)
                .gender(Gender.MAN)
                .email("testEmail@email2")
                .phone("000-0000-0000")
                .dateOfBirth("1993-09-09")
                .role(MemberRole.ACTIVATED_USER)
                .address(new Address("", "", "", "", ""))
                .build();

        Clothes TEST_CLOTHES1 = createClothes(1000l, "testClothesName", "testClothesEngName", Color.RED);

        Clothes TEST_CLOTHES2 = createClothes(100l, "testClothesName2", "testClothesEngName2", Color.BLUE);

        ClothesSize TEST_CLOTEHS_SIZE_1 = createClothesSize(SizeLabel.S, 10l);
        ClothesSize TEST_CLOTEHS_SIZE_2 = createClothesSize(SizeLabel.M, 20l);
        ClothesSize TEST_CLOTEHS_SIZE_3 = createClothesSize(SizeLabel.L, 30l);

        ClothesSize TEST_CLOTHES_SIZE_4 = createClothesSize(SizeLabel.S, 100l);
        ClothesSize TEST_CLOTHES_SIZE_5 = createClothesSize(SizeLabel.M, 200l);
        ClothesSize TEST_CLOTHES_SIZE_6 = createClothesSize(SizeLabel.L, 300l);

        TEST_CLOTHES1.changeClothesSizes(List.of(TEST_CLOTEHS_SIZE_1, TEST_CLOTEHS_SIZE_2, TEST_CLOTEHS_SIZE_3));
        TEST_CLOTHES2.changeClothesSizes(List.of(TEST_CLOTHES_SIZE_4, TEST_CLOTHES_SIZE_5, TEST_CLOTHES_SIZE_6));

        Order TEST_ORDER = Order.builder()
                .orderer(TEST_MEMBER_1)
                .delivery(Delivery.builder()
                        .deliveryMemo("testMemo1")
                        .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                        .address(new Address("", "", "65000", "", ""))
                        .recipient("testRecipient")
                        .recipientPhone("000-0000-0000")
                        .build())
                .ordererInfo(
                        OrdererInfo.builder()
                                .ordererName("testOrdererName1")
                                .ordererEmail("testOrdererEmail1")
                                .ordererPhone("000-0000-0000")
                                .build()
                )
                .build();

        TEST_ORDER.changeOrderItems(List.of(
                new OrderClothes(10l, TEST_CLOTHES1, TEST_CLOTEHS_SIZE_1),
                new OrderClothes(20l, TEST_CLOTHES1, TEST_CLOTEHS_SIZE_2)));

        Order TEST_ORDER_2 = Order.builder()
                .orderer(TEST_MEMBER_2)
                .delivery(Delivery.builder()
                        .deliveryMemo("testMemo2")
                        .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                        .address(new Address("", "", "65000", "", ""))
                        .recipient("testRecipient")
                        .recipientPhone("000-0000-0000")
                        .build())
                .ordererInfo(
                        OrdererInfo.builder()
                                .ordererName("testOrdererName2")
                                .ordererEmail("testOrdererEmail2")
                                .ordererPhone("000-0000-0000")
                                .build()
                )
                .build();

        TEST_ORDER_2.changeOrderItems(List.of(
                new OrderClothes(100l, TEST_CLOTHES2, TEST_CLOTHES_SIZE_4)
        ));

        Order TEST_ORDER_3 = Order.builder()
                .orderer(TEST_MEMBER_1)
                .delivery(Delivery.builder()
                        .deliveryMemo("testMemo1")
                        .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                        .address(new Address("", "", "65000", "", ""))
                        .recipient("testRecipient")
                        .recipientPhone("000-0000-0000")
                        .build())
                .ordererInfo(
                        OrdererInfo.builder()
                                .ordererName("testOrdererName1")
                                .ordererEmail("testOrdererEmail1")
                                .ordererPhone("000-0000-0000")
                                .build()
                )
                .build();

        TEST_ORDER_3.changeOrderItems(List.of(
                new OrderClothes(100l, TEST_CLOTHES2, TEST_CLOTHES_SIZE_5)
        ));

        testEntityManager.persist(TEST_MEMBER_1);
        testEntityManager.persist(TEST_MEMBER_2);
        testEntityManager.persist(TEST_CLOTHES1);
        testEntityManager.persist(TEST_CLOTHES2);
        testEntityManager.persist(TEST_ORDER);
        testEntityManager.persist(TEST_ORDER_2);
        testEntityManager.persist(TEST_ORDER_3);
        testEntityManager.flush();
        testEntityManager.clear();

        System.out.println("TEST_ORDER id: " + TEST_ORDER.getOrderId());
        System.out.println("TEST_ORDER_ORDER_ITEM_ORDER id: " + TEST_ORDER.getOrderItems().get(0).getOrder().getOrderId());
        System.out.println("TEST_ORDER_2 id: " + TEST_ORDER_2.getOrderId());
        System.out.println("TEST_ORDER_2_ORDER_ITEM_ORDER id: " + TEST_ORDER_2.getOrderItems().get(0).getOrder().getOrderId());

        // 삭제 전, 임시저장 된 주문 목록.
        List<Order> beforeOrders = orderRepository.getOrderIdsByAuthId(TEST_ORDER.getOrderer().getAuthId(), OrderStatus.ORDER_TEMP);

        //when
        orderRepository.deleteAll(beforeOrders);

        // 삭제 후, 임시저장 된 주문 목록.
        List<Order> afterOrders = orderRepository.getOrderIdsByAuthId(TEST_ORDER.getOrderer().getAuthId(), OrderStatus.ORDER_TEMP);

        //then
        assertThat(beforeOrders.size(), is(2));
        assertThat(afterOrders.size(), is(0));
    }

    private Clothes createClothes(long price, String testClothesName, String testClothesEngName, Color red) {
        return Clothes.builder()
                .price(price)
                .name(testClothesName)
                .engName(testClothesEngName)
                .color(red)
                .build();
    }

    private ClothesSize createClothesSize(SizeLabel sizeLabel, Long stockQuantity) {
        return ClothesSize.builder()
                .sizeLabel(sizeLabel)
                .stockQuantity(stockQuantity)
                .build();
    }
}
