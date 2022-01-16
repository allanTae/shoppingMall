package com.allan.shoppingMall.domains.order.presentation;

import com.allan.shoppingMall.common.exception.BusinessException;
import com.allan.shoppingMall.common.exception.order.payment.PaymentFailException;
import com.allan.shoppingMall.common.exception.order.RefundFailException;
import com.allan.shoppingMall.domains.infra.AuthenticationConverter;
import com.allan.shoppingMall.domains.member.domain.Member;
import com.allan.shoppingMall.domains.order.domain.model.OrderErrorResponse;
import com.allan.shoppingMall.domains.order.domain.model.OrderRequest;
import com.allan.shoppingMall.domains.order.domain.model.OrderResponse;
import com.allan.shoppingMall.domains.order.service.OrderService;
import com.allan.shoppingMall.domains.payment.domain.model.iamport.PaymentIamportDTO;
import com.allan.shoppingMall.domains.payment.domain.model.PaymentRequest;
import com.allan.shoppingMall.domains.payment.service.PaymentService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;


@RestController
@Slf4j
public class RestOrderController {

    private AuthenticationConverter authenticationConverter;
    private OrderService orderService;
    private PaymentService paymentService;
    private IamportClient api;

    @Autowired(required = false)
    public RestOrderController(AuthenticationConverter authenticationConverter, OrderService orderService, PaymentService paymentService){
        this.authenticationConverter = authenticationConverter;
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.api = new IamportClient("3101823184907840", "7269cbb2f71f7941f4d532c0097c06138e3056ad04699aa2b67de132a78727acd7852454b7f9d242");
    }

    /**
     * test용. 생성자.
     */
    @Autowired(required = false)
    public RestOrderController(AuthenticationConverter authenticationConverter, OrderService orderService, PaymentService paymentService, IamportClient iamportClient){
        this.authenticationConverter = authenticationConverter;
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.api = iamportClient;
    }

    /**
     * 주문 메소드.
     * @param request
     */
    @PostMapping("/order")
    public ResponseEntity<OrderResponse> order(@RequestBody OrderRequest request, Authentication authentication){
        Member findMember = authenticationConverter.getMemberFromAuthentication(authentication);

        if(null == request.getOrdererName() || request.getOrdererName().equals("")){
            log.info("orderName null");
            request.setOrdererName(findMember.getName());
            log.info(request.getOrdererName());
        }
        if(null == request.getOrdererPhone() || request.getOrdererPhone().equals("")){
            log.info("ordererPhone null");
            request.setOrdererPhone(findMember.getPhone());
            log.info(request.getOrdererPhone());
        }
        if(null == request.getOrdererEmail() || request.getOrdererEmail().equals("")){
            log.info("ordererEmail null");
            request.setOrdererEmail(findMember.getEmail());
            log.info(request.getOrdererEmail());
        }

        try{
            String orderNum = orderService.order(request, findMember);
            return new ResponseEntity<OrderResponse>(new OrderResponse("주문도메인 생성성공", orderNum), HttpStatus.OK);
        }catch (BusinessException e){
            return new ResponseEntity<OrderResponse>(new OrderResponse("주문도메인 생성실패", "empty", OrderErrorResponse.of(e.getMessage(), e.getErrorCode())),
                    HttpStatus.valueOf(e.getErrorCode().getCode()));
        }
    }

    /**
     * 결제 완료 후, 주문번호와 결제번호를 통해 유효성 검사를 실시합니다.(유효성 검사의 내용은 OrderService validatePaymentByIamport() 를 참조).
     * 유효성 검사 실패시, 환불 및 관련 주문 도메인을 삭제 처리합니다.
     * @param request
     * @return OrderResponse
     */
    @PostMapping("/order/complete")
    public ResponseEntity<OrderResponse> validatePaymentAfter(@RequestBody PaymentRequest request, Authentication authentication) throws IOException, IamportResponseException {
        Payment iamportPayment = null;
        try{
            iamportPayment = api.paymentByImpUid(request.getImp_uid()).getResponse();
            PaymentIamportDTO paymentDTO = PaymentIamportDTO.builder()
                    .paymentAmount(iamportPayment.getAmount().longValue())
                    .impUid(iamportPayment.getImpUid())
                    .merchantUid(iamportPayment.getMerchantUid())
                    .payMethod(iamportPayment.getPayMethod())
                    .payStatus(iamportPayment.getStatus())
                    .name(iamportPayment.getName())
                    .build();
            orderService.validatePaymentByIamport(paymentDTO, authentication.getName());
            // 결제 성공.
            return new ResponseEntity<OrderResponse>(new OrderResponse("결제 성공", "empty"), HttpStatus.OK);
        }catch (PaymentFailException paymentEx){
            try{
                if(iamportPayment != null) {
                    // payment 환불 요청.
                    paymentService.refundPaymentAll(iamportPayment.getImpUid(), iamportPayment.getAmount().longValue(), paymentEx.getErrorCode(), authentication.getName());
                    // 임시 주문 삭제.
                    orderService.deleteTempOrder(iamportPayment.getMerchantUid(), authentication.getName());
                }
            }catch (RefundFailException refundEx){
                // 환불 실패.
                return new ResponseEntity<OrderResponse>(new OrderResponse("결제 및 환불 실패", "empty", OrderErrorResponse.of(refundEx.getMessage(), refundEx.getErrorCode())),
                        HttpStatus.OK);
            }
            // 결제 실패.
            return new ResponseEntity<OrderResponse>(new OrderResponse("결제 실패", "empty", OrderErrorResponse.of(paymentEx.getMessage(), paymentEx.getErrorCode())),
                    HttpStatus.OK);
        }
    }

}
