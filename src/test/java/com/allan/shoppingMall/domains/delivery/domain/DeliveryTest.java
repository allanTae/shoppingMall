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
                .address(new Address("",  "",  "", "", ""))
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
                .address(new Address("",  "",  "", "", ""))
                .build();

        //when, then
        assertThrows(OrderCancelFailException.class, () -> {
            TEST_DELIVERY.cancelDelivery();
        });
    }
}
