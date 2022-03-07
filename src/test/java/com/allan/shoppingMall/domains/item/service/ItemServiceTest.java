package com.allan.shoppingMall.domains.item.service;

import com.allan.shoppingMall.domains.category.domain.CategoryRepository;
import com.allan.shoppingMall.domains.category.service.CategoryService;
import com.allan.shoppingMall.domains.item.domain.item.Item;
import com.allan.shoppingMall.domains.item.domain.item.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Rollback(value = true)
public class ItemServiceTest {

    @Mock
    CategoryService categoryService;

    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    ItemService itemService;

    /**
     * 카테리에 해당하는 상품 도메인 리스트를 페이징 테스트 입니다.
     */
    @Test
    public void 상품_페이징_테스트() throws Exception {
        //given
        List<Long> TEST_CATEGORY_ID_LIST = new ArrayList<>();
        given(categoryService.getCategoryIds(any()))
                .willReturn(TEST_CATEGORY_ID_LIST);

        Page<Item> TEST_PAGE = mock(Page.class);
        given(itemRepository.getItemsByCategoryIds(any(), any()))
                .willReturn(TEST_PAGE);

        //when
        itemService.getItems(any(), PageRequest.of(1, 1));

        //then
        verify(categoryService, atLeastOnce()).getCategoryIds(any());
        verify(itemRepository, atLeastOnce()).getItemsByCategoryIds(any(), any());
    }
}
