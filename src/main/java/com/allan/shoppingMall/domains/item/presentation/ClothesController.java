package com.allan.shoppingMall.domains.item.presentation;

import com.allan.shoppingMall.domains.item.domain.clothes.ClothesFabric;
import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import com.allan.shoppingMall.domains.item.domain.model.ClothesDTO;
import com.allan.shoppingMall.domains.item.domain.model.ClothesDetailDTO;
import com.allan.shoppingMall.domains.item.domain.model.ClothesFabricDTO;
import com.allan.shoppingMall.domains.item.domain.model.ClothesForm;
import com.allan.shoppingMall.domains.item.service.ClothesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/clothes")
@Slf4j
@RequiredArgsConstructor
public class ClothesController {

    private final ClothesService clothesService;

    @PostMapping("/save")
    public String saveClothes(@ModelAttribute("clothesForm") ClothesForm clothesForm){
        clothesService.saveClothes(clothesForm);

        return "redirect:/index";
    }

    @GetMapping("/clothesForm")
    public String clothesForm(@ModelAttribute("clothesForm") ClothesForm clothesForm, Model model){
        List<SizeLabel> sizeLabelList = new ArrayList<>();

        return "clothes/clothesForm";
    }

    @GetMapping("/{clothesId}")
    public String getClothes(@PathVariable("clothesId") Long clothesId, Model model){
        ClothesDTO clothes = clothesService.getClothes(clothesId);
        model.addAttribute("clothesInfo", clothes);

        return "clothes/clothesDetail";
    }
}
