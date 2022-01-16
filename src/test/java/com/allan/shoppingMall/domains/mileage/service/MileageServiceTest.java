package com.allan.shoppingMall.domains.mileage.service;

import com.allan.shoppingMall.common.exception.mileage.MileageDeductFailException;
import com.allan.shoppingMall.domains.mileage.domain.Mileage;
import com.allan.shoppingMall.domains.mileage.domain.MileageRepository;
import com.allan.shoppingMall.domains.mileage.domain.model.MileageContent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
@Rollback(value = true)
public class MileageServiceTest {

    @Mock
    MileageRepository mileageRepository;

    @InjectMocks
    MileageService mileageService;

    @Test
    public void 마일리지_적립_테스트() throws Exception {
        //given, //when
        mileageService.accumulateMileage("testOrderNum", "testAuthId", 100l, MileageContent.PAYMENT_MILEAGE_ACCUMULATE);

        //then
        verify(mileageRepository, atLeastOnce()).save(any());
    }

    @Test
    public void 마일리지_차감_테스트() throws Exception {
        //given
        List<Mileage> TEST_MILEAGE_LIST = List.of(
                Mileage.builder()
                        .authId("testAuthId")
                        .orderNum("testOrderNum1")
                        .mileageContent(MileageContent.PAYMENT_MILEAGE_ACCUMULATE)
                        .point(1000l)
                        .build(),
                Mileage.builder()
                        .authId("testAuthId")
                        .orderNum("testOrderNum2")
                        .mileageContent(MileageContent.PAYMENT_MILEAGE_ACCUMULATE)
                        .point(2000l)
                        .build()
        );
        given(mileageRepository.findAllByAuthId(any()))
                .willReturn(TEST_MILEAGE_LIST);

        //when
        mileageService.deductMileage("testOrderNum1", "testAuthId", 1000l, MileageContent.USED_MILEAGE_DEDUCTION);

        //then
        verify(mileageRepository, atLeastOnce()).save(any());
    }

    @Test
    public void 포인트부족으로_인한_마일리지_차감_실패테스트() throws Exception {
        //given
        List<Mileage> TEST_MILEAGE_LIST = List.of(
                Mileage.builder()
                        .authId("testAuthId")
                        .orderNum("testOrderNum1")
                        .mileageContent(MileageContent.PAYMENT_MILEAGE_ACCUMULATE)
                        .point(1000l)
                        .build(),
                Mileage.builder()
                        .authId("testAuthId")
                        .orderNum("testOrderNum2")
                        .mileageContent(MileageContent.PAYMENT_MILEAGE_ACCUMULATE)
                        .point(2000l)
                        .build()
        );
        given(mileageRepository.findAllByAuthId(any()))
                .willReturn(TEST_MILEAGE_LIST);

        //when, then
        assertThrows(MileageDeductFailException.class, () -> {
            mileageService.deductMileage("testOrderNum1", "testAuthId", 4000l, MileageContent.USED_MILEAGE_DEDUCTION);
        });
    }

    @Test
    public void 마일리지_조회_실패로_인한_마일리지_차감_실패테스트() throws Exception {
        //given
        List<Mileage> TEST_MILEAGE_LIST = List.of(
        );
        given(mileageRepository.findAllByAuthId(any()))
                .willReturn(TEST_MILEAGE_LIST);

        //when, then
        assertThrows(MileageDeductFailException.class, () -> {
            mileageService.deductMileage("testOrderNum1", "testAuthId", 4000l, MileageContent.USED_MILEAGE_DEDUCTION);
        });
    }

    @Test
    public void 마일리지_삭제_테스트() throws Exception {
        //given
        List<Mileage> TEST_MILEAGE_LIST = List.of(
                Mileage.builder()
                        .authId("testAuthId")
                        .orderNum("testOrderNum1")
                        .mileageContent(MileageContent.PAYMENT_MILEAGE_ACCUMULATE)
                        .point(1000l)
                        .build(),
                Mileage.builder()
                        .authId("testAuthId")
                        .orderNum("testOrderNum1")
                        .mileageContent(MileageContent.USED_MILEAGE_DEDUCTION)
                        .point(-500l)
                        .build()
        );
        given(mileageRepository.findAllByOrderNum(any()))
                .willReturn(TEST_MILEAGE_LIST);

        //when
        mileageService.deleteMileage(any());

        //then
        verify(mileageRepository, atLeastOnce()).deleteAllByMileageIds(any());
    }


    @Test
    public void 주문번호로_마일리지_조회_테스트() throws Exception {
        //given
        Mileage TEST_MILEAGE_1 = Mileage.builder()
                .orderNum("testOrderNum1")
                .mileageContent(MileageContent.USED_MILEAGE_DEDUCTION)
                .point(1000l)
                .build();

        given(mileageRepository.findByOrderNumAndMileageContent(TEST_MILEAGE_1.getOrderNum(), TEST_MILEAGE_1.getMileageContent()))
                .willReturn(Optional.of(TEST_MILEAGE_1));

        //when
        mileageService.getMileageByOrderNum(TEST_MILEAGE_1.getOrderNum(), TEST_MILEAGE_1.getMileageContent());

        // then
        verify(mileageRepository, atLeastOnce()).findByOrderNumAndMileageContent(TEST_MILEAGE_1.getOrderNum(), TEST_MILEAGE_1.getMileageContent());
    }

    @Test
    public void 사용가능한_총마일리지_조회테스트() throws Exception {
        //given
        Mileage TEST_MILEAGE_1 = Mileage.builder()
                .orderNum("testOrderNum1")
                .authId("testAuthId")
                .mileageContent(MileageContent.JOIN_MILEAGE_ACCUMULATE)
                .point(1000l)
                .build();

        Mileage TEST_MILEAGE_2 = Mileage.builder()
                .orderNum("testOrderNum2")
                .authId("testAuthId")
                .mileageContent(MileageContent.JOIN_MILEAGE_ACCUMULATE)
                .point(2500l)
                .build();

        Mileage TEST_MILEAGE_3 = Mileage.builder()
                .orderNum("testOrderNum2")
                .authId("testAuthId")
                .mileageContent(MileageContent.PAYMENT_MILEAGE_ACCUMULATE)
                .point(-300l)
                .build();

        given(mileageRepository.findAllByAuthId("testAuthId"))
                .willReturn(List.of(TEST_MILEAGE_1, TEST_MILEAGE_2, TEST_MILEAGE_3));

        //when
        long mileagePoint = mileageService.getAvailableMileagePoint("testAuthId");

        //then
        verify(mileageRepository, atLeastOnce()).findAllByAuthId("testAuthId");
        assertThat(mileagePoint, is(TEST_MILEAGE_1.getPoint() + TEST_MILEAGE_2.getPoint() + TEST_MILEAGE_3.getPoint()));
    }
}
