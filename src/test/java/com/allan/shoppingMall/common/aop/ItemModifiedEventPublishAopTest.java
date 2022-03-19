package com.allan.shoppingMall.common.aop;

import com.allan.shoppingMall.common.config.aop.ItemModifiedEventPublishAop;
import com.allan.shoppingMall.domains.category.domain.Category;
import com.allan.shoppingMall.domains.category.domain.CategoryCode;
import com.allan.shoppingMall.domains.category.domain.CategoryItemRepository;
import com.allan.shoppingMall.domains.category.domain.CategoryRepository;
import com.allan.shoppingMall.domains.item.domain.accessory.Accessory;
import com.allan.shoppingMall.domains.item.domain.accessory.AccessoryRepository;
import com.allan.shoppingMall.domains.item.domain.clothes.Clothes;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesRepository;
import com.allan.shoppingMall.domains.item.domain.item.ItemModifiedEvent;
import com.allan.shoppingMall.domains.item.domain.item.ItemSizeRepository;
import com.allan.shoppingMall.domains.item.domain.model.AccessoryForm;
import com.allan.shoppingMall.domains.item.domain.model.ClothesForm;
import com.allan.shoppingMall.domains.item.infra.ImageFileHandler;
import com.allan.shoppingMall.domains.item.service.AccessoryService;
import com.allan.shoppingMall.domains.item.service.ClothesService;
import org.junit.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * 상품정보 수정시, 상품수정 이벤트 publish aop 테스트 클래스 입니다.
 */
@SpringBootTest
public class ItemModifiedEventPublishAopTest {

    @Test
    public void 의류_상품_수정시_상품수정_이벤트_발생테스트() throws Exception {
        //given
        ClothesRepository TEST_CLOTHES_REPOSITORY = mock(ClothesRepository.class);
        ImageFileHandler TEST_FILE_IMAGE = mock(ImageFileHandler.class);
        CategoryRepository TEST_CATEGORY_REPOSITORY = mock(CategoryRepository.class);
        CategoryItemRepository TEST_CATEGORY_ITEM_REPOSITORY = mock(CategoryItemRepository.class);
        ApplicationContext TEST_APPLICATION_CONTEXT = mock(ApplicationContext.class);
        ItemSizeRepository TEST_ITEM_SIZE_REPOSITORY = mock(ItemSizeRepository.class);

        Clothes TEST_CLOTHES = Clothes.builder().build();
        given(TEST_CLOTHES_REPOSITORY.findById(any()))
                .willReturn(Optional.of(TEST_CLOTHES));

        Category TEST_CATEGORY = Category.builder()
                .categoryCode(CategoryCode.CLOTHES)
                .build();
        given(TEST_CATEGORY_REPOSITORY.findById(any()))
                .willReturn(Optional.of(TEST_CATEGORY));

        AspectJProxyFactory factory = new AspectJProxyFactory(new ClothesService(TEST_CLOTHES_REPOSITORY, TEST_FILE_IMAGE, TEST_CATEGORY_REPOSITORY, TEST_CATEGORY_ITEM_REPOSITORY));
        factory.addAspect(new ItemModifiedEventPublishAop(TEST_APPLICATION_CONTEXT));
        ClothesService proxyClothesService = factory.getProxy();

        //when
        proxyClothesService.updateClothes(createdClothesForm());

        //then
        verify(TEST_APPLICATION_CONTEXT, atLeastOnce()).publishEvent(any(ItemModifiedEvent.class));
    }

    @Test
    public void 악세서리_상품_수정시_상품수정_이벤트_발생테스트() throws Exception {
        //given
        AccessoryRepository TEST_ACCESSORY_REPOSITORY = mock(AccessoryRepository.class);
        ImageFileHandler TEST_IMAGE_FILE_HANDLER = mock(ImageFileHandler.class);
        CategoryRepository TEST_CATEGORY_REPOSITORY = mock(CategoryRepository.class);
        CategoryItemRepository TEST_CATEGORY_ITEM_REPOSITORY = mock(CategoryItemRepository.class);
        ApplicationContext TEST_APPLICATION_CONTEXT = mock(ApplicationContext.class);

        Accessory TEST_ACCESSORY = Accessory.builder().build();
        given(TEST_ACCESSORY_REPOSITORY.findById(any()))
                .willReturn(Optional.of(TEST_ACCESSORY));

        Category TEST_CATEGORY = Category.builder()
                .categoryCode(CategoryCode.ACCESSORY)
                .build();
        given(TEST_CATEGORY_REPOSITORY.findById(any()))
                .willReturn(Optional.of(TEST_CATEGORY));

        AspectJProxyFactory factory = new AspectJProxyFactory(new AccessoryService(TEST_ACCESSORY_REPOSITORY, TEST_IMAGE_FILE_HANDLER, TEST_CATEGORY_REPOSITORY, TEST_CATEGORY_ITEM_REPOSITORY));
        factory.addAspect(new ItemModifiedEventPublishAop(TEST_APPLICATION_CONTEXT));
        AccessoryService proxyAccessoryService = factory.getProxy();

        //when
        proxyAccessoryService.updateAccessory(createdAccessoryForm());

        //then
        verify(TEST_APPLICATION_CONTEXT, atLeastOnce()).publishEvent(any(ItemModifiedEvent.class));
    }

    private AccessoryForm createdAccessoryForm(){
        return AccessoryForm.builder()
                .itemFabrics(List.of())
                .itemDetails(List.of())
                .accessorySizes(List.of())
                .accessoryColor(2)
                .build();
    }

    private ClothesForm createdClothesForm(){
        return ClothesForm.builder()
                .itemFabrics(List.of())
                .itemDetails(List.of())
                .clothesSizes(List.of())
                .modelSizes(List.of())
                .clothesColor(2)
                .build();
    }
}
