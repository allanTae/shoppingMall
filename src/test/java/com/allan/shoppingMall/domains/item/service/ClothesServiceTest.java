package com.allan.shoppingMall.domains.item.service;

import com.allan.shoppingMall.domains.item.domain.ItemRepository;
import com.allan.shoppingMall.domains.item.domain.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Rollback(value = true)
public class ClothesServiceTest {

    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    ClothesService clothesService;

    @Test
    public void 의류_상품_등록_테스트() throws Exception {
        //given
        ClothesForm TEST_CLOTHES_REQUEST = createClothesRequest();

        //when
        clothesService.saveClothes(TEST_CLOTHES_REQUEST);

        //then
        verify(itemRepository, atLeastOnce()).save(any());
    }

    private ClothesForm createClothesRequest() {
        ClothesForm clothesRequest = new ClothesForm();
        clothesRequest.setName("testName");
        clothesRequest.setEngName("testEngName");
        clothesRequest.setPrice(10000l);
        clothesRequest.setStockQuantity(100l);
        clothesRequest.setClothesFabrics(List.of(
                new ClothesFabricsDTO("clothesMaterialPart", "clothesMaterialDesc")
        ));
        clothesRequest.setClothesDetails(List.of(
                new ClothesDetailsDTO("clothesDetailDesc")
        ));
        clothesRequest.setClothesSizes(List.of(
                new ClothesSizesDTO("1", 20.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80.0)
        ));
        clothesRequest.setModelSizes(List.of(
                new ModelSizeDTO(10.0, 20.0, 30.0, 40.0, 50.0)
        ));
        return clothesRequest;
    }
}
