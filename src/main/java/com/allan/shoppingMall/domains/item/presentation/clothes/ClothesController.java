package com.allan.shoppingMall.domains.item.presentation.clothes;

import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
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
@Slf4j
@RequiredArgsConstructor
public class ClothesController {

    private final ClothesService clothesService;

    @PostMapping("/clothes/save")
    public String saveClothes(@ModelAttribute("clothesForm") ClothesForm clothesForm){
        clothesService.saveClothes(clothesForm);

        return "redirect:/index";
    }

    @GetMapping("/clothes/clothesForm")
    public String clothesForm(@ModelAttribute("clothesForm") ClothesForm clothesForm, Model model){
        List<SizeLabel> sizeLabelList = new ArrayList<>();

        return "clothes/clothesForm";
    }

}
