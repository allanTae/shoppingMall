package com.allan.shoppingMall.domains.payment.domain;

import com.allan.shoppingMall.domains.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    public Optional<Payment> findByPaymentNum(String paymentNum);
}
