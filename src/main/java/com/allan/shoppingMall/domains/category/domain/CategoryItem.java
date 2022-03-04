package com.allan.shoppingMall.domains.category.domain;

import com.allan.shoppingMall.common.domain.BaseTimeEntity;
import com.allan.shoppingMall.domains.item.domain.item.Item;
import com.allan.shoppingMall.domains.order.domain.OrderItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Objects;

/**
 * 상품 도메인과 카테고리 도메인의 다대다 매핑을 위한 중간 도메인 입니다.
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "category_items")
@Slf4j
public class CategoryItem extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private Category category;

    /**
     * 생성자 함수 입니다.
     * 필수로 입력되어야 하는 파라미터들이기 때문에 빌더 패턴을 사용하지 않습니다.
     * @param category
     */
    public CategoryItem(Category category) {
        this.category = category;
    }

    /**
     * 연관 관계 편의 메소드.
     * @param item
     */
    public void changeitem(Item item){
        this.item = item;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getCategory().getCategoryId());
    }

    /**
     * 카테고리 도메인 아이디로 비교.
     * @param obj
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        log.info("CategoryItem equals() call!!!");
        if(obj == null){
            log.error("CategoryItem equals()'s parameter is null");
            return false;
        }
        if(this == obj)
            return true;
        if(!(obj instanceof CategoryItem))
            return false;

        return this.getCategory().getCategoryId() == ((CategoryItem) obj).getCategory().getCategoryId();
    }

}
