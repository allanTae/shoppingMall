package com.allan.shoppingMall.common.config.security;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class LoginController {

    @RequestMapping("/signInForm")
    public String signIn(Model model){
        model.addAttribute("loginForm", new LoginForm());
        return "auth/signIn";
    }

    @RequestMapping("/signInError")
    public String signInError(){
        return "auth/signInError";
    }
}
