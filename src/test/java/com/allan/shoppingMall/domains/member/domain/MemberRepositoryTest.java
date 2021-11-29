package com.allan.shoppingMall.domains.member.domain;

import com.allan.shoppingMall.common.config.jpa.auditing.JpaAuditingConfig;
import com.allan.shoppingMall.common.value.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = JpaAuditingConfig.class
))

@WithMockUser
public class MemberRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 아이디로_회원_조회() throws Exception {
        //given
        Member TEST_MEMBER = createMember();
        entityManager.persist(TEST_MEMBER);

        //when
        boolean present = memberRepository.findByAuthIdLike(TEST_MEMBER.getAuthId()).isPresent();

        //then
        assertTrue(present);
    }

    private Member createMember() {
        return Member.builder()
                .name("testName")
                .age(4)
                .address(Address.builder()
                        .postCode("test postCode")
                        .roadAddress("test roadAddress")
                        .detailAddress("test detailAddress")
                        .jibunAddress("test jibunAddress")
                        .extraAddress("test extraAddress")
                        .build())
                .role(MemberRole.ACTIVATED_USER)
                .email("testEmail")
                .dateOfBirth("19990212")
                .gender(Gender.MAN)
                .phone("000-1234-1234")
                .authId("testAuthId")
                .pwd("testPwd")
                .build();
    }
}
