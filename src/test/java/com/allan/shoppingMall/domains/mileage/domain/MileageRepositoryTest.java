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

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@DataJpaTest(
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JpaAuditingConfig.class
        )
)
@WithMockUser
public class MileageRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;


    @Autowired
    MileageRepository mileageRepository;

    @Test
    public void 로그인아이디로_마일리지_리스트_조회테스트() throws Exception {
        //given
        Mileage TEST_MILEAGE_1 = Mileage.builder()
                .authId("test_authId_1")
                .orderNum("testOrderNum1")
                .point(1000l)
                .mileageContent(MileageContent.PAYMENT_MILEAGE_ACCUMULATE)
                .build();

        Mileage TEST_MILEAGE_2 = Mileage.builder()
                .authId("test_authId_1")
                .orderNum("testOrderNum2")
                .point(500l)
                .mileageContent(MileageContent.REVIEW_MILEAGE_ACCUMULATE)
                .build();

        Mileage TEST_MILEAGE_3 = Mileage.builder()
                .authId("test_authId_2")
                .orderNum("testOrderNum3")
                .point(300l)
                .mileageContent(MileageContent.PAYMENT_MILEAGE_ACCUMULATE)
                .build();

        testEntityManager.persist(TEST_MILEAGE_1);
        testEntityManager.persist(TEST_MILEAGE_2);
        testEntityManager.persist(TEST_MILEAGE_3);

        //when
        List<Mileage> mileageList = mileageRepository.findAllByAuthId("test_authId_1");

        long pointAmount = mileageList.stream()
                .mapToLong(mileage -> {
                    return mileage.getPoint();
                })
                .sum();

        //then
        assertThat(mileageList.size(), is(2));
        assertThat(pointAmount, is(TEST_MILEAGE_1.getPoint() + TEST_MILEAGE_2.getPoint()));
    }

    @Test
    public void 주문번호로_마일리지_리스트_조회테스트() throws Exception {
        //given
        Mileage TEST_MILEAGE_1 = Mileage.builder()
                .authId("test_authId_1")
                .orderNum("testOrderNum1")
                .point(1000l)
                .mileageContent(MileageContent.PAYMENT_MILEAGE_ACCUMULATE)
                .build();

        Mileage TEST_MILEAGE_2 = Mileage.builder()
                .authId("test_authId_1")
                .orderNum("testOrderNum1")
                .point(-500l)
                .mileageContent(MileageContent.USED_MILEAGE_DEDUCTION)
                .build();

        Mileage TEST_MILEAGE_3 = Mileage.builder()
                .authId("test_authId_2")
                .orderNum("testOrderNum3")
                .point(300l)
                .mileageContent(MileageContent.PAYMENT_MILEAGE_ACCUMULATE)
                .build();

        testEntityManager.persist(TEST_MILEAGE_1);
        testEntityManager.persist(TEST_MILEAGE_2);
        testEntityManager.persist(TEST_MILEAGE_3);

        //when
        List<Mileage> mileageList = mileageRepository.findAllByOrderNum("testOrderNum1");

        long pointAmount = mileageList.stream()
                .mapToLong(mileage -> {
                    return mileage.getPoint();
                })
                .sum();

        //then
        assertThat(mileageList.size(), is(2));
        assertThat(pointAmount, is(TEST_MILEAGE_1.getPoint() + TEST_MILEAGE_2.getPoint()));
    }

    @Test
    public void 마일리지아이디_목록으로_삭제_테스트() throws Exception {
        //given
        Mileage TEST_MILEAGE_1 = Mileage.builder()
                .authId("test_authId_1")
                .orderNum("testOrderNum1")
                .point(1000l)
                .mileageContent(MileageContent.PAYMENT_MILEAGE_ACCUMULATE)
                .build();

        Mileage TEST_MILEAGE_2 = Mileage.builder()
                .authId("test_authId_1")
                .orderNum("testOrderNum1")
                .point(-500l)
                .mileageContent(MileageContent.USED_MILEAGE_DEDUCTION)
                .build();

        Mileage TEST_MILEAGE_3 = Mileage.builder()
                .authId("test_authId_2")
                .orderNum("testOrderNum3")
                .point(300l)
                .mileageContent(MileageContent.PAYMENT_MILEAGE_ACCUMULATE)
                .build();

        //when
        testEntityManager.persist(TEST_MILEAGE_1);
        testEntityManager.persist(TEST_MILEAGE_2);
        testEntityManager.persist(TEST_MILEAGE_3);

        List<Mileage> mileageList = mileageRepository.findAll();
        assertThat(mileageList.size(), is(3));

        mileageRepository.deleteAllByMileageIds(List.of(TEST_MILEAGE_1.getMileageId(), TEST_MILEAGE_2.getMileageId(), TEST_MILEAGE_3.getMileageId()));

        //then
        List<Mileage> mileages = mileageRepository.findAll();
        assertThat(mileages.size(), is(0));
    }

    @Test
    public void 특정주문에_사용한_마일리지_조회테스트() throws Exception {
        //given
        Mileage TEST_USED_MILEAGE = Mileage.builder()
                .orderNum("testOrderNum")
                .authId("testAuthId")
                .mileageContent(MileageContent.USED_MILEAGE_DEDUCTION)
                .point(1000l)
                .build();

        Mileage TEST_PAYMENT_MILEAGE = Mileage.builder()
                .orderNum("testOrderNum")
                .authId("testAuthId")
                .mileageContent(MileageContent.PAYMENT_MILEAGE_ACCUMULATE)
                .point(2000l)
                .build();

        testEntityManager.persist(TEST_USED_MILEAGE);
        testEntityManager.persist(TEST_PAYMENT_MILEAGE);

        //when
        Mileage mileage = mileageRepository.findByOrderNumAndMileageContent("testOrderNum", MileageContent.USED_MILEAGE_DEDUCTION).get();

        //then
        assertThat(mileage.getMileageContent().getDesc(), is(TEST_USED_MILEAGE.getMileageContent().getDesc()));
    }
}
