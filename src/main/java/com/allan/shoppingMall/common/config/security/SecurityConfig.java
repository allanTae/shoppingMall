package com.allan.shoppingMall.common.config.security;

import com.allan.shoppingMall.domains.cart.domain.CartRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = CartRepository.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/clothes/save", "/clothes/clothesForm")
                    .hasAnyRole("ADMIN")
                .antMatchers("/resources/**", "/auth/**", "/member/signupForm",
                        "/member/checkId", "/member", "/image/**", "/index",
                        "/clothes/**", "/cart/**")
                    .permitAll()
                .anyRequest()
                    .authenticated()
            .and()
                .formLogin()
                    .loginPage("/auth/loginForm")
                    .loginProcessingUrl("/auth/login")
                    .usernameParameter("userId")
                    .passwordParameter("userPwd")
                    .failureHandler(new SignInFailHandler())
                    .successHandler(signInSuccessHandler())
            .and()
                .csrf()
                .disable()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"))
                .logoutSuccessUrl("/auth/loginForm");
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SignInSuccessHandler signInSuccessHandler(){
        return new SignInSuccessHandler();
    }

}
