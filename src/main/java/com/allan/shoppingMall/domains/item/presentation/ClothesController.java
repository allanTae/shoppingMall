package com.allan.shoppingMall.domains.item.presentation;

import com.allan.shoppingMall.domains.item.domain.model.ClothesForm;
import com.allan.shoppingMall.domains.item.service.ClothesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
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

    // 나중에 삭제요망.
    @PostMapping("/api/save")
    public ClothesForm apiSaveClothes(@RequestBody ClothesForm clothesRequest){
        clothesService.saveClothes(clothesRequest);
        return clothesRequest;
    }



}
