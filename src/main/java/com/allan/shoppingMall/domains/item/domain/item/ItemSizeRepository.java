package com.allan.shoppingMall.domains.item.domain.item;

import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ItemSizeRepository extends JpaRepository<ItemSize, Long> {

    /**
     * 특정 상품의 아이템 사이즈 도메인을 반환하는 메소드.
     * @param item
     * @param sizeLabel
     */
    @Query("select size from ItemSize size where size.item = :item and size.sizeLabel = :sizeLabel")
    public Optional<ItemSize> getItemSizebySizelabel(@Param("item") Item item, @Param("sizeLabel") SizeLabel sizeLabel);

}
