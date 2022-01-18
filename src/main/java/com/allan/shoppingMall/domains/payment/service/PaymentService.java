package com.allan.shoppingMall.domains.payment.service;

import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.PaymentNotFoundException;
import com.allan.shoppingMall.common.exception.order.RefundFailException;
import com.allan.shoppingMall.domains.payment.domain.PaymentRepository;
import com.allan.shoppingMall.domains.payment.domain.model.PaymentDTO;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * 결제 도메인 서비스단 코드입니다.(IamPort api 에 의존하고 있습니다.)
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class PaymentService {

    private PaymentRepository paymentRepository;
    private IamportClient api;

    @Value("${payment.iamport.apiKey}")
    private String apiKey;

    @Value("${payment.iamport.apiSecret}")
    private String apiSecret;

    @Autowired(required = false)
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * PaymentServiceTest 에서 PaymentService 를 테스트하기 위한
     * 테스트용 생성자입니다.
     * 운용시에도 잘 동작하는 지 테스트 요망.
     */
    @Autowired(required = false)
    public PaymentService(PaymentRepository paymentRepository, IamportClient client) {
        this.paymentRepository = paymentRepository;
        this.api = client;
    }

    /**
     * Iamport 설정을 위한 초기화 메소드.
     */
    @PostConstruct
    private void init(){
        this.api = new IamportClient(apiKey, apiSecret);
    }

    /**
     * 결제 실패로 인하여 결제 금액을 모두 환불 요청하는 메소드입니다.
     * 결제 환불을 진행 합니다. 임시저장 상태의 주문 도메인은 삭제합니다.(단, 결제금액과 주문금액의 일치 유효성 검사 실패 경우에만 삭제합니다.)
     * @param impUid iamport api 결제 시 제공하는 고유 결제 번호입니다.
     * @param cancelAmount 결제 할 총 금액.
     * @param errorCode error 정보입니다.
     * @return Payment domain의 결제번호.
     */
    @Transactional
    public String refundPaymentAll(String impUid, Long cancelAmount, ErrorCode errorCode, String authId){

        // 환불 요청 정보를 생성합니다.
        CancelData cancelData = new CancelData(impUid, true);
        cancelData.setChecksum(BigDecimal.valueOf(cancelAmount));
        cancelData.setReason(errorCode.getMessage());

        // 환불 요청합니다.
        com.siot.IamportRestClient.response.Payment cancelResponse = null;
        try{
            cancelResponse = api.cancelPaymentByImpUid(cancelData).getResponse();
        }catch (Exception e){
            // iamport api request 예외 전환.
            if(e instanceof IamportResponseException || e instanceof IOException){
                throw new RefundFailException(e.getMessage(), ErrorCode.IAMPORT_ERROR);
            }

        }

        // 결제 실패로 인한 환불 로직은 Payment 객체가 생성되어 있지 않기 때문에, 결제 정보를 수정하지 않습니다.
        // 환불정보 Payment domain update.
//        com.siot.IamportRestClient.response.Payment finalCancelResponse = cancelResponse;
//        paymentRepository.findByPaymentNum(impUid).ifPresent(payment ->
//                payment.changeCancelAmount(finalCancelResponse.getCancelAmount().longValue()));

        return cancelResponse.getMerchantUid();
    }

    /**
     * @param impUid
     * @return paymentDTO 사용자에게 결제 정보를 전달하기 위한 Object
     */
    public PaymentDTO getPamentDetail(String impUid){
        Payment iamportPayment = null;
        PaymentDTO payment = null;
        try{
            iamportPayment = api.paymentByImpUid(impUid).getResponse();
        }catch (Exception e){
            if(e instanceof  IamportResponseException || e instanceof  IOException){
                throw new PaymentNotFoundException(e.getMessage(), ErrorCode.IAMPORT_ERROR);
            }
        }

        if(iamportPayment != null){
            payment = PaymentDTO.builder()
                    .payMethod(iamportPayment.getPayMethod())
                    .totalAmount(iamportPayment.getAmount().longValue())
                    .build();
            // card 결제정보.
            if(iamportPayment.getPayMethod().equals("card")){
                payment.setCardInfo(iamportPayment.getCardNumber(), iamportPayment.getCardName());
                payment.setCardName(iamportPayment.getCardName());
            }

        }
        return payment;
    }


}
