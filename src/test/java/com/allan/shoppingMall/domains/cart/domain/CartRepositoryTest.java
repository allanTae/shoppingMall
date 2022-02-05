package com.allan.shoppingMall.domains.cart.domain;

import com.allan.shoppingMall.common.config.jpa.auditing.JpaAuditingConfig;
import com.allan.shoppingMall.common.value.Address;
import com.allan.shoppingMall.domains.member.domain.Gender;
import com.allan.shoppingMall.domains.member.domain.Member;
import com.allan.shoppingMall.domains.member.domain.MemberRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@DataJpaTest(
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JpaAuditingConfig.class
        )
)
@WithMockUser
public class CartRepositoryTest {
    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    CartRepository cartRepository;

    @Test
    public void 쿠키_값으로_장바구니_조회테스트() throws Exception {
        //given
        Cart TEST_CART = Cart.builder()
                .ckId("testCkId")
                .build();

        testEntityManager.persist(TEST_CART);

        //when
        Cart cart = cartRepository.findByCkId(TEST_CART.getCkId()).get();

        //then
        assertThat(cart.getCkId(), is(TEST_CART.getCkId()));
    }

    @Test
    public void 회원아이디로_장바구니_조회테스트() throws Exception {
        Member TEST_MEMBER = createMember();

        Cart TEST_CART = Cart.builder()
                .member(TEST_MEMBER)
                .build();

        testEntityManager.persist(TEST_MEMBER);
        testEntityManager.persist(TEST_CART);
        testEntityManager.clear();

        //when
        Cart cart = cartRepository.findByAuthId(TEST_MEMBER.getAuthId()).get();

        //then
        assertThat(cart.getCartId(), is(TEST_CART.getCartId()));
    }

    private Member createMember() {
        return Member.builder()
                .name("testMemberName")
                .age(10)
                .gender(Gender.MAN)
                .email("testEmail")
                .address(Address.builder()
                        .postCode("65044")
                        .detailAddress("testDetailAddress")
                        .roadAddress("testRoadAddress")
                        .build())
                .authId("testAuthId")
                .pwd("testPwd")
                .role(MemberRole.ACTIVATED_USER)
                .phone("000-0000-0000")
                .dateOfBirth("20020204")
                .build();
    }
}
