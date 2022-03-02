package com.allan.shoppingMall.common.aop;

import com.allan.shoppingMall.common.config.aop.OrderCompletedPublishAop;
import com.allan.shoppingMall.common.value.Address;
import com.allan.shoppingMall.domains.delivery.domain.Delivery;
import com.allan.shoppingMall.domains.item.domain.clothes.*;
import com.allan.shoppingMall.domains.item.domain.item.ItemSize;
import com.allan.shoppingMall.domains.item.domain.item.ItemSizeRepository;
import com.allan.shoppingMall.domains.mileage.domain.model.MileageDTO;
import com.allan.shoppingMall.domains.mileage.service.MileageService;
import com.allan.shoppingMall.domains.order.domain.*;
import com.allan.shoppingMall.domains.order.service.OrderService;
import com.allan.shoppingMall.domains.payment.domain.PaymentRepository;
import com.allan.shoppingMall.domains.payment.domain.model.iamport.PaymentIamportDTO;
import com.allan.shoppingMall.domains.payment.service.PaymentService;
import org.junit.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * 주문완료시, 주문완료 이벤트 publish aop 테스트 클래스 입니다.
 */
@SpringBootTest
public class OrderCompletedPublishAopTest {

    @Test
    public void 주문완료_이벤트_생성프록시_테스트() throws Exception {
        //given

        OrderRepository TEST_ORDER_REPOSITORY = mock(OrderRepository.class);
        MileageService TEST_MILEAGE_SERVICE = mock(MileageService.class);
        PaymentRepository TEST_PAYMENT_REPOSITORY = mock(PaymentRepository.class);
        ApplicationContext TEST_APPLICATION_CONTEXT = mock(ApplicationContext.class);
        ClothesRepository TEST_CLOTHES_REPOSITORY = mock(ClothesRepository.class);
        ItemSizeRepository TEST_CLOTHES_SIZE_REPOSITORY = mock(ItemSizeRepository.class);
        PaymentService TEST_PAYMENT_SERVICE = mock(PaymentService.class);

        Clothes TEST_CLOTHES = Clothes.builder()
                .price(1000l)
                .build();

        ClothesSize TEST_CLOTHES_SIZE = ClothesSize.builder()
                .stockQuantity(100l)
                .sizeLabel(SizeLabel.S)
                .build();

        TEST_CLOTHES.changeItemSizes(List.of(TEST_CLOTHES_SIZE));

        Order TEST_ORDER = Order.builder()
                .delivery(Delivery.builder()
                        .address(Address.builder()
                                .postCode("53000")
                                .build())
                        .build())
                .build();
        ReflectionTestUtils.setField(TEST_ORDER, "orderId", 1l);
        ReflectionTestUtils.setField(TEST_ORDER, "orderStatus", OrderStatus.ORDER_TEMP);
        TEST_ORDER.changeOrderItems(List.of(
                new OrderItem(10l, TEST_CLOTHES, TEST_CLOTHES_SIZE)));

        given(TEST_ORDER_REPOSITORY.findByOrderNumAndAuthId("testAuthId", "testOrderNum"))
            .willReturn(Optional.of(TEST_ORDER));

        MileageDTO TEST_MILEAGE_DTO = new MileageDTO(100l, "testMileage");
        given(TEST_MILEAGE_SERVICE.getMileageByOrderNum(any(), any()))
                .willReturn(TEST_MILEAGE_DTO);

        PaymentIamportDTO TEST_PAYMENT_IAMPORT_DTO = PaymentIamportDTO.builder()
                .paymentAmount(13100l)
                .impUid("testImpUid")
                .merchantUid("testOrderNum")
                .payStatus("testPayStatus")
                .payMethod("testPayMethod")
                .name("testPayName")
                .build();

        AspectJProxyFactory factory = new AspectJProxyFactory(new OrderService(TEST_ORDER_REPOSITORY, TEST_CLOTHES_REPOSITORY,
                TEST_CLOTHES_SIZE_REPOSITORY, TEST_PAYMENT_REPOSITORY, TEST_PAYMENT_SERVICE, TEST_MILEAGE_SERVICE));
        factory.addAspect(new OrderCompletedPublishAop(TEST_APPLICATION_CONTEXT));
        OrderService proxy = factory.getProxy();

        //when, then
        proxy.validatePaymentByIamport(TEST_PAYMENT_IAMPORT_DTO, "testAuthId");

        // then
        verify(TEST_APPLICATION_CONTEXT, atLeastOnce()).publishEvent(any(OrderCompletedEvent.class));
    }
}
