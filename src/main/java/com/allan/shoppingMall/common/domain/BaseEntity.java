package com.allan.shoppingMall.common.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity extends BaseTimeEntity{

    @Column(nullable = false, updatable = false)
    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;

}
