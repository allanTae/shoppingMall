package com.allan.shoppingMall.domains.delivery.domain;

import com.allan.shoppingMall.common.config.jpa.auditing.JpaAuditingConfig;
import com.allan.shoppingMall.common.exception.order.OrderCancelFailException;
import com.allan.shoppingMall.common.value.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = JpaAuditingConfig.class
))
@WithMockUser
public class DeliveryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    public void 배송_취소_테스트() throws Exception {
        //given
        Delivery TEST_DELIVERY = Delivery.builder()
                .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                .address(new Address("",  "",  "65040", "", ""))
                .build();

        //when
        TEST_DELIVERY.cancelDelivery();

        //then
        assertThat(TEST_DELIVERY.getDeliveryStatus(), is(DeliveryStatus.DELIVERY_CANCEL));
    }

    @Test
    public void 배송_취소_실패_테스트() throws Exception {
        //given
        Delivery TEST_DELIVERY = Delivery.builder()
                .deliveryStatus(DeliveryStatus.SHIPPING)
                .address(new Address("",  "",  "65040", "", ""))
                .build();

        //when, then
        assertThrows(OrderCancelFailException.class, () -> {
            TEST_DELIVERY.cancelDelivery();
        });
    }
    /**
     * 제주 지역 배송비가 4000원으로 설정되는지 테스트 하는 메소드.
     */
    @Test
    public void 제주지역_배송비_테스트() throws Exception {
        //given
        Delivery TEST_DELIVERY = Delivery.builder()
                .deliveryMemo("testDeliveryMemo")
                .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                .address(new Address("", "", "63450", "", ""))
                .build();

        //then
        assertThat(TEST_DELIVERY.getDeliveryAmount(), is(4000l));
    }

    /**
     * 제주 이외 지역 배송비가 3000원으로 설정되는지 테스트 하는 메소드.
     */
    @Test
    public void 제주이외지역_배송비_테스트() throws Exception {
        //given
        Delivery TEST_DELIVERY = Delivery.builder()
                .deliveryMemo("testDeliveryMemo")
                .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                .address(new Address("", "", "45450", "", ""))
                .build();

        //then
        assertThat(TEST_DELIVERY.getDeliveryAmount(), is(3000l));
    }
}
