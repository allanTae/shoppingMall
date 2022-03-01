package com.allan.shoppingMall.domains.category.service;

import com.allan.shoppingMall.domains.category.domain.Category;
import com.allan.shoppingMall.domains.category.domain.CategoryRepository;
import com.allan.shoppingMall.domains.category.domain.model.CategoryRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Rollback(value = true)
public class CategoryServiceTest {

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CategoryService categoryService;

    @Test
    public void 루트_카테고리가_없는_상황에서_대분류_카테고리_추가테스트() throws Exception {
        //given
        CategoryRequest TEST_CATEGORY_REQUEST = new CategoryRequest();
        TEST_CATEGORY_REQUEST.setBranch("testBranch");
        TEST_CATEGORY_REQUEST.setName("testName");
        TEST_CATEGORY_REQUEST.setParentCategoryName(null);
        TEST_CATEGORY_REQUEST.setCategoryCode(2);

        given(categoryRepository.existsByBranchAndName(TEST_CATEGORY_REQUEST.getBranch(), TEST_CATEGORY_REQUEST.getName()))
                .willReturn(false);

        Category TEST_ROOT_CATEGORY = Category.builder()

                .build();
        given(categoryRepository.findByBranchAndName(TEST_CATEGORY_REQUEST.getBranch(), "ROOT"))
                .willReturn(Optional.of(TEST_ROOT_CATEGORY));

        given(categoryRepository.existsByBranchAndName(TEST_CATEGORY_REQUEST.getBranch(), "ROOT"))
                .willReturn(true);

        Category TEST_SAVED_CATEGORY = Category.builder().build();
        ReflectionTestUtils.setField(TEST_SAVED_CATEGORY, "categoryId", 1l);

        given(categoryRepository.save(any()))
                .willReturn(TEST_SAVED_CATEGORY);

        //when
        categoryService.saveCategory(TEST_CATEGORY_REQUEST);

        //then
        verify(categoryRepository, atLeastOnce()).existsByBranchAndName(TEST_CATEGORY_REQUEST.getBranch(), TEST_CATEGORY_REQUEST.getName());
        verify(categoryRepository, atLeastOnce()).findByBranchAndName(TEST_CATEGORY_REQUEST.getBranch(), "ROOT");
        verify(categoryRepository, atLeastOnce()).existsByBranchAndName(TEST_CATEGORY_REQUEST.getBranch(), "ROOT");
        verify(categoryRepository, atLeastOnce()).save(any());
    }

    @Test
    public void 중소분류_카테고리_추가_테스트() throws Exception {
        //given
        CategoryRequest TEST_CATEGORY_REQUEST = new CategoryRequest();
        TEST_CATEGORY_REQUEST.setBranch("testBranch");
        TEST_CATEGORY_REQUEST.setName("testName");
        TEST_CATEGORY_REQUEST.setParentCategoryName("testParentCategoryName");
        TEST_CATEGORY_REQUEST.setCategoryCode(2);

        Category TEST_PARENT_CATEGORY = Category.builder()
                .depth(1)
                .build();
        given(categoryRepository.findByBranchAndName(TEST_CATEGORY_REQUEST.getBranch(), TEST_CATEGORY_REQUEST.getParentCategoryName()))
                .willReturn(Optional.of(TEST_PARENT_CATEGORY));

        Category TEST_SAVED_CATEGORY = Category.builder().build();
        ReflectionTestUtils.setField(TEST_SAVED_CATEGORY, "categoryId", 1l);

        given(categoryRepository.save(any()))
                .willReturn(TEST_SAVED_CATEGORY);

        //when
        categoryService.saveCategory(TEST_CATEGORY_REQUEST);

        //then
        verify(categoryRepository, atLeastOnce()).findByBranchAndName(TEST_CATEGORY_REQUEST.getBranch(), TEST_CATEGORY_REQUEST.getParentCategoryName());
        verify(categoryRepository, atLeastOnce()).save(any());
    }

    @Test
    public void 카테고리_수정_테스트() throws Exception {
        //given
        Category TEST_CATEGORY = Category.builder()
                .name("testName")
                .build();

        given(categoryRepository.findById(any()))
                .willReturn(Optional.of(TEST_CATEGORY));

        CategoryRequest TEST_CATEGORY_REQUEST = new CategoryRequest();
        TEST_CATEGORY_REQUEST.setName("testName2");

        //when
        categoryService.updateCategory(any(), TEST_CATEGORY_REQUEST);

        //then
        assertThat(TEST_CATEGORY.getName(), is(TEST_CATEGORY_REQUEST.getName()));
    }

    @Test
    public void 카테고리_완전삭제_테스트() throws Exception {
        //given
        Category TEST_CATEGORY = Category.builder()
                .name("testName")
                .build();

        given(categoryRepository.findById(any()))
                .willReturn(Optional.of(TEST_CATEGORY));

        //when
        categoryService.deleteCategory(any());

        //then
        verify(categoryRepository, atLeastOnce()).delete(any());
    }
}
