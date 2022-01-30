package com.allan.shoppingMall.domains.order.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * 현재 로그인 한 회원의 주문 리스트 페이징 정보를 반환하는 메소드.
     * @param authId 로그인 한 회원 아이디.
     * @param orderStatusList 조회 할 주문의 주문상태 정보.
     * @param pageable 페이징 정보.
     */
    @Query("select o from Order o where o.orderer.authId like :authId and o.orderStatus in :orderStatusList")
    public Page<Order> getOrderListByAuthId(@Param("authId") String authId, @Param("orderStatusList") List<OrderStatus> orderStatusList, Pageable pageable);

    /**
     * 현재 로그인 한 회원의 특정 주문번호로 조회 한 주문 도메인.
     * @param authId
     * @param orderNum
     */
    @Query("select o from Order o where o.orderer.authId like :authId and o.orderNum like :orderNum")
    public Optional<Order> findByOrderNumAndAuthId(@Param("authId") String authId, @Param("orderNum") String orderNum);

    /**
     * 현재 로그인한 회원의 '임시주문' 상태의 주문 도메인 리스트를 조회하는 메소드.
     * @param authId
     * @param orderStatus
     * @return
     */
    @Query("select o from Order o where o.orderer.authId like :authId and o.orderStatus = :orderStatus")
    public List<Order> getOrderIdsByAuthId(@Param("authId") String authId, @Param("orderStatus") OrderStatus orderStatus);

}
