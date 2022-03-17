package com.allan.shoppingMall.domains.cart.domain;

import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import com.allan.shoppingMall.domains.item.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {


    @Modifying
    @Transactional
    @Query("delete from CartItem ci where ci.item = :item and ci.size not in :sizes")
    void deleteCartItems(@Param("item") Item item, @Param("sizes") List<SizeLabel> sizes);
}
