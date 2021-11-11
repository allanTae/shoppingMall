package com.allan.shoppingMall.common.config.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseUserDetailService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // test 용 계정. (추후에 데이터베이스 연동으로 테스트 필요.)
        return new User("testid", "testpwd", List.of(new SimpleGrantedAuthority("user")));
    }
}
