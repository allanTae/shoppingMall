package com.allan.shoppingMall.domains.item.domain.clothes;

import com.allan.shoppingMall.domains.item.domain.clothes.Clothes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClothesRepository extends JpaRepository<Clothes, Long> {

    /**
     * 의류 리스트 정보를 반환하는 메소드.
     */
    @Query("select distinct item from Item item join fetch item.itemImages image")
    public List<Clothes> getClothesList();

    /**
     * 단일 Clothes Entity 를 반환하기 위한 join fetch query.
     */
    @Query("select clothes from Clothes clothes join fetch clothes.itemImages" +
            " where clothes.itemId = :clothesId")
    public Optional<Clothes> getClothes(@Param("clothesId") Long clothesId);

}
