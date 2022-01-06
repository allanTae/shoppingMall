package com.allan.shoppingMall.domains.payment.domain;

import com.allan.shoppingMall.common.config.jpa.auditing.JpaAuditingConfig;
import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.order.OrderNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@DataJpaTest(
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JpaAuditingConfig.class
        )
)
@WithMockUser
public class PaymentRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    PaymentRepository paymentRepository;

    @Test
    public void 결제번호로_결제_조회_테스트() throws Exception {
        //given
        Payment TEST_PAYMENT = Payment.builder()
                .orderName("testOrderName")
                .orderNum("test_merchantUid")
                .paymentNum("test_impUid")
                .payAmount(10000l)
                .payStatus("testPayStatus")
                .payMethod("testPayMethod")
                .build();

        testEntityManager.persist(TEST_PAYMENT);
        testEntityManager.flush();
        testEntityManager.clear();

        //when
        Payment findPayment = paymentRepository.findByPaymentNum(TEST_PAYMENT.getPaymentNum()).orElseThrow(()
                -> new OrderNotFoundException(ErrorCode.ENTITY_NOT_FOUND.getMessage(), ErrorCode.ENTITY_NOT_FOUND));

        //then
        assertThat(findPayment.getOrderName(), is(TEST_PAYMENT.getOrderName()));
    }
}
