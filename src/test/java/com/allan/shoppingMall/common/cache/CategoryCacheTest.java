package com.allan.shoppingMall.common.cache;

import com.allan.shoppingMall.common.config.cache.CacheConfig;
import com.allan.shoppingMall.domains.category.domain.Category;
import com.allan.shoppingMall.domains.category.domain.CategoryCode;
import com.allan.shoppingMall.domains.category.domain.CategoryRepository;
import com.allan.shoppingMall.domains.category.domain.model.CategoryDTO;
import com.allan.shoppingMall.domains.category.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.AopTestUtils;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static java.util.Optional.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ContextConfiguration
@ExtendWith(SpringExtension.class)
public class CategoryCacheTest {

    private static final CategoryDTO TEST_SHOP_CATEGORY_DTO = new CategoryDTO(Category.builder()
            .name("shop")
            .depth(1)
            .branch("shop")
            .categoryCode(CategoryCode.SHOP)
            .build());

    private CategoryService mock;

    @Autowired
    private CategoryService categoryService;

    @EnableCaching
    @Configuration
    public static class CachingTestConfig {
        @Bean
        public CategoryService categoryServiceMockImplementation() {
            return mock(CategoryService.class);
        }
        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager("shopCategoryCaching");
        }
    }

    @BeforeEach
    void setUp() {
        // given
        mock = AopTestUtils.getTargetObject(categoryService);

        reset(mock);

        when(mock.getShopCategoryByBranch())
                .thenReturn(TEST_SHOP_CATEGORY_DTO)
                .thenThrow(new RuntimeException("Book should be cached!"));
    }

    @Test
    public void 상품_카테고리_캐싱테스트() throws Exception {
        //when, then
        assertThat(TEST_SHOP_CATEGORY_DTO, is(categoryService.getShopCategoryByBranch()));
        verify(mock, atLeastOnce()).getShopCategoryByBranch();

        assertThat(TEST_SHOP_CATEGORY_DTO, is(categoryService.getShopCategoryByBranch()));
        assertThat(TEST_SHOP_CATEGORY_DTO, is(categoryService.getShopCategoryByBranch()));
        verifyNoMoreInteractions(mock);
    }
}
