package com.allan.shoppingMall.domains.item.domain;

import com.allan.shoppingMall.common.domain.BaseEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "itemImages")
@Getter
public class ItemImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "item_image_name", nullable = false)
    private String itemImageName;

    @Column(name = "item_image_path", nullable = false)
    private String itemImagePath;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "image_type", nullable = false)
    private ImageType imageType;
}
