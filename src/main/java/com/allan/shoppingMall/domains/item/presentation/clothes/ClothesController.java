package com.allan.shoppingMall.domains.item.presentation.clothes;

import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import com.allan.shoppingMall.domains.item.domain.model.ClothesDTO;
import com.allan.shoppingMall.domains.item.domain.model.ClothesForm;
import com.allan.shoppingMall.domains.item.service.ClothesService;
import com.allan.shoppingMall.domains.member.domain.Gender;
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
        if(clothesForm.getMode() != null && clothesForm.getMode().equals("edit")){
            clothesService.updateClothes(clothesForm);
        }else{
            clothesService.saveClothes(clothesForm);
        }

        return "redirect:/index";
    }

    @GetMapping("/clothes/clothesForm")
    public String clothesForm(@ModelAttribute("clothesForm") ClothesForm clothesForm, Model model, @RequestParam(name = "clothesId", required = false) Long clothesId
            , @RequestParam(name="mode", required = false) String mode){

        log.info("clothesId: " + clothesId);
        if(clothesId != null){
            ClothesDTO clothes = clothesService.getClothes(clothesId);
            clothes.setMode(mode);
            model.addAttribute("clothesInfo", clothes);
        }

        return "clothes/clothesForm";
    }

    @ModelAttribute("sizeLabels")
    public List<SizeLabel> genders(){
        List<SizeLabel> list = new ArrayList<SizeLabel>();
        list.add(SizeLabel.S);
        list.add(SizeLabel.M);
        list.add(SizeLabel.L);
        list.add(SizeLabel.FREE);

        return list;
    }

}
