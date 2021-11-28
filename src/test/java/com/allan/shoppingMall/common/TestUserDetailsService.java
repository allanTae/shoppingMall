package com.allan.shoppingMall.common;

import com.allan.shoppingMall.domains.member.domain.MemberRole;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 관리자 계정 인증용 테스트 DetailService.
 */
@Service
public class TestUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new User("testId", "testPwd", List.of(new SimpleGrantedAuthority(MemberRole.ACTIVATED_ADMIN.getSecurityKey())));
    }
}
