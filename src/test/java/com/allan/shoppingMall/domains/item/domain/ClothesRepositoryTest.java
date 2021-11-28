package com.allan.shoppingMall.domains.item.domain;

import com.allan.shoppingMall.common.config.jpa.auditing.JpaAuditingConfig;
import com.allan.shoppingMall.domains.item.domain.clothes.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.dao.DataAccessException;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@DataJpaTest(
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JpaAuditingConfig.class
        )
)
@WithMockUser
public class ClothesRepositoryTest {

    @Autowired
    ClothesRepository clothesRepository;

    @Autowired
    TestEntityManager testEntityManager;


    @Test
    public void 의류엔티티_생성_테스트() throws Exception {
        //given

        Clothes TEST_CLOTHES = createClothes();

        //when
        clothesRepository.save(TEST_CLOTHES);
        Clothes findClothes = (Clothes) clothesRepository.findById(TEST_CLOTHES.getItemId()).orElseThrow(() -> new DataAccessException("Clothes Entity is not exist") {
        });

        //then
        assertThat(findClothes.getName(), is(TEST_CLOTHES.getName()));
        assertThat(findClothes.getPrice(), is(TEST_CLOTHES.getPrice()));
        assertThat(findClothes.getStockQuantity(), is(TEST_CLOTHES.getStockQuantity()));
        assertThat(findClothes.getEngName(), is(TEST_CLOTHES.getEngName()));

    }

    @Test
    public void 의류_리스트_반환_테스트() throws Exception {
        //given
        Clothes TEST_CLOTHES1 = createClothes();
        TEST_CLOTHES1.changeClothesFabrics(createClothesFabrics("testMaterial1", "testMaterial1"));
        TEST_CLOTHES1.changeClothesDetails(createClothesDetails("testDetailDesc1"));
        TEST_CLOTHES1.changeClothesSizes(createClothesSizes(SizeLabel.S, 10.0, 20.0, 10.0, 30.0, 40.0, 20.0, 10.0));
        TEST_CLOTHES1.changeModelSizes(createModelSizes(10.0, 20.0, 30.0, 40.0, 21.0));
        TEST_CLOTHES1.changeItemImages(createItemImages("testOriginalItemImageName1", "testItemImagePath1", ImageType.PREVIEW, 10l));

        Clothes TEST_CLOTHES2 = createClothes();
        TEST_CLOTHES2.changeClothesFabrics(createClothesFabrics("testMaterial2", "testMaterial2"));
        TEST_CLOTHES2.changeClothesDetails(createClothesDetails("testDetailDesc2"));
        TEST_CLOTHES2.changeClothesSizes(createClothesSizes(SizeLabel.S, 10.0, 20.0, 10.0, 30.0, 40.0, 20.0, 10.0));
        TEST_CLOTHES2.changeModelSizes(createModelSizes(10.0, 20.0, 30.0, 40.0, 21.0));
        TEST_CLOTHES2.changeItemImages(createItemImages("testOriginalItemImageName2", "testItemImagePath2", ImageType.PREVIEW, 10l));
        TEST_CLOTHES2.changeItemImages(createItemImages("testOriginalItemImageName3", "testItemImagePath3", ImageType.PRODUCT, 10l));

        testEntityManager.persist(TEST_CLOTHES1);
        testEntityManager.persist(TEST_CLOTHES2);

        //when
        List<Clothes> clothesList = clothesRepository.getClothesList();

        //then
        assertThat(clothesList.size(), is(2)); // Clothes 엔티티 갯수 확인.
        assertThat(clothesList.get(0).getItemImages().size(), is(2)); // 리스트의 첫번째 Clothes의 Image 갯수 확인.
        assertThat(clothesList.get(0).getItemImages().get(0).getOriginalItemImageName(), is(TEST_CLOTHES1.getItemImages().get(0).getOriginalItemImageName()));
        assertThat(clothesList.get(0).getItemImages().get(1).getOriginalItemImageName(), is(TEST_CLOTHES1.getItemImages().get(1).getOriginalItemImageName()));

        assertThat(clothesList.get(1).getItemImages().size(), is(4)); // 리스트의 첫번째 Clothes의 Image 갯수 확인.
        assertThat(clothesList.get(1).getItemImages().get(0).getOriginalItemImageName(), is(TEST_CLOTHES2.getItemImages().get(0).getOriginalItemImageName()));
        assertThat(clothesList.get(1).getItemImages().get(1).getOriginalItemImageName(), is(TEST_CLOTHES2.getItemImages().get(1).getOriginalItemImageName()));


    }

    @Test
    public void 의상_엔티티_단일_조회_테스트() throws Exception {
        //given
        Clothes TEST_CLOTHES = createClothes();
        TEST_CLOTHES.changeClothesFabrics(createClothesFabrics("materialPart", "materialDesc"));
        TEST_CLOTHES.changeClothesDetails(createClothesDetails("detailDesc"));
        TEST_CLOTHES.changeClothesDetails(createClothesDetails("detailDesc2"));
        TEST_CLOTHES.changeClothesSizes(createClothesSizes(SizeLabel.S, 30.0,40.0, 50.0, 60.0, 70.0, 80.0, 90.0));
        TEST_CLOTHES.changeModelSizes(createModelSizes(100.0, 110.0, 120.0, 130.0, 140.0));
        TEST_CLOTHES.changeItemImages(createItemImages("imageName", "imagePath", ImageType.PREVIEW, 10l));
        TEST_CLOTHES.changeItemImages(createItemImages("imageName2", "imagePath2", ImageType.PRODUCT, 20l));

        testEntityManager.persist(TEST_CLOTHES);

        //when
        Clothes findClothes = clothesRepository.getClothes(TEST_CLOTHES.getItemId()).get();

        //then
        assertThat(findClothes.getName(), is(TEST_CLOTHES.getName()));
        assertThat(findClothes.getPrice(), is(TEST_CLOTHES.getPrice()));
        assertThat(findClothes.getClothesFabrics().size(), is(2));
        assertThat(findClothes.getClothesDetails().size(), is(4));
        assertThat(findClothes.getClothesSizes().size(), is(2));
        assertThat(findClothes.getModelSizes().size(), is(1));
        assertThat(findClothes.getItemImages().size(), is(4));
        assertThat(findClothes.getClothesDetails().get(2).getDetailDesc(), is(TEST_CLOTHES.getClothesDetails().get(2).getDetailDesc()));
        assertThat(findClothes.getClothesFabrics().get(1).getMaterialPart(), is(TEST_CLOTHES.getClothesFabrics().get(1).getMaterialPart()));
        assertThat(findClothes.getModelSizes().get(0).getModelHeap(), is(TEST_CLOTHES.getModelSizes().get(0).getModelHeap()));
        assertThat(findClothes.getItemImages().get(3).getImageType().getWhereToUse(), is(TEST_CLOTHES.getItemImages().get(3).getImageType().getWhereToUse()));
    }

    private Clothes createClothes() {
        Clothes clothes = Clothes.builder()
                .name("testName")
                .price(1000l)
                .stockQuantity(200l)
                .engName("testEngName")
                .build();
        return clothes;
    }

    private List<ClothesFabric> createClothesFabrics(String materialPart, String materialDesc){
        return List.of(
                ClothesFabric.builder()
                        .materialPart(materialPart)
                        .materialDesc(materialDesc)
                        .build(),
                ClothesFabric.builder()
                        .materialPart(materialPart)
                        .materialDesc(materialDesc)
                        .build()
        );
    }

    private List<ClothesDetail> createClothesDetails(String detailDesc){
        return List.of(
                ClothesDetail.builder()
                        .detailDesc(detailDesc)
                        .build(),
                ClothesDetail.builder()
                        .detailDesc(detailDesc)
                        .build()
        );
    }

    private List<ClothesSize> createClothesSizes(SizeLabel sizeLabel, Double backLength, Double bottomWidth, Double chestWidth,
                                                 Double heapWidth, Double shoulderWidth, Double waistWidth, Double sleeveLength){
        return List.of(
                ClothesSize.builder()
                        .sizeLabel(sizeLabel)
                        .backLength(backLength)
                        .bottomWidth(bottomWidth)
                        .chestWidth(chestWidth)
                        .heapWidth(heapWidth)
                        .shoulderWidth(shoulderWidth)
                        .waistWidth(waistWidth)
                        .sleeveLength(sleeveLength)
                        .build(),
                ClothesSize.builder()
                        .sizeLabel(sizeLabel)
                        .backLength(backLength)
                        .bottomWidth(bottomWidth)
                        .chestWidth(chestWidth)
                        .heapWidth(heapWidth)
                        .shoulderWidth(shoulderWidth)
                        .waistWidth(waistWidth)
                        .sleeveLength(sleeveLength)
                        .build()

        );
    }

    private List<ModelSize> createModelSizes(Double modelShoulderSize, Double modelHeap, Double modelWaist, Double modelHeight, Double modelWeight){
        return List.of(
                ModelSize.builder()
                        .modelShoulderSize(modelShoulderSize)
                        .modelHeap(modelHeap)
                        .modelWaist(modelWaist)
                        .modelHeight(modelHeight)
                        .modelWeight(modelWeight)
                        .build()
        );
    }

    private List<ItemImage> createItemImages(String originalItemImageName, String itemImagePath, ImageType imageType, Long imageSize){
        return List.of(
                ItemImage.builder()
                        .originalItemImageName(originalItemImageName)
                        .imageSize(imageSize)
                        .itemImagePath(itemImagePath)
                        .imageType(imageType)
                        .build(),

                ItemImage.builder()
                        .originalItemImageName(originalItemImageName)
                        .imageSize(imageSize)
                        .itemImagePath(itemImagePath)
                        .imageType(imageType)
                        .build()
        );
    }
}
