package com.allan.shoppingMall.domains.order.presentation;

import com.allan.shoppingMall.common.exception.BusinessException;
import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.order.payment.PaymentFailException;
import com.allan.shoppingMall.common.exception.order.RefundFailException;
import com.allan.shoppingMall.common.util.FormatUtil;
import com.allan.shoppingMall.domains.infra.AuthenticationConverter;
import com.allan.shoppingMall.domains.member.domain.Member;
import com.allan.shoppingMall.domains.order.domain.model.OrderRequest;
import com.allan.shoppingMall.domains.order.service.OrderService;
import com.allan.shoppingMall.domains.payment.domain.model.iamport.PaymentIamportDTO;
import com.allan.shoppingMall.domains.payment.domain.model.PaymentRequest;
import com.allan.shoppingMall.domains.payment.service.PaymentService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.io.IOException;
import java.util.regex.Pattern;


@RestController
@Slf4j
public class RestOrderController {

    private AuthenticationConverter authenticationConverter;
    private OrderService orderService;
    private PaymentService paymentService;
    private IamportClient api;

    @Value("${payment.iamport.apiKey}")
    private String apiKey;

    @Value("${payment.iamport.apiSecret}")
    private String apiSecret;

    @Autowired(required = false)
    public RestOrderController(AuthenticationConverter authenticationConverter, OrderService orderService, PaymentService paymentService){
        this.authenticationConverter = authenticationConverter;
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.api = new IamportClient(apiKey, apiSecret);
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
     * Iamport 설정을 위한 초기화 메소드.
     */
    @PostConstruct
    public void init(){
        if(!apiKey.equals("testApiKey") && !apiSecret.equals("testApiSecret"))
            this.api = new IamportClient(apiKey, apiSecret);
    }

    /**
     * 주문 메소드.
     * @param request
     */
    @PostMapping("/order")
    public ResponseEntity<OrderResponse> order(@RequestBody @Valid OrderRequest request, BindingResult bindingResult, Authentication authentication){

        // 유효성 검사를 위한 표현식.
        String NAME_PATTERN = "^[가-힣]{2,16}$";
        String EMAIL_PATTERN = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
        String PHONE_PATTERN = "^0\\d{1,2}[ -]\\d{3,4}[ -]?\\d{3,4}$";
        String POSTCODE_PATTERN = "^(\\d{3}\\d{3}|\\d{5})$";
        String ROAD_ADDRESS_PATTERN = "";
        String JIBUN_ADDRESS_PATTERN = "";
        String DETAIL_ADDRESS_PATTERN = "";

        if(!Pattern.compile(NAME_PATTERN).matcher(request.getOrdererName().trim()).find()){
            log.error("restOrderController's order() fail ordererName validation check!!");
            bindingResult.rejectValue("ordererName", "ordererName.invalidatedVal", "주문자 이름은 한글 2~16자로 작성하셔야 합니다.");
        }
        if(!Pattern.compile(PHONE_PATTERN).matcher(FormatUtil.phoneFormat(request.getOrdererPhone().trim())).find()){
            log.error("restOrderController's order() fail ordererPhone validation check!!");
            bindingResult.rejectValue("ordererPhone", "ordererPhone.invalidatedVal", "주문자 휴대전화를 올바르게 입력 해 주세요.");
        }
        if(!Pattern.compile(EMAIL_PATTERN).matcher(request.getOrdererEmail().trim()).find()){
            log.error("restOrderController's order() fail ordererEmail validation check!!");
            bindingResult.rejectValue("ordererEmail", "ordererEmail.invalidatedVal", "주문자 이메일을 올바르게 입력 해 주세요.");
        }

        if(!Pattern.compile(NAME_PATTERN).matcher(request.getRecipientName().trim()).find()){
            log.error("restOrderController's order() fail recipient validation check!!");
            bindingResult.rejectValue("recipientName", "recipientName.invalidatedVal", "배송 수령자 이름은 한글 2~16자로 작성하셔야 합니다.");
        }
        if(!Pattern.compile(PHONE_PATTERN).matcher(FormatUtil.phoneFormat(request.getRecipientPhone().trim())).find()){
            log.error("restOrderController's order() fail recipientPhone validation check!!");
            bindingResult.rejectValue("recipientPhone", "recipientPhone.invalidatedVal", "배송 수령자는 휴대전화를 올바르게 입력 해 주세요.");
        }
        if(!Pattern.compile(POSTCODE_PATTERN).matcher(request.getPostcode().trim()).find()){
            log.error("restOrderController's order() fail postcode validation check!!");
            bindingResult.rejectValue("postcode", "postcode.invalidatedVal", "우편번호를 올바르게 입력 해 주세요.");
        }

        if(bindingResult.hasErrors()){
            log.error("========biningFilerError List========");
            for(FieldError error: bindingResult.getFieldErrors()){
                log.error(error.getDefaultMessage());
            }
            log.error("======================================");
            return new ResponseEntity<OrderResponse>(new OrderResponse("주문도메인 생성실패", "empty", OrderErrorResponse.of(ErrorCode.INVALID_ORDER_REQUEST_INPUT_VALUE, bindingResult)),
                    HttpStatus.OK);
        }

        request.setAddress(request.getRoadAddress() + " " + request.getJibunAddress());

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
            return new ResponseEntity<OrderResponse>(new OrderResponse("주문도메인 생성실패", "empty", OrderErrorResponse.of(e.getErrorCode())),
                    HttpStatus.OK);
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
        }catch (IOException | IamportResponseException e){
            log.error("validatePaymentAfter() cause iamport error!!");
            log.error("message: " + e.getMessage());
            return new ResponseEntity<OrderResponse>(new OrderResponse("결제 및 환불 실패", "empty", OrderErrorResponse.of(e.getMessage(), ErrorCode.IAMPORT_ERROR)),
                    HttpStatus.OK);
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
                return new ResponseEntity<OrderResponse>(new OrderResponse("결제 및 환불 실패", "empty", OrderErrorResponse.of(refundEx.getErrorCode())),
                        HttpStatus.OK);
            }
            // 결제 실패.
            return new ResponseEntity<OrderResponse>(new OrderResponse("결제 실패", "empty", OrderErrorResponse.of(paymentEx.getErrorCode())),
                    HttpStatus.OK);
        }
    }

}
