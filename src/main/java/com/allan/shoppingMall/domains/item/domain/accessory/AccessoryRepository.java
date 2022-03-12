package com.allan.shoppingMall.domains.item.domain.accessory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccessoryRepository extends JpaRepository<Accessory, Long> {

}
