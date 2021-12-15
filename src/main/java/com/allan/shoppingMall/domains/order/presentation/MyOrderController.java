package com.allan.shoppingMall.domains.order.presentation;

import com.allan.shoppingMall.common.domain.model.PageInfo;
import com.allan.shoppingMall.domains.infra.AuthenticationConverter;
import com.allan.shoppingMall.domains.member.domain.Member;
import com.allan.shoppingMall.domains.order.domain.Order;
import com.allan.shoppingMall.domains.order.domain.OrderRepository;
import com.allan.shoppingMall.domains.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/myOrder")
@Slf4j
@RequiredArgsConstructor
public class MyOrderController {

    private final AuthenticationConverter authenticationConverter;
    private final OrderService orderService;

    /**
     * 로그인 한 회원이 자신의 주문 목록을 확인하기 위해 호출하는 메소드.
     */
    @GetMapping("/list")
    public String myOrderList(Authentication authentication, Pageable pageable, Model model){
        Member findMember = authenticationConverter.getMemberFromAuthentication(authentication);
        Page<Order> page = orderService.getMyOrderSummaryList(findMember.getAuthId(), pageable);

        model.addAttribute("myOrderList", orderService.getOrderSummaryDTO(page.getContent())); // 주문 상품.
        model.addAttribute("pagination", new PageInfo(page.getNumber(), page.getTotalPages(), page.isFirst(), page.isLast())); // 페이징 정보.

        return "order/myOrderList";
    }
}
