package com.allan.shoppingMall.domains.infra;

import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.MemberNotFoundException;
import com.allan.shoppingMall.domains.member.domain.Member;
import com.allan.shoppingMall.domains.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationConverter {

    private final MemberRepository memberRepository;

    public Member getMemberFromAuthentication(Authentication authentication){
        String authId = "";
        if(authentication instanceof UsernamePasswordAuthenticationToken){
            authId = authentication.getName();
        }
        return memberRepository.findByAuthIdLike(authId).orElseThrow(() -> new MemberNotFoundException(ErrorCode.ENTITY_NOT_FOUND.getMessage(), ErrorCode.ENTITY_NOT_FOUND));
    }
}
