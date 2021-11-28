package com.allan.shoppingMall;

import com.allan.shoppingMall.common.value.LoginUserInfo;
import com.allan.shoppingMall.domains.infra.AuthenticationConverter;
import com.allan.shoppingMall.domains.item.domain.clothes.Clothes;
import com.allan.shoppingMall.domains.item.service.ClothesService;
import com.allan.shoppingMall.domains.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class IndexController {

    private final ClothesService clothesService;
    private final AuthenticationConverter authenticationConverter;

    @RequestMapping("/index")
    public String getIndex(Model model, Authentication authentication){
        model.addAttribute("ClothesSummaryList", clothesService.getClothesSummary());
        if(authentication != null){
            getUserInfo(authentication, model);
        }

        return "/index";
    }

    public void getUserInfo(Authentication authentication, Model model){
        Member findMember = authenticationConverter.getMemberFromAuthentication(authentication);

        LoginUserInfo loginUserInfo = LoginUserInfo.builder()
                .userName(findMember.getName())
                .authId(findMember.getAuthId())
                .nickName(findMember.getNickName())
                .userRole(findMember.getRole().getRoleDetail())
                .build();

        model.addAttribute("loginUserInfo", loginUserInfo);
    }
}