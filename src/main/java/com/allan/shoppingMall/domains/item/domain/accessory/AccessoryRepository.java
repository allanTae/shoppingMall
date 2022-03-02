package com.allan.shoppingMall.domains.item.domain.accessory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccessoryRepository extends JpaRepository<Accessory, Long> {

    /**
     * 단일 Accessory Entity 를 반환하기 위한 join fetch query.
     */
    @Query("select accessory from Accessory accessory join fetch accessory.itemImages" +
            " where accessory.itemId = :accessoryId")
    public Optional<Accessory> getAccessory(@Param("accessoryId") Long accessoryId);
}
