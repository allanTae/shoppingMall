package com.allan.shoppingMall.common.config.jpa.auditing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
public class LoginIdAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 인증이 되지 않은 회원이거나 알 수 없는 회원인 경우
        if(null == authentication || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")){
            log.error("JpaAuditing's auditorAware error!!!!");
            return null;
        }

        if(authentication instanceof UsernamePasswordAuthenticationToken){
            UserDetails user = (UserDetails) authentication.getPrincipal();
            return Optional.of(user.getUsername());
        }else{
            // oauth2 인증시 로직 추가.
            return null;
        }
    }
}
