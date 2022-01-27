package com.allan.shoppingMall.domains.cart.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * 쿠키 값으로 비회원이 등록한 Cart 를 조회하는 메소드.
     * @param ckId
     * @return Cart
     */
    Optional<Cart> findByCkId(String ckId);

    /**
     * 로그인한 회원 아이디로 조회하는 메소드.
     * @param authId 로그인한 회원 아이디.
     * @return Cart
     */
    @Query("select c from Cart c where c.member.authId like :authId")
    Optional<Cart> findByAuthId(@Param("authId") String authId);
}
