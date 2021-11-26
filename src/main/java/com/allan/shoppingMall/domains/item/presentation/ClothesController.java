package com.allan.shoppingMall.domains.item.presentation;

import com.allan.shoppingMall.domains.item.domain.model.ClothesForm;
import com.allan.shoppingMall.domains.item.service.ClothesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clothes")
@Slf4j
@RequiredArgsConstructor
public class ClothesController {

    private final ClothesService clothesService;

    @PostMapping("/save")
    public String saveClothes(@ModelAttribute("clothesForm") ClothesForm clothesForm){
        clothesService.saveClothes(clothesForm);
        return "catalog/main";
    }

    @GetMapping("/clothesForm")
    public String clothesForm(@ModelAttribute("clothesForm") ClothesForm clothesForm){
        return "clothes/clothesForm";
    }

}
