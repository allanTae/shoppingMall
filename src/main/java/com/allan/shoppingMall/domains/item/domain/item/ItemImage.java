package com.allan.shoppingMall.domains.item.domain.item;

import com.allan.shoppingMall.common.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 상품의 이미지 정보들을 저장하는 엔티티.
 *
 * 이미지 파일의 이름이 동일한 경우에도 저장하기 위해서, 저장 된 파일은 원본 파일명이 아닌 커스텀하여
 * 저장합니다. (원본파일명은 별도로 저장합니다.)
 * itemImageName: 이미지 파일의 원본이름을 저장하는 필드.
 * itemImagePath: 애플리케이션 내 이미지 파일의 저장 경로.
 */

@Entity
@Table(name = "itemImages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "image_name", nullable = false)
    private String originalItemImageName;

    @Column(name = "image_path", nullable = false)
    private String itemImagePath;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "image_type", nullable = false)
    private ImageType imageType;

    @Column(nullable = false, name = "image_size")
    private Long imageSize;

    @Builder
    public ItemImage(String originalItemImageName, String itemImagePath, ImageType imageType, Long imageSize) {
        this.originalItemImageName = originalItemImageName;
        this.itemImagePath = itemImagePath;
        this.imageType = imageType;
        this.imageSize = imageSize;
    }

    /**
     * 양방향 매핑을 위한 연관 관계 편의 메소드.
     * Item Entity 측에서 ItemImage 추가하도록 비즈니스 로직을 처리함.
     * @param item
     */
    public void changeItem(Item item) {
        this.item = item;
    }

}
