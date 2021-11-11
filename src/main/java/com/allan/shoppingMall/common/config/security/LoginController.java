package com.allan.shoppingMall.common.config.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class LoginController {

    @RequestMapping("/signIn")
    public String signIn(){
        return "auth/signIn";
    }

    @RequestMapping("/signInError")
    public String signInError(){
        return "auth/signInError";
    }
}
