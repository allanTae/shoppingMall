package com.allan.shoppingMall.domains.order.domain;

import com.allan.shoppingMall.common.config.jpa.auditing.JpaAuditingConfig;
import com.allan.shoppingMall.common.value.Address;
import com.allan.shoppingMall.domains.delivery.domain.Delivery;
import com.allan.shoppingMall.domains.delivery.domain.DeliveryStatus;
import com.allan.shoppingMall.domains.item.domain.Color;
import com.allan.shoppingMall.domains.item.domain.Item;
import com.allan.shoppingMall.domains.item.domain.clothes.Clothes;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesSize;
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

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = JpaAuditingConfig.class
))
@WithMockUser
public class OrderTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    public void 고객이_주문_취소_테스트() throws Exception {
        //given
        Order TEST_ORDER = createOrderByMember();
        testEntityManager.persist(TEST_ORDER);

        //when
        TEST_ORDER.cancelOrder();

        //then
        assertThat(TEST_ORDER.getOrderStatus(), is(OrderStatus.ORDER_CANCEL));

    }

    /**
     * 주문시, 동일한 상품이 리스트에 추가여부를 확인하는 테스트.
     * 오류로 인하여 동일한 상품이 추가되어 이중 결제가 되는 것을 방지하기 위함.
     */
    @Test
    public void 주문시_중복상품_추가_테스트() throws Exception {
        //given
        Member TEST_ORDERER = createMember();
        ClothesSize TEST_CLOTHES_SIZE = createClothesSize();
        Clothes TEST_CLOTHES = createClothes(TEST_CLOTHES_SIZE);
        testEntityManager.persist(TEST_CLOTHES);
        testEntityManager.persist(TEST_ORDERER);

        Order TEST_ORDER = createOrderByMember(TEST_ORDERER);
        TEST_ORDER.changeOrderItems(createdOrderItems(TEST_CLOTHES));
        testEntityManager.persist(TEST_ORDER);

        //when
        TEST_ORDER.changeOrderItems(createdOrderItems(TEST_CLOTHES));

        //then
        assertThat(TEST_ORDER.getOrderItems().size(), is(1));
    }


    @Test
    public void 주문시_옷_상품_중복_추가_테스트() throws Exception {
        //given
        Member TEST_ORDERER = createMember();
        ClothesSize TEST_CLOTHES_SIZE = createClothesSize();
        Clothes TEST_CLOTHES = createClothes(TEST_CLOTHES_SIZE);

        testEntityManager.persist(TEST_CLOTHES);
        testEntityManager.persist(TEST_ORDERER);

        Order TEST_ORDER = createOrderByMember(TEST_ORDERER);
        TEST_ORDER.changeOrderItems(createdOrderClothes(TEST_CLOTHES, TEST_CLOTHES_SIZE));
        testEntityManager.persist(TEST_ORDER);

        //when
        TEST_ORDER.changeOrderItems(createdOrderClothes(TEST_CLOTHES, TEST_CLOTHES_SIZE));

        //then
        assertThat(TEST_ORDER.getOrderItems().size(), is(1));
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

    private List<OrderItem> createdOrderClothes(Clothes clothes, ClothesSize clothesSize){
        return List.of(
                new OrderClothes(10l, clothes, clothesSize)
        );
    }

    private List<OrderItem> createdOrderItems(Item item
    ){
        return List.of(
                // 주문량 10개 아이템.
                new OrderItem(10l, item)
        );
    }


    private Clothes createClothes(ClothesSize clothesSize){
        Clothes clothes = Clothes.builder()
                .name("testClothesName")
                .price(100l)
                .engName("testEngname")
                .color(Color.RED)
                .build();



        clothes.changeClothesSizes(List.of(clothesSize));

        return clothes;
    }

    private ClothesSize createClothesSize(){
        return ClothesSize.builder()
                .stockQuantity(30l)
                .build();
    }

    private Order createOrderByMember() {
        return Order.builder()
                .orderStatus(OrderStatus.ORDER_READY)
                .orderer(createMember())
                .delivery(Delivery.builder()
                        .address(new Address("", "", "", "", ""))
                        .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                        .build())
                .build();
    }

    private Order createOrderByMember(Member orderer) {
        return Order.builder()
                .orderStatus(OrderStatus.ORDER_COMPLETE)
                .orderer(orderer)
                .delivery(Delivery.builder()
                        .address(new Address("", "", "", "", ""))
                        .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                        .build())
                .build();
    }
}
