package com.allan.shoppingMall.domains.item.domain.clothes;

import com.allan.shoppingMall.domains.item.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemSizeRepository extends JpaRepository<ItemSize, Long> {

    /**
     * 특정 상품의 아이템 사이즈 도메인을 반환하는 메소드.
     * @param item
     * @param sizeLabel
     */
    @Query("select size from ItemSize size where size.item = :item and size.sizeLabel like :sizeLabel")
    public ItemSize getItemSizebySizelabel(@Param("item") Item item, @Param("sizeLabel") SizeLabel sizeLabel);
}
