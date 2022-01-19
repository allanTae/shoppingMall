package com.allan.shoppingMall.common.config.security;

import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.MemberNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
public class SignInFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("signIn Faile: ");
        log.error("exception: " + exception);
        String errorMsg = "";
        if(exception instanceof UsernameNotFoundException){
            errorMsg = UTFEncode(exception.getMessage());
        }else if(exception instanceof BadCredentialsException){
            errorMsg = UTFEncode(ErrorCode.LOGIN_FAIL.getMessage());
        }

        response.sendRedirect("loginForm?errorMsg=" + errorMsg);

    }

    private String UTFEncode(String msg){
        return URLEncoder.encode(msg, StandardCharsets.UTF_8);
    }
}
