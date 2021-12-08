package com.allan.shoppingMall.domains.item.domain.clothes;

import com.allan.shoppingMall.domains.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClothesSizeRepository extends JpaRepository<ClothesSize, Long> {

    @Query("select size from ClothesSize size where size.item = :item and size.sizeLabel like :sizeLabel")
    public ClothesSize getClothesSizebySizelabel(@Param("item") Item item, @Param("sizeLabel") SizeLabel sizeLabel);
}
