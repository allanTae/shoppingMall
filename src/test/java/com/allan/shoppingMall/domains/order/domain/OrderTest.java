package com.allan.shoppingMall.domains.order.domain;

import com.allan.shoppingMall.common.config.jpa.auditing.JpaAuditingConfig;
import com.allan.shoppingMall.common.value.Address;
import com.allan.shoppingMall.domains.delivery.domain.Delivery;
import com.allan.shoppingMall.domains.delivery.domain.DeliveryStatus;
import com.allan.shoppingMall.domains.item.domain.Item;
import com.allan.shoppingMall.domains.item.domain.clothes.Clothes;
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

@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = JpaAuditingConfig.class
))
@WithMockUser
public class OrderTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    public void 주문_취소_테스트() throws Exception {
        //given
        Order TEST_ORDER = createOrder();
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
        Item TEST_ITEM = createClothes();
        testEntityManager.persist(TEST_ITEM);
        testEntityManager.persist(TEST_ORDERER);

        Order TEST_ORDER = createOrder(TEST_ORDERER);
        TEST_ORDER.changeOrderItems(createdOrderItems(TEST_ITEM));
        testEntityManager.persist(TEST_ORDER);

        //when
        TEST_ORDER.changeOrderItems(createdOrderItems(TEST_ITEM));

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

    private List<OrderItem> createdOrderItems(Item item
    ){
        return List.of(
                new OrderItem(10l, item)
        );
    }


    private Clothes createClothes(){
        Clothes clothes = Clothes.builder()
                .name("testClothesName")
                .price(100l)
                .stockQuantity(20l)
                .engName("testEngname")
                .build();

        return clothes;
    }

    private Order createOrder() {
        return Order.builder()
                .orderStatus(OrderStatus.ORDER_COMPLETE)
                .orderer(createMember())
                .delivery(Delivery.builder()
                        .address(new Address("", "", "", "", ""))
                        .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                        .build())
                .build();
    }

    private Order createOrder(Member orderer) {
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
