package com.allan.shoppingMall.domains.item.domain;

import com.allan.shoppingMall.domains.item.domain.clothes.Clothes;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesSummaryDTO;
import com.allan.shoppingMall.domains.item.domain.model.ClothesDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    /**
     * 의류 리스트 정보를 반환하는 메소드.
     */
    @Query("select distinct item from Item item join fetch item.itemImages image")
    public List<Clothes> getClothesList();

}
