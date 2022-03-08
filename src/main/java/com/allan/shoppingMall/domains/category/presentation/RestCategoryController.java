package com.allan.shoppingMall.domains.category.presentation;

import com.allan.shoppingMall.common.exception.category.CategoryCreateFailException;
import com.allan.shoppingMall.common.exception.category.CategoryNotFoundException;
import com.allan.shoppingMall.domains.category.domain.model.CategoryDTO;
import com.allan.shoppingMall.domains.category.domain.model.CategoryRequest;
import com.allan.shoppingMall.domains.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RestCategoryController {

    private final CategoryService categoryService;

    @ResponseBody
    @PostMapping("/category")
    public ResponseEntity<CategoryResponse> saveCategory(@RequestBody CategoryRequest categoryRequest){
        try{
            categoryService.saveCategory(categoryRequest);
        }catch (CategoryCreateFailException exception){
            return new ResponseEntity<CategoryResponse>(new CategoryResponse(CategoryResult.SAVE_CATEGORY_FAIL,
                    CategoryErrorResponse.of(exception.getErrorCode())), HttpStatus.OK);
        }

        return new ResponseEntity<CategoryResponse>(new CategoryResponse(CategoryResult.SAVE_CATEGORY_SUCCESS), HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/category/{branch}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable("branch")String branch){
        CategoryDTO category = null;
        try {
            category = categoryService.getEntireCategoryByBranch(branch);
        }catch (CategoryNotFoundException exception){
            return new ResponseEntity<CategoryResponse>(new CategoryResponse(branch + "관련 카테고리 정보를 찾을 수 없습니다.", false,
                    CategoryErrorResponse.of(exception.getErrorCode())), HttpStatus.OK);
        }
        return new ResponseEntity<CategoryResponse>(new CategoryResponse(CategoryResult.GET_CATEGORY_SUCCESS, category), HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/category/shop")
    public ResponseEntity<CategoryResponse> getCategory(){
        CategoryDTO category = null;
        try {
            category = categoryService.getShopCategoryByBranch();
        }catch (CategoryNotFoundException exception){
            return new ResponseEntity<CategoryResponse>(new CategoryResponse("shop 관련 카테고리 정보를 찾을 수 없습니다.", false,
                    CategoryErrorResponse.of(exception.getErrorCode())), HttpStatus.OK);
        }
        return new ResponseEntity<CategoryResponse>(new CategoryResponse(CategoryResult.GET_CATEGORY_SUCCESS, category), HttpStatus.OK);
    }

    @ResponseBody
    @PutMapping("/category/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable("categoryId") Long categoryId, @RequestBody CategoryRequest categoryRequest){
        try {
            categoryService.updateCategory(categoryId, categoryRequest);
        }catch (CategoryNotFoundException exception){
            return new ResponseEntity<CategoryResponse>(new CategoryResponse(CategoryResult.MODIFY_CATEGORY_FAIL,
                    CategoryErrorResponse.of(exception.getErrorCode())), HttpStatus.OK);
        }
        return new ResponseEntity<CategoryResponse>(new CategoryResponse(CategoryResult.MODIFY_CATEGORY_SUCCESS), HttpStatus.OK);
    }

    /**
     * 카테고리 삭제 메소드.
     * @param categoryId 카테고리 도메인 id.
     */
    @ResponseBody
    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<CategoryResponse> deleteCategory(@PathVariable("categoryId") Long categoryId){
        try {
            categoryService.deleteCategory(categoryId);
        }catch (CategoryNotFoundException exception){
            return new ResponseEntity<CategoryResponse>(new CategoryResponse(CategoryResult.DELETE_CATEGORY_FAIL,
                    CategoryErrorResponse.of(exception.getErrorCode())), HttpStatus.OK);
        }
        return new ResponseEntity<CategoryResponse>(new CategoryResponse(CategoryResult.DELETE_CATEGORY_SUCCESS), HttpStatus.OK);
    }
}
