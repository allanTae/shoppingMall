package com.allan.shoppingMall.domains.item.service;

import com.allan.shoppingMall.domains.item.domain.*;
import com.allan.shoppingMall.domains.item.domain.clothes.*;
import com.allan.shoppingMall.domains.item.domain.model.ClothesSummaryDTO;
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
    ItemImageRepository itemImageRepository;

    @InjectMocks
    ClothesService clothesService;

    @Test
    public void 의류_상품_등록_테스트() throws Exception {
        //given
        ClothesForm TEST_CLOTHES_REQUEST = createClothesRequest();
        List<ItemImage> TEST_ITEM_IMAGE = createItemImage();
        given(imageFileHandler.parseImageInfo(any(), any()))
                .willReturn(TEST_ITEM_IMAGE);

        //when
        clothesService.saveClothes(TEST_CLOTHES_REQUEST);

        //then
        verify(clothesRepository, atLeastOnce()).save(any());
        verify(imageFileHandler, atLeastOnce()).parseImageInfo(any(), any());
    }

    @Test
    public void 의류_상품_리스트_테스트() throws Exception {
        //given
        List<Clothes> TEST_CLOTHES_LIST = createClothesList();
        given(clothesRepository.getClothesList())
                .willReturn(TEST_CLOTHES_LIST);

        //when
        List<ClothesSummaryDTO> clothes = clothesService.getClothesSummary();

        //then
        verify(clothesRepository, atLeastOnce()).getClothesList();
        assertThat(clothes.get(0).getClothesId(), is(TEST_CLOTHES_LIST.get(0).getItemId()));
        assertThat(clothes.get(0).getClothesName(), is(TEST_CLOTHES_LIST.get(0).getName()));
        assertThat(clothes.get(0).getPrice(), is(TEST_CLOTHES_LIST.get(0).getPrice()));
        assertThat(clothes.get(0).getProfileImageIds().size(), is(TEST_CLOTHES_LIST.get(0).getItemImages().size() - 1));
        assertThat(clothes.get(0).getProfileImageIds().get(0), is(TEST_CLOTHES_LIST.get(0).getItemImages().get(0).getItemImageId()));
        assertThat(clothes.get(0).getProfileImageIds().get(1), is(TEST_CLOTHES_LIST.get(0).getItemImages().get(2).getItemImageId()));
    }

    @Test
    public void 의류_상품_조회() throws Exception {
        //given
        Clothes TEST_CLOTHES = Clothes.builder()
                .name("testName")
                .engName("testEngName")
                .price(1000l)
                .stockQuantity(1100l)
                .build();

        TEST_CLOTHES.changeClothesFabrics(List.of(ClothesFabric.builder().materialPart("testMaterial").build()));
        TEST_CLOTHES.changeClothesDetails(List.of(ClothesDetail.builder().detailDesc("testDetail").build()));
        TEST_CLOTHES.changeClothesSizes(List.of(ClothesSize.builder().shoulderWidth(10.5).sizeLabel(SizeLabel.S).build()));
        TEST_CLOTHES.changeModelSizes(List.of(ModelSize.builder().modelWeight(10.5).build()));
        TEST_CLOTHES.changeItemImages(List.of(ItemImage.builder().imageType(ImageType.PREVIEW).build()));
        TEST_CLOTHES.changeItemColors(List.of(ItemColor.builder().color("빨강").build()));

        given(clothesRepository.findById(any()))
                .willReturn(Optional.of(TEST_CLOTHES));

        //when
        ClothesDTO clothes = clothesService.getClothes(any());

        //then
        verify(clothesRepository, atLeastOnce()).findById(any());
        assertThat(clothes.getClothesName(), is(TEST_CLOTHES.getName()));
        assertThat(clothes.getPrice(), is(TEST_CLOTHES.getPrice()));
        assertThat(clothes.getEngName(), is(TEST_CLOTHES.getEngName()));
        assertThat(clothes.getClothesFabrics().size(), is(1));
        assertThat(clothes.getClothesDetails().size(), is(1));
        assertThat(clothes.getClothesSizes().size(), is(1));
        assertThat(clothes.getModelSizes().size(), is(1));
        assertThat(clothes.getPreviewImages().size(), is(1));
        assertThat(clothes.getDetailImages().size(), is(0));
        assertThat(clothes.getColors().size(), is(1));
    }

    private List<Clothes> createClothesList() {
        Clothes clothes = Clothes.builder()
                .name("testClothesName")
                .price(100l)
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
        clothesRequest.setStockQuantity(100l);
        clothesRequest.setClothesFabrics(List.of(
                new ClothesFabricDTO("clothesMaterialPart", "clothesMaterialDesc")
        ));
        clothesRequest.setClothesDetails(List.of(
                new ClothesDetailDTO("clothesDetailDesc")
        ));
        clothesRequest.setClothesSizes(List.of(
                new ClothesSizeDTO("1", 20.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80.0)
        ));
        clothesRequest.setModelSizes(List.of(
                new ModelSizeDTO(10.0, 20.0, 30.0, 40.0, 50.0)
        ));
        return clothesRequest;
    }
}
