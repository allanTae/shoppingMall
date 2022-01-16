package com.allan.shoppingMall.domains.mileage.domain;

import com.allan.shoppingMall.common.config.jpa.auditing.JpaAuditingConfig;
import com.allan.shoppingMall.domains.mileage.domain.model.MileageContent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
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
public class MileageTest {
    @Autowired
    TestEntityManager testEntityManager;

    @Test
    public void 회원가입_마일리지_주문번호_입력테스트() throws Exception {
        //given
        Mileage TEST_MILEAGE = Mileage.builder()
                .authId("testAuthId")
                .point(1000l)
                .mileageContent(MileageContent.JOIN_MILEAGE_ACCUMULATE)
                .build();

        //when
        testEntityManager.persist(TEST_MILEAGE);

        //then
        assertThat(TEST_MILEAGE.getOrderNum(), is("joinMember"));
    }
}
