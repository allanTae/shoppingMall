package com.allan.shoppingMall.domains.order.presentation;

import com.allan.shoppingMall.common.domain.model.UserInfo;
import com.allan.shoppingMall.domains.infra.AuthenticationConverter;
import com.allan.shoppingMall.domains.member.domain.Member;
import com.allan.shoppingMall.domains.order.domain.model.OrderRequest;
import com.allan.shoppingMall.domains.order.domain.model.OrderSummaryRequest;
import com.allan.shoppingMall.domains.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping("/orderForm")
    public String orderForm(@ModelAttribute OrderSummaryRequest request, Authentication authentication, Model model){
        model.addAttribute("orderInfo", request);
        setUserInfo(model, authentication);
        return "order/orderForm";
    }

    @PostMapping("/save")
    public String order(@ModelAttribute OrderRequest request, Authentication authentication){

        log.info("request: " + request.toString());

        Member findMember = authenticationConverter.getMemberFromAuthentication(authentication);

        if(null == request.getOrdererName()){
            request.setOrdererName(findMember.getName());
        }
        if(null == request.getOrdererPhone()){
            request.setOrdererPhone(findMember.getPhone());
        }
        if(null == request.getOrdererEmail()){
            request.setOrdererEmail(findMember.getEmail());
        }

        orderService.order(request, findMember);

        return "redirect:/order/orderResult";
    }

    @GetMapping("/orderResult")
    public String getOrderResult(){
        return "order/orderResult";
    }


    /**
     * 로그인한 계정의 정보를 전달하는 메소드.
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
