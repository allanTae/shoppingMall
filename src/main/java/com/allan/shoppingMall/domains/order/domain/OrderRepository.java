package com.allan.shoppingMall.domains.order.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select distinct o from Order o join o.orderItems where o.orderer.authId like :authId")
    public Page<Order> getOrderListByAuthId(@Param("authId") String authId, Pageable pageable);
}
