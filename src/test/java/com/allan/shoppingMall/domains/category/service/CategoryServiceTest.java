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

import java.util.List;
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

    /**
     * 조회한 카테고리와 연관 된 카테고리 id 리스트 반환 테스트 입니다.
     * Ex) top 카테고리 조회시, 연관 된 shirt, knit 카테고리 아이디 리스트 반환.
     */
    @Test
    public void 조회한_모든_카테고리_아이디_테스트() throws Exception {
        //given
        Category KNIT_CATEGORY = Category.builder()
                .branch("shop")
                .name("knit")
                .depth(2)
                .build();
        ReflectionTestUtils.setField(KNIT_CATEGORY, "categoryId", 5l);

        Category SHIRT_CATEGORY = Category.builder()
                .branch("shop")
                .name("shirt")
                .depth(2)
                .build();
        ReflectionTestUtils.setField(SHIRT_CATEGORY, "categoryId", 4l);

        Category BOTTOM_CATEGORY = Category.builder()
                .branch("shop")
                .name("bottom")
                .depth(1)
                .build();
        ReflectionTestUtils.setField(BOTTOM_CATEGORY, "categoryId", 3l);

        Category TOP_CATEGORY = Category.builder()
                .branch("shop")
                .name("top")
                .depth(1)
                .build();
        ReflectionTestUtils.setField(TOP_CATEGORY, "categoryId", 2l);
        ReflectionTestUtils.setField(TOP_CATEGORY, "childCategory", List.of(SHIRT_CATEGORY, KNIT_CATEGORY));

        Category SHOP_CATEGORY = Category.builder()
                .branch("shop")
                .name("shop")
                .depth(0)
                .build();
        ReflectionTestUtils.setField(SHOP_CATEGORY, "categoryId", 1l);
        ReflectionTestUtils.setField(SHOP_CATEGORY, "childCategory", List.of(TOP_CATEGORY, BOTTOM_CATEGORY));

        given(categoryRepository.findById(any()))
                .willReturn(Optional.of(TOP_CATEGORY));

        //when
        List<Long> categoryIdList = categoryService.getCategoryIds(any());

        //then
        assertThat(categoryIdList.size(), is(3));
        assertThat(categoryIdList.get(0), is(TOP_CATEGORY.getCategoryId()));
        assertThat(categoryIdList.get(1), is(SHIRT_CATEGORY.getCategoryId()));
        assertThat(categoryIdList.get(2), is(KNIT_CATEGORY.getCategoryId()));
    }
}
