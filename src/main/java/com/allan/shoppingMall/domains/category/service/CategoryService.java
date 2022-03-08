package com.allan.shoppingMall.domains.category.service;

import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.category.CategoryCreateFailException;
import com.allan.shoppingMall.common.exception.category.CategoryNotFoundException;
import com.allan.shoppingMall.domains.category.domain.Category;
import com.allan.shoppingMall.domains.category.domain.CategoryCode;
import com.allan.shoppingMall.domains.category.domain.CategoryRepository;
import com.allan.shoppingMall.domains.category.domain.model.CategoryDTO;
import com.allan.shoppingMall.domains.category.domain.model.CategoryRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Category 서비스단 클래스 입니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 카테고리를 저장하는 메소드.
     * @param categoryRequest 프론트단에서 전달하는 카테고리 정보 오브젝트.
     */
    @Transactional
    public Long saveCategory(CategoryRequest categoryRequest){

        Category category = null;

        // 생성하고자 하는 카테고리가 대분류인 경우.
        if(categoryRequest.getParentCategoryName() == null){
            log.info("대분류 카테고리 생성.");
            // 이미, 대분류에 동일한 이름의 카테고리가 존재하는 경우.
            if(categoryRepository.existsByBranchAndName(categoryRequest.getBranch(), categoryRequest.getName())){
               throw new CategoryCreateFailException(ErrorCode.CATEGORY_BRANCH_AND_NAME_IS_DUPLICATION);
            }

            // Root category 조회시 없으면, 새로 생성.
            Category rootCategory = categoryRepository.findByBranchAndName(categoryRequest.getBranch(), "ROOT").orElseGet(() ->
                    Category.builder()
                            .branch(categoryRequest.getBranch())
                            .name("ROOT")
                            .categoryCode(CategoryCode.ROOT)
                            .depth(0)
                            .build());

            if(!categoryRepository.existsByBranchAndName(categoryRequest.getBranch(), "ROOT")){
                log.info("root save!!");
                categoryRepository.save(rootCategory);
            }

            category = Category.builder()
                .branch(categoryRequest.getBranch())
                .name(categoryRequest.getName())
                .parentCategory(rootCategory)
                .categoryCode(CategoryCode.valueOf(categoryRequest.getCategoryCode()))
                .depth(1)
                .build();
        }
        // 생성하고자 하는 카테고리가 대분류가 아닌 경우. (중, 소분류 인경우)
        else{
            log.info("중, 소분류 카테고리 생성.");
            Category parentCategory = categoryRepository.findByBranchAndName(categoryRequest.getBranch(), categoryRequest.getParentCategoryName()).orElseThrow(() ->
                    new CategoryCreateFailException(ErrorCode.ENTITY_NOT_FOUND));

            category = Category.builder()
                .name(categoryRequest.getName())
                .branch(categoryRequest.getBranch())
                .depth(parentCategory.getDepth() + 1)
                .parentCategory(parentCategory)
                .categoryCode(CategoryCode.valueOf(categoryRequest.getCategoryCode()))
                .build();
        }

        return categoryRepository.save(category).getCategoryId();

    }

    /**
     * 특정 그룹의 카테고리를 조회하는 메소드.
     * @param branch 조회 할 카테고리 그룹 정보.
     * @return categoryDTO 카테고리 DTO 오브젝트.
     */
    public CategoryDTO getEntireCategoryByBranch(String branch){
        Category findCategory = categoryRepository.findByBranchAndName(branch, "ROOT").orElseThrow(() ->
                new CategoryNotFoundException("찾는 대분류 카테고리가 존재하지 않습니다.",ErrorCode.ENTITY_NOT_FOUND));

        CategoryDTO categoryDTO = new CategoryDTO(findCategory);

       return categoryDTO;
    }

    /**
     * shop 카테고리를 조회하는 메소드.
     * @return categoryDTO 카테고리 DTO 오브젝트.
     */
    public CategoryDTO getShopCategoryByBranch(){
        Category findCategory = categoryRepository.findByCategoryCode(CategoryCode.SHOP).orElseThrow(() ->
                new CategoryNotFoundException("shop 카테고리를 조회 할 수 없습니다.", ErrorCode.ENTITY_NOT_FOUND));

        CategoryDTO categoryDTO = new CategoryDTO(findCategory);

        return categoryDTO;
    }

    /**
     * 카테고리의 자식 카테고리를 포함한 모든 카테고리 아이디 리스트를 반환하는 메소드.
     * @param categoryId 카테고리 도메인 아이디.
     * @return List<Long> 카테고리 도메인 아이디 리스트.
     */
    public List<Long> getCategoryIds(Long categoryId){
        Category findCategory = categoryRepository.findById(categoryId).orElseThrow(() ->
                new CategoryNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        List<Long> categoryIdList = new ArrayList<>();
        categoryIdList.add(findCategory.getCategoryId());

        addChildCategoryId(categoryIdList, findCategory);

        return categoryIdList;
    }


    /** 연관된 모든 자식 카테고리 아이디를 리스트에 추가하는 메소드.
     * 재귀를 통해, 모든 자식 카테고리 아이디를 조회합니다.
     * @param categoryIdList
     * @param parentCategory
     */
    private void addChildCategoryId(List<Long> categoryIdList, Category parentCategory){
        if(parentCategory.getChildCategory().size() == 0)
            return;

        for(Category childCategory : parentCategory.getChildCategory()){
            categoryIdList.add(childCategory.getCategoryId());
            addChildCategoryId(categoryIdList, childCategory);
        }
    }

    /**
     * @param categoryId 변경 할 카테고리 도메인 id.
     * @param categoryRequest 프론트단에서 전달 할 카테고리 변경 정보.
     * @return category domain id.
     */
    @Transactional
    public Long updateCategory(Long categoryId, CategoryRequest categoryRequest){
        Category findCategory = categoryRepository.findById(categoryId).orElseThrow(() ->
                new CategoryNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        findCategory.changeName(categoryRequest.getName());
        return findCategory.getCategoryId();
    }

    /**
     * @param categoryId
     * @return
     */
    @Transactional
    public Long deleteCategory(Long categoryId){
        Category findCategory = categoryRepository.findById(categoryId).orElseThrow(() ->
                new CategoryNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        if(findCategory.getChildCategory().size() == 0){
            categoryRepository.delete(findCategory);
        }else{
            if(!(findCategory.getName().equals("deleted category")))
                findCategory.changeName("deleted category");
            else if(findCategory.getName().equals("deleted category")){
                categoryRepository.delete(findCategory);
            }
        }
        return findCategory.getCategoryId();
    }
}
