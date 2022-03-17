package com.allan.shoppingMall.domains.item.domain;

import com.allan.shoppingMall.common.config.jpa.auditing.JpaAuditingConfig;
import com.allan.shoppingMall.domains.item.domain.clothes.Clothes;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesSize;
import com.allan.shoppingMall.domains.item.domain.item.ItemSize;
import com.allan.shoppingMall.domains.item.domain.item.ItemSizeRepository;
import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
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
public class ClothesSizeRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    ItemSizeRepository clothesSizeRepository;

    @Test
    public void 의상아이디_사이즈로_사이즈정보_조회_테스트() throws Exception {
        //given
        List<Clothes> TEST_CLORTHES_LIST = createClothes();

        for(Clothes clothes : TEST_CLORTHES_LIST){
            testEntityManager.persist(clothes);
        }

        testEntityManager.flush();

        //when
        ItemSize clothesSizebySize1 = clothesSizeRepository.getItemSizebySizelabel(TEST_CLORTHES_LIST.get(0), SizeLabel.M).get();
        ItemSize clothesSizebySize2 = clothesSizeRepository.getItemSizebySizelabel(TEST_CLORTHES_LIST.get(1), SizeLabel.L).get();

        //then
        assertThat(clothesSizebySize1.getSizeLabel(), is(SizeLabel.M));
        assertThat(clothesSizebySize1.getItem().getItemId(), is(TEST_CLORTHES_LIST.get(0).getItemId()));
        assertThat(clothesSizebySize2.getSizeLabel(), is(SizeLabel.L));
        assertThat(clothesSizebySize2.getItem().getItemId(), is(TEST_CLORTHES_LIST.get(1).getItemId()));
    }

    private List<Clothes> createClothes() {
        Clothes clothes = Clothes.builder()
                .name("testName")
                .engName("testEngName")
                .price(1000l)
                .build();

        ClothesSize clothesSize1 = ClothesSize.builder()
                .stockQuantity(10l)
                .sizeLabel(SizeLabel.S)
                .build();

        ClothesSize clothesSize2 = ClothesSize.builder()
                .stockQuantity(12l)
                .sizeLabel(SizeLabel.M)
                .build();

        ClothesSize clothesSize3 = ClothesSize.builder()
                .stockQuantity(5l)
                .sizeLabel(SizeLabel.L)
                .build();

        clothes.changeItemSizes(List.of(clothesSize1, clothesSize2, clothesSize3));


        Clothes clothes2 = Clothes.builder()
                .name("testName2")
                .engName("testEngName2")
                .price(500l)
                .build();

        ClothesSize clothesSize4 = ClothesSize.builder()
                .stockQuantity(20l)
                .sizeLabel(SizeLabel.S)
                .build();

        ClothesSize clothesSize5 = ClothesSize.builder()
                .stockQuantity(22l)
                .sizeLabel(SizeLabel.M)
                .build();

        ClothesSize clothesSize6 = ClothesSize.builder()
                .stockQuantity(25l)
                .sizeLabel(SizeLabel.L)
                .build();

        clothes2.changeItemSizes(List.of(clothesSize4, clothesSize5, clothesSize6));

        return List.of(clothes, clothes2);
    }
}
