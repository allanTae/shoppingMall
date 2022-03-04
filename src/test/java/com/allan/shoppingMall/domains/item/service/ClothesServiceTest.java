package com.allan.shoppingMall.domains.item.service;

import com.allan.shoppingMall.domains.category.domain.Category;
import com.allan.shoppingMall.domains.category.domain.CategoryCode;
import com.allan.shoppingMall.domains.category.domain.CategoryItem;
import com.allan.shoppingMall.domains.category.domain.CategoryRepository;
import com.allan.shoppingMall.domains.item.domain.clothes.*;
import com.allan.shoppingMall.domains.item.domain.item.*;
import com.allan.shoppingMall.domains.item.domain.model.ClothesSummaryDTOForIndex;
import com.allan.shoppingMall.domains.item.domain.model.*;
import com.allan.shoppingMall.domains.item.infra.ImageFileHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Rollback(value = true)
public class ClothesServiceTest {

    @Mock
    ClothesRepository clothesRepository;

    @Mock
    ImageFileHandler imageFileHandler;

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    ClothesService clothesService;

    @Test
    public void 의류_상품_등록_테스트() throws Exception {
        //given
        ClothesForm TEST_CLOTHES_REQUEST = createClothesRequest();
        List<ItemImage> TEST_ITEM_IMAGE = createItemImage();
        given(imageFileHandler.parseImageInfo(any(), any()))
                .willReturn(TEST_ITEM_IMAGE);

        Category TEST_CATEGORY = Category.builder()
                .categoryCode(CategoryCode.CLOTHES)
                .build();
        given(categoryRepository.findById(any()))
                .willReturn(Optional.of(TEST_CATEGORY));

        //when
        Long clothesId = clothesService.saveClothes(TEST_CLOTHES_REQUEST);

        //then
        verify(clothesRepository, atLeastOnce()).save(any());
        verify(imageFileHandler, atLeastOnce()).parseImageInfo(any(), any());
    }

    @Test
    public void 의류_상품_리스트_테스트() throws Exception {
        //given
        List<Clothes> TEST_CLOTHES_LIST = createClothesList();
        Page<Clothes> TEST_CLOTHES = createClothesByPaging(TEST_CLOTHES_LIST);
        given(clothesRepository.findAll(any(Pageable.class))).willReturn(TEST_CLOTHES);


        //when
        List<ClothesSummaryDTOForIndex> clothes = clothesService.getClothesSummary(PageRequest.of(0, 9, Sort.by(Sort.Direction.DESC, "createdDate")));

        //then
        verify(clothesRepository, atLeastOnce()).findAll(any(Pageable.class));
        assertThat(clothes.get(0).getClothesId(), is(TEST_CLOTHES_LIST.get(0).getItemId()));
        assertThat(clothes.get(0).getClothesName(), is(TEST_CLOTHES_LIST.get(0).getName()));
        assertThat(clothes.get(0).getPrice(), is(TEST_CLOTHES_LIST.get(0).getPrice()));
        assertThat(clothes.get(0).getProfileImageIds().size(), is(TEST_CLOTHES_LIST.get(0).getItemImages().size() - 1));
        assertThat(clothes.get(0).getProfileImageIds().get(0), is(TEST_CLOTHES_LIST.get(0).getItemImages().get(0).getItemImageId()));
        assertThat(clothes.get(0).getProfileImageIds().get(1), is(TEST_CLOTHES_LIST.get(0).getItemImages().get(2).getItemImageId()));
    }

    private Page<Clothes> createClothesByPaging(List<Clothes> clothesList) {

        PageImpl<Clothes> page = new PageImpl<>(clothesList);

        return page;
    }

    @Test
    public void 의류_상품_조회() throws Exception {
        //given
        Clothes TEST_CLOTHES = Clothes.builder()
                .name("testName")
                .engName("testEngName")
                .price(1000l)
                .color(Color.RED)
                .build();

        Category TEST_CATEGORY = Category.builder().build();
        ReflectionTestUtils.setField(TEST_CATEGORY, "categoryId", 1l);

        TEST_CLOTHES.changeItemFabrics(List.of(ItemFabric.builder().materialPart("testMaterial").build()));
        TEST_CLOTHES.changeItemDetails(List.of(ItemDetail.builder().detailDesc("testDetail").build()));
        TEST_CLOTHES.changeClothesSize(List.of(ClothesSize.builder().shoulderWidth(10.5).sizeLabel(SizeLabel.S).stockQuantity(3l).build()));
        TEST_CLOTHES.changeModelSizes(List.of(ModelSize.builder().modelWeight(10.5).build()));
        TEST_CLOTHES.changeItemImages(List.of(ItemImage.builder().imageType(ImageType.PREVIEW).build()));
        TEST_CLOTHES.changeCategoryItems(List.of(new CategoryItem(TEST_CATEGORY)));

        given(clothesRepository.getClothes(any()))
                .willReturn(Optional.of(TEST_CLOTHES));

        //when
        ClothesDTO clothes = clothesService.getClothes(any());

        //then
        verify(clothesRepository, atLeastOnce()).getClothes(any());
        assertThat(clothes.getClothesName(), is(TEST_CLOTHES.getName()));
        assertThat(clothes.getPrice(), is(TEST_CLOTHES.getPrice()));
        assertThat(clothes.getEngName(), is(TEST_CLOTHES.getEngName()));
        assertThat(clothes.getItemFabrics().size(), is(1));
        assertThat(clothes.getItemDetails().size(), is(1));
        assertThat(clothes.getClothesSizes().size(), is(1));
        assertThat(clothes.getModelSizes().size(), is(1));
        assertThat(clothes.getPreviewImages().size(), is(1));
        assertThat(clothes.getDetailImages().size(), is(0));
    }

    private List<Clothes> createClothesList() {
        Clothes clothes = Clothes.builder()
                .name("testClothesName")
                .price(100l)
                .color(Color.RED)
                .build();

        clothes.changeItemImages(List.of(ItemImage.builder()
                        .imageType(ImageType.PREVIEW)
                        .itemImagePath("testImagePath")
                        .build(),
                ItemImage.builder()
                        .imageType(ImageType.PRODUCT)
                        .itemImagePath("testImagePath2")
                        .build(),
                ItemImage.builder()
                        .imageType(ImageType.PREVIEW)
                        .itemImagePath("testImagePath3")
                        .build())
        );

        ReflectionTestUtils.setField(clothes, "itemId", 1l);

        Category TEST_CATEGORY = Category.builder().build();
        ReflectionTestUtils.setField(TEST_CATEGORY, "categoryId", 1l);

        clothes.changeCategoryItems(List.of(new CategoryItem(TEST_CATEGORY)));

        return List.of(
                clothes
        );
    }

    private List<ItemImage> createItemImage() {
        List<ItemImage> itemImages = List.of();
        return itemImages;
    }

    private ClothesForm createClothesRequest() {
        ClothesForm clothesRequest = new ClothesForm();
        clothesRequest.setName("testName");
        clothesRequest.setEngName("testEngName");
        clothesRequest.setPrice(10000l);
        clothesRequest.setItemFabrics(List.of(
                new ItemFabricDTO("clothesMaterialPart", "clothesMaterialDesc")
        ));
        clothesRequest.setItemDetails(List.of(
                new ItemDetailDTO("clothesDetailDesc")
        ));
        clothesRequest.setClothesSizes(List.of(
                new ClothesSizeDTO("1", 20.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80.0, 10l),
                new ClothesSizeDTO("1", 20.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80.0, 20l)
        ));
        clothesRequest.setModelSizes(List.of(
                new ModelSizeDTO(10.0, 20.0, 30.0, 40.0, 50.0)
        ));
        clothesRequest.setClothesColor(1);

        clothesRequest.setCategoryId(2l);

        return clothesRequest;
    }

    @Test
    public void 의류_상품_요약정보_조회_테스트() throws Exception {
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

        given(clothesRepository.findById(any()))
                .willReturn(Optional.of(TEST_CLOTHES));

        //when
        ClothesSummeryDTO clothesSummaryDTO = clothesService.getClothesSummaryDTO(any());

        //then
        verify(clothesRepository, atLeastOnce()).findById(any());
        assertThat(clothesSummaryDTO.getClothesId(), is(TEST_CLOTHES.getItemId()));
        assertThat(clothesSummaryDTO.getClothesName(), is(TEST_CLOTHES.getName()));
        assertThat(clothesSummaryDTO.getProfileImgId(), is(TEST_ITEM_IMAGE_1.getItemImageId()));
        assertThat(clothesSummaryDTO.getClothesPrice(), is(TEST_CLOTHES.getPrice()));
        assertThat(clothesSummaryDTO.getSizes().size(), is(3));
        assertThat(clothesSummaryDTO.getSizes().get(0), is(TEST_CLOTHES.getItemSizes().get(0).getSizeLabel()));
        assertThat(clothesSummaryDTO.getSizes().get(1), is(TEST_CLOTHES.getItemSizes().get(1).getSizeLabel()));
        assertThat(clothesSummaryDTO.getSizes().get(2), is(TEST_CLOTHES.getItemSizes().get(2).getSizeLabel()));
    }

    private ItemImage createItemImage(Long imageId){
        ItemImage itemImage = ItemImage.builder().build();
        ReflectionTestUtils.setField(itemImage, "itemImageId", imageId);
        return itemImage;
    }
}
