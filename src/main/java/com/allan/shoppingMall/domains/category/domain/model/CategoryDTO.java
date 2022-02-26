package com.allan.shoppingMall.domains.category.domain.model;

import com.allan.shoppingMall.domains.category.domain.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 카테고리 정보를 저장하는 DTO 클래스.
 * 카테고리 정보를 벡단에서 프론트 단으로 전달 할 때 사용.
 */
@Getter
@Setter
public class CategoryDTO {

    private Long categoryId;
    private String branch;
    private String name; // 카테고리 이름.
    private String parentCategoryName; // 부모 카테고리 이름.
    private Integer depth;
    private Map<Long, CategoryDTO> child;

    /**
     * @param entity 카테고리 도메인.
     * @return CategoryDTO 카테고리 DTO 오브젝트.
     */
    public CategoryDTO(Category entity) {
        this. categoryId = entity.getCategoryId();
        this.branch = entity.getBranch();
        this.name = entity.getName();
        this.depth = entity.getDepth();

        if(entity.getParentCategory() == null){
            this.parentCategoryName = "대분류";
        }else{
            this.parentCategoryName = entity.getParentCategory().getName();
        }

        // 순환하면서 하위 카테고리 정보 셋.
//        this.child = entity.getChildCategory() == null ? null:entity.getChildCategory()
//                .stream()
//                .map(CategoryDTO::new)
//                .collect(Collectors.toList());

        this.child = entity.getChildCategory() == null ? null:entity.getChildCategory()
                .stream()
                .collect(Collectors.toMap(Category::getCategoryId, CategoryDTO::new));
    }


}
