package com.allan.shoppingMall.domains.manage.presentation;

import com.allan.shoppingMall.common.domain.model.PageInfo;
import com.allan.shoppingMall.common.exception.BusinessException;
import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.category.CategoryNotFoundException;
import com.allan.shoppingMall.domains.category.domain.Category;
import com.allan.shoppingMall.domains.category.domain.CategoryCode;
import com.allan.shoppingMall.domains.category.domain.CategoryRepository;
import com.allan.shoppingMall.domains.item.domain.item.Item;
import com.allan.shoppingMall.domains.item.domain.model.ItemSummaryDTO;
import com.allan.shoppingMall.domains.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ManagerController {

    private final ItemService itemService;
    private final CategoryRepository categoryRepository;

    @GetMapping("/manage/itemList")
    public String getAllClothes(Pageable pageable, Model model){
        Page<Item> page = itemService.getAllItems(pageable);

        List<ItemSummaryDTO> itemDTOS = itemService.getItemDTOS(page.getContent());
        model.addAttribute("itemList", itemDTOS);
        model.addAttribute("pagination", new PageInfo(page.getNumber(), page.getTotalPages(), page.isFirst(), page.isLast())); // 페이징 정보.

        return "manage/manageList";
    }

    @GetMapping("/manage")
    public String updateForm(@RequestParam("itemId") Long itemId, @RequestParam("categoryId") Long categoryId){
        Category findCategory = categoryRepository.findById(categoryId).orElseThrow(() ->
                new CategoryNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        if(findCategory.getCategoryCode().getDesc().equals(CategoryCode.CLOTHES.getDesc())){
            return "redirect:/clothes/clothesForm?clothesId=" + itemId + "&mode=edit";
        }else if(findCategory.getCategoryCode().getDesc().equals(CategoryCode.ACCESSORY.getDesc())) {
            return "redirect:/accessory/accessoryForm?accessoryId=" + itemId + "&mode=edit";
        }else{
            throw new BusinessException("상품 카테고리가 아닙니다.", ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
