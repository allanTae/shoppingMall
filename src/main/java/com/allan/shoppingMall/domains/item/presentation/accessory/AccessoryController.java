package com.allan.shoppingMall.domains.item.presentation.accessory;

import com.allan.shoppingMall.domains.item.domain.model.AccessoryForm;
import com.allan.shoppingMall.domains.item.service.AccessoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AccessoryController {

    private final AccessoryService accessoryService;

    @PostMapping("/accessory/save")
    public String saveClothes(@ModelAttribute("accessoryForm") AccessoryForm form){
        accessoryService.saveAccessory(form);

        return "redirect:/index";
    }

    @GetMapping("/accessory/accessoryForm")
    public String clothesForm(@ModelAttribute("accessoryForm") AccessoryForm form){

        return "accessory/accessoryForm";
    }

}
