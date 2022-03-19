package com.allan.shoppingMall.domains.item.service;

import com.allan.shoppingMall.domains.category.domain.*;
import com.allan.shoppingMall.domains.item.domain.accessory.Accessory;
import com.allan.shoppingMall.domains.item.domain.accessory.AccessoryRepository;
import com.allan.shoppingMall.domains.item.domain.accessory.AccessorySize;
import com.allan.shoppingMall.domains.item.domain.clothes.Clothes;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesSize;
import com.allan.shoppingMall.domains.item.domain.clothes.ModelSize;
import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import com.allan.shoppingMall.domains.item.domain.item.*;
import com.allan.shoppingMall.domains.item.domain.model.*;
import com.allan.shoppingMall.domains.item.infra.ImageFileHandler;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Rollback(value = true)
public class AccessoryServiceTest {

    @Mock
    AccessoryRepository accessoryRepository;

    @Mock
    ImageFileHandler imageFileHandler;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    CategoryItemRepository categoryItemRepository;

    @InjectMocks
    AccessoryService accessoryService;


    @Test
    public void 악세서리_상품_등록_테스트() throws Exception {
        //given
        AccessoryForm TEST_ACCESSORY_FORM = createAccessoryForm();
        List<ItemImage> TEST_ITEM_IMAGE = createItemImage();
        given(imageFileHandler.parseImageInfo(any(), any()))
                .willReturn(TEST_ITEM_IMAGE);

        Category TEST_CATEGORY = Category.builder()
                .categoryCode(CategoryCode.ACCESSORY)
                .build();
        given(categoryRepository.findById(any()))
                .willReturn(Optional.of(TEST_CATEGORY));

        //when
        Long clothesId = accessoryService.saveAccessory(TEST_ACCESSORY_FORM);

        //then
        verify(categoryRepository, atLeastOnce()).findById(any());
        verify(accessoryRepository, atLeastOnce()).save(any());
        verify(imageFileHandler, atLeastOnce()).parseImageInfo(any(), any());
    }

    @Test
    public void 악세서리_상품_조회테스트() throws Exception {
        //given
        Accessory TEST_ACCESSORY = Accessory.builder()
                .name("testName")
                .engName("testEngName")
                .price(1000l)
                .color(Color.RED)
                .build();
        ReflectionTestUtils.setField(TEST_ACCESSORY, "itemId", 2l);

        Category TEST_CATEGORY = Category.builder().build();
        ReflectionTestUtils.setField(TEST_CATEGORY, "categoryId", 1l);

        TEST_ACCESSORY.changeItemFabrics(List.of(ItemFabric.builder().materialPart("testMaterial").build()));
        TEST_ACCESSORY.changeItemDetails(List.of(ItemDetail.builder().detailDesc("testDetail").build()));
        TEST_ACCESSORY.changeAccessorySize(List.of(AccessorySize.builder().widthLength(10.5).sizeLabel(SizeLabel.S).stockQuantity(3l).build()));
        TEST_ACCESSORY.changeItemImages(List.of(ItemImage.builder().imageType(ImageType.PREVIEW).build()));
        TEST_ACCESSORY.changeCategoryItems(List.of(new CategoryItem(TEST_CATEGORY)));

        given(accessoryRepository.findById(any()))
                .willReturn(Optional.of(TEST_ACCESSORY));

        CategoryItem TEST_CATEGORY_ITEM = new CategoryItem(Category.builder().build());
        given(categoryItemRepository.getCategoryItem(List.of(CategoryCode.ACCESSORY), TEST_ACCESSORY.getItemId()))
                .willReturn(Optional.of(TEST_CATEGORY_ITEM));

        //when
        AccessoryDTO accessory = accessoryService.getAccessory(any());

        //then
        verify(accessoryRepository, atLeastOnce()).findById(any());
        assertThat(accessory.getAccessoryName(), is(TEST_ACCESSORY.getName()));
        assertThat(accessory.getPrice(), is(TEST_ACCESSORY.getPrice()));
        assertThat(accessory.getEngName(), is(TEST_ACCESSORY.getEngName()));
        assertThat(accessory.getItemFabrics().size(), is(1));
        assertThat(accessory.getItemDetails().size(), is(1));
        assertThat(accessory.getAccessorySizes().size(), is(1));
        assertThat(accessory.getPreviewImages().size(), is(1));
        assertThat(accessory.getDetailImages().size(), is(0));
    }

    @Test
    public void 악세서리_상품_수정테스트() throws Exception {
        Accessory TEST_ACCESSORY = Accessory.builder()
                .name("testName")
                .engName("testEngName")
                .price(1000l)
                .color(Color.RED)
                .build();

        Category TEST_CATEGORY = Category.builder()
                .categoryCode(CategoryCode.ACCESSORY)
                .build();
        ReflectionTestUtils.setField(TEST_CATEGORY, "categoryId", 1l);

        TEST_ACCESSORY.changeItemFabrics(List.of(ItemFabric.builder().materialPart("testMaterial").build()));
        TEST_ACCESSORY.changeItemDetails(List.of(ItemDetail.builder().detailDesc("testDetail").build()));
        TEST_ACCESSORY.changeAccessorySize(List.of(AccessorySize.builder().widthLength(10.5).sizeLabel(SizeLabel.S).stockQuantity(3l).build()));
        TEST_ACCESSORY.changeItemImages(List.of(ItemImage.builder().imageType(ImageType.PREVIEW).build()));
        TEST_ACCESSORY.changeCategoryItems(List.of(new CategoryItem(TEST_CATEGORY)));

        assertThat(TEST_ACCESSORY.getItemFabrics().size(), is(1));
        assertThat(TEST_ACCESSORY.getItemDetails().size(), is(1));
        assertThat(TEST_ACCESSORY.getItemSizes().size(), is(1));
        assertThat(TEST_ACCESSORY.getItemImages().size(), is(1));
        assertThat(TEST_ACCESSORY.getCategoryItems().size(), is(1));

        given(accessoryRepository.findById(any()))
                .willReturn(Optional.of(TEST_ACCESSORY));

        AccessoryForm TEST_ACCESSORY_FORM = createAccessoryForm();

        given(categoryRepository.findById(any()))
                .willReturn(Optional.of(TEST_CATEGORY));

        //when
        accessoryService.updateAccessory(TEST_ACCESSORY_FORM);

        //then
        verify(accessoryRepository, atLeastOnce()).findById(any());
        verify(categoryRepository, atLeastOnce()).findById(any());
        assertThat(TEST_ACCESSORY.getItemFabrics().size(), is(1));
        assertThat(TEST_ACCESSORY.getItemDetails().size(), is(2));
        assertThat(TEST_ACCESSORY.getItemDetails().get(0).getDetailDesc(), is("clothesDetailDesc1"));
        assertThat(TEST_ACCESSORY.getItemDetails().get(1).getDetailDesc(), is("clothesDetailDesc2"));
        assertThat(TEST_ACCESSORY.getItemSizes().size(), is(2));
        assertThat( ((AccessorySize)TEST_ACCESSORY.getItemSizes().get(0)).getStockQuantity(), is(10l));
        assertThat( ((AccessorySize)TEST_ACCESSORY.getItemSizes().get(0)).getWidthLength(), is(10.0));
        assertThat( ((AccessorySize)TEST_ACCESSORY.getItemSizes().get(0)).getHeightLength(), is(20.0));
        assertThat( ((AccessorySize)TEST_ACCESSORY.getItemSizes().get(0)).getSizeLabel().getDesc(), is(SizeLabel.S.getDesc()));


        assertThat( ((AccessorySize)TEST_ACCESSORY.getItemSizes().get(1)).getStockQuantity(), is(20l));
        assertThat( ((AccessorySize)TEST_ACCESSORY.getItemSizes().get(1)).getWidthLength(), is(20.0));
        assertThat( ((AccessorySize) TEST_ACCESSORY.getItemSizes().get(1)).getHeightLength(), is(30.0));
        assertThat( ((AccessorySize) TEST_ACCESSORY.getItemSizes().get(1)).getSizeLabel().getDesc(), is(SizeLabel.FREE.getDesc()));

        assertThat(TEST_ACCESSORY.getItemImages().size(), is(1));
        assertThat(TEST_ACCESSORY.getCategoryItems().size(), is(1));
    }

    private List<ItemImage> createItemImage() {
        List<ItemImage> itemImages = List.of();
        return itemImages;
    }

    private AccessoryForm createAccessoryForm() {
        AccessoryForm accessoryForm = new AccessoryForm();
        accessoryForm.setName("testName");
        accessoryForm.setEngName("testEngName");
        accessoryForm.setPrice(10000l);
        accessoryForm.setItemFabrics(List.of(
                new ItemFabricDTO("clothesMaterialPart", "clothesMaterialDesc")
        ));
        accessoryForm.setItemDetails(List.of(
                new ItemDetailDTO("clothesDetailDesc1"),
                new ItemDetailDTO("clothesDetailDesc2")
        ));
        accessoryForm.setAccessorySizes(List.of(
                new AccessorySizeDTO(Long.toString(SizeLabel.FREE.getId()), 20l, 20.0, 30.0, SizeLabel.FREE),
                new AccessorySizeDTO(Long.toString(SizeLabel.S.getId()), 10l, 10.0, 20.0, SizeLabel.S)
        ));
        accessoryForm.setAccessoryColor(1);

        accessoryForm.setCategoryId(2l);

        return accessoryForm;
    }
}
