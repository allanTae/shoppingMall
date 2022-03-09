package com.allan.shoppingMall.domains.item.presentation;

import com.allan.shoppingMall.common.exception.BusinessException;
import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.category.CategoryNotFoundException;
import com.allan.shoppingMall.domains.category.domain.Category;
import com.allan.shoppingMall.domains.category.domain.CategoryCode;
import com.allan.shoppingMall.domains.category.domain.CategoryRepository;
import com.allan.shoppingMall.domains.item.domain.accessory.Accessory;
import com.allan.shoppingMall.domains.item.domain.model.AccessoryDTO;
import com.allan.shoppingMall.domains.item.domain.model.ClothesDTO;
import com.allan.shoppingMall.domains.item.service.AccessoryService;
import com.allan.shoppingMall.domains.item.service.ClothesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ClothesService clothesService;
    private final CategoryRepository categoryRepository;
    private final AccessoryService accessoryService;

    /**
     * 단일 상품 조회 핸들러.
     * @param
     * @return
     */
    @GetMapping("/item")
    public String getClothes(@RequestParam("categoryId") Long categoryId, @RequestParam("itemId") Long itemId, Model model){
        log.info("getClothes() call!!");
        Category findCategory = categoryRepository.findById(categoryId).orElseThrow(() ->
                new CategoryNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        log.info("categoryCode: " + findCategory.getCategoryCode().getDesc());

        // 의류 상품 정보.
        if(findCategory.getCategoryCode().getDesc().equals(CategoryCode.CLOTHES.getDesc())){
            ClothesDTO clothes = clothesService.getClothes(itemId);
            model.addAttribute("clothesInfo", clothes);

            return "clothes/clothesDetail";

        // 악세서리 상품 정보.
        }else if(findCategory.getCategoryCode().getDesc().equals(CategoryCode.ACCESSORY.getDesc())){
            AccessoryDTO accessory = accessoryService.getAccessory(itemId);
            model.addAttribute("accessoryInfo", accessory);
            return "accessory/accessoryDetail";
        }

        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
