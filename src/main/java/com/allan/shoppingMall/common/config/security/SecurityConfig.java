package com.allan.shoppingMall.common.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/resources/**", "/auth/**")
                    .permitAll()
                .anyRequest()
                    .authenticated()
            .and()
                .formLogin()
                    .loginPage("/auth/signIn")
                    .failureUrl("/auth/signInError")
                    .loginProcessingUrl("auth/signIn")
                    .usernameParameter("userId")
                    .passwordParameter("userPwd")
                    .successHandler(new SignInSuccessHandler())
            .and()
                .csrf()
                .disable()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/auth/signIn");
    }

}
