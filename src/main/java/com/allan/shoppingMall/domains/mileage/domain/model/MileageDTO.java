package com.allan.shoppingMall.domains.mileage.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 마일리지 정보를 담고있는 오브젝트.
 */
@Getter
@Setter
@AllArgsConstructor
public class MileageDTO {
    private Long mileagePoint;
    private String mileageDesc;
}
