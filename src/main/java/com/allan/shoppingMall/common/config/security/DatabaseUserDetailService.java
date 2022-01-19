package com.allan.shoppingMall.common.config.security;

import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.domains.member.domain.Member;
import com.allan.shoppingMall.domains.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseUserDetailService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("security authentication");
        // test 용 계정. (추후에 데이터베이스 연동으로 테스트 필요.)
        Member findMember = memberRepository.findByAuthIdLike(username).orElseThrow(() -> new UsernameNotFoundException(ErrorCode.LOGIN_FAIL.getMessage()));
        return new User(findMember.getAuthId(), passwordEncoder.encode(findMember.getPwd()), List.of(new SimpleGrantedAuthority(findMember.getRole().getSecurityKey())));
    }
}
