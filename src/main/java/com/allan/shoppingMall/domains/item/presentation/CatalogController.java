package com.allan.shoppingMall.domains.item.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/catalog")
public class CatalogController {

    @GetMapping("/main")
    public String mainPage(){
        return "catalog/main";
    }
}
