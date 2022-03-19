package com.allan.shoppingMall.domains.item.service;

import com.allan.shoppingMall.domains.category.service.CategoryService;
import com.allan.shoppingMall.domains.item.domain.clothes.Clothes;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesSize;
import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import com.allan.shoppingMall.domains.item.domain.item.Item;
import com.allan.shoppingMall.domains.item.domain.item.ItemImage;
import com.allan.shoppingMall.domains.item.domain.item.ItemRepository;
import com.allan.shoppingMall.domains.item.domain.item.ItemSummaryDTOForCart;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
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

    @Test
    public void 상품_요약정보_조회_테스트() throws Exception {
        //given
        Clothes TEST_CLOTHES = Clothes.builder()
                .price(1000l)
                .name("testClothesName")
                .build();

        ReflectionTestUtils.setField(TEST_CLOTHES, "itemId", 1l);

        TEST_CLOTHES.changeItemSizes(List.of(
                ClothesSize.builder()
                        .sizeLabel(SizeLabel.S)
                        .stockQuantity(10l)
                        .build(),
                ClothesSize.builder()
                        .sizeLabel(SizeLabel.M)
                        .stockQuantity(20l)
                        .build(),
                ClothesSize.builder()
                        .sizeLabel(SizeLabel.L)
                        .stockQuantity(30l)
                        .build()
        ));

        ItemImage TEST_ITEM_IMAGE_1 = createItemImage(1l);
        ItemImage TEST_ITEM_IMAGE_2 = createItemImage(2l);
        ItemImage TEST_ITEM_IMAGE_3 = createItemImage(3l);

        TEST_CLOTHES.changeItemImages(List.of(
                TEST_ITEM_IMAGE_1, TEST_ITEM_IMAGE_2, TEST_ITEM_IMAGE_3
        ));

        given(itemRepository.findById(any()))
                .willReturn(Optional.of(TEST_CLOTHES));

        //when
        ItemSummaryDTOForCart itemSummaryDTO = itemService.getItemSummaryDTO(any());

        //then
        verify(itemRepository, atLeastOnce()).findById(any());
        assertThat(itemSummaryDTO.getItemId(), is(TEST_CLOTHES.getItemId()));
        assertThat(itemSummaryDTO.getName(), is(TEST_CLOTHES.getName()));
        assertThat(itemSummaryDTO.getProfileImgId(), is(TEST_ITEM_IMAGE_1.getItemImageId()));
        assertThat(itemSummaryDTO.getPrice(), is(TEST_CLOTHES.getPrice()));
        assertThat(itemSummaryDTO.getSizes().size(), is(3));
        assertThat(itemSummaryDTO.getSizes().get(0), is(TEST_CLOTHES.getItemSizes().get(0).getSizeLabel()));
        assertThat(itemSummaryDTO.getSizes().get(1), is(TEST_CLOTHES.getItemSizes().get(1).getSizeLabel()));
        assertThat(itemSummaryDTO.getSizes().get(2), is(TEST_CLOTHES.getItemSizes().get(2).getSizeLabel()));
    }

    private ItemImage createItemImage(Long imageId){
        ItemImage itemImage = ItemImage.builder().build();
        ReflectionTestUtils.setField(itemImage, "itemImageId", imageId);
        return itemImage;
    }
}
