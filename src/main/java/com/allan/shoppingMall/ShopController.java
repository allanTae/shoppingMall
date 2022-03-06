package com.allan.shoppingMall;

import com.allan.shoppingMall.common.domain.model.PageInfo;
import com.allan.shoppingMall.domains.item.domain.item.Item;
import com.allan.shoppingMall.domains.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 판매하는 상품의 정보에 대한 처리를 담당하는 컨트롤러.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class ShopController {

    private final ItemService itemService;

    /**
     * 로그인 한 회원이 자신의 주문 목록을 확인하기 위해 호출하는 메소드.
     */
    @GetMapping("/shop")
    public String myOrderList(@RequestParam("categoryId") Long categoryId, Pageable pageable, Model model){
        Page<Item> page = itemService.getItems(categoryId, pageable);

        model.addAttribute("itemList", itemService.getItemDTOS(page.getContent(), categoryId)); // 상품 정보.
        model.addAttribute("pagination", new PageInfo(page.getNumber(), page.getTotalPages(), page.isFirst(), page.isLast())); // 페이징 정보.

        return "shop/itemList";
    }
}
