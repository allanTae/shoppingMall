package com.allan.shoppingMall.domains.order.presentation;

import com.allan.shoppingMall.common.domain.model.UserInfo;
import com.allan.shoppingMall.domains.infra.AuthenticationConverter;
import com.allan.shoppingMall.domains.member.domain.Member;
import com.allan.shoppingMall.domains.mileage.service.MileageService;
import com.allan.shoppingMall.domains.order.domain.model.*;
import com.allan.shoppingMall.domains.order.service.OrderService;
import com.allan.shoppingMall.domains.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/order")
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final AuthenticationConverter authenticationConverter;
    private final OrderService orderService;
    private final MileageService mileageService;

    @RequestMapping("/orderForm")
    public String orderForm(@ModelAttribute OrderSummaryRequest request, Authentication authentication, Model model){

        // 주문 정보
        model.addAttribute("orderInfo", request);
        setUserInfo(model, authentication);

        // 마일리지 정보.
        long availableMileagePoint = mileageService.getAvailableMileagePoint(authentication.getName());
        model.addAttribute("availableMileage", availableMileagePoint);

        return "order/orderForm";
    }

    /**
     * 주문 메소드.
     * @param request
     */
    @PostMapping("/save")
    public String order(@ModelAttribute OrderRequest request, Authentication authentication){
        Member findMember = authenticationConverter.getMemberFromAuthentication(authentication);

        if(null == request.getOrdererName() || request.getOrdererName().equals("")){
            request.setOrdererName(findMember.getName());
        }
        if(null == request.getOrdererPhone() || request.getOrdererPhone().equals("")){
            request.setOrdererPhone(findMember.getPhone());
        }
        if(null == request.getOrdererEmail() || request.getOrdererEmail().equals("")){
            request.setOrdererEmail(findMember.getEmail());
        }
        orderService.order(request, findMember);

        return "redirect:/order/orderResult";
    }

    /**
     * 주문 후, 주문 결과 페이지를 출력하는 메소드.
     * 주문페이지(orderForm.jsp)에서 Iamport api 결제 요청 실패시, 생성 된 '임시저장' 주문를 삭제 합니다.
     * (모바일 환경 결제 실패시 호출 되는 핸들러 메소드이기도 합니다.)
     * @param orderResultRequest 주문 결과 정보를 담고 있는 request object.
     * @return
     */
    @PostMapping("/orderResult")
    public String getOrderResult(@ModelAttribute("orderResult") OrderResultRequest orderResultRequest, Authentication authentication){
        // 임시 주문 삭제.
        orderService.deleteAllTempOrder(authentication.getName());

        return "redirect:/order/orderResult";
    }

    /**
     * 주문 취소 함수.
     * @param orderNum OrderEntity id.
     */
    @PostMapping("/cancel/{orderNum}")
    public String cancelOrder(@PathVariable("orderNum") String orderNum, Authentication authentication){
        orderService.cancelMyOrder(orderNum, authentication.getName());
        return "redirect:/myOrder/list";
    }

    /**
     * 상세 주문에 대한 조회 함수.
     * @param orderId
     */
    @GetMapping("/{orderId}")
    public String getOrderDetail(@PathVariable("orderId") Long orderId, Model model){

        // 주문정보 설정.
        OrderDetailDTO orderDetail = orderService.getOrderDetailDTO(orderId);
        model.addAttribute("orderInfo", orderDetail);

        return "order/orderDetail";
    }

    @GetMapping("/order/error")
    public String getOrderFailResult(@RequestParam("imp_uid") String impUid,
                                     @RequestParam("merchant_uid") String merchantUid,
                                     @RequestParam("imp_success") boolean impSuccess,
                                     @RequestParam("error_msg") String errorMsg,
                                     Model model){

        model.addAttribute("imp_uid", impUid);
        model.addAttribute("merchant_uid", merchantUid);
        model.addAttribute("imp_success", impSuccess);
        model.addAttribute("error_msg", errorMsg);
        return "order/orderFailResult";
    }

    /**
     * 로그인한 계정의 정보를 전달하는 메소드.
     * @param model
     * @param authentication
     */
    private void setUserInfo(Model model, Authentication authentication){
        Member findMember = authenticationConverter.getMemberFromAuthentication(authentication);
        model.addAttribute("userInfo", UserInfo.builder()
                                                        .memberId(findMember.getMemberId())
                                                        .name(findMember.getName())
                                                        .email(findMember.getEmail())
                                                        .phone(findMember.getPhone())
                                                        .build());
    }

}
