package com.allan.shoppingMall.domains;

import com.allan.shoppingMall.ShopController;
import com.allan.shoppingMall.common.config.security.SecurityConfig;
import com.allan.shoppingMall.domains.cart.service.CartService;
import com.allan.shoppingMall.domains.category.domain.Category;
import com.allan.shoppingMall.domains.category.domain.CategoryRepository;
import com.allan.shoppingMall.domains.infra.AuthenticationConverter;
import com.allan.shoppingMall.domains.item.domain.item.Color;
import com.allan.shoppingMall.domains.item.domain.item.Item;
import com.allan.shoppingMall.domains.item.domain.model.ItemSummaryDTO;
import com.allan.shoppingMall.domains.item.service.ItemService;
import com.allan.shoppingMall.domains.mileage.service.MileageService;
import com.allan.shoppingMall.domains.order.presentation.OrderController;
import com.allan.shoppingMall.domains.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = ShopController.class)
@AutoConfigureMockMvc
public class ShopControllerTest {

    @MockBean
    ItemService itemService;

    @MockBean
    CategoryRepository categoryRepository;

    @Autowired
    MockMvc mvc;

    // securityConfig.class 의 SignInSuccessHandler 에서 사용 할 cartService.
    @MockBean
    CartService cartService;

    /**
     * 특정 카테고리에 해당하는 모든 상품을 조회하는 테스트 입니다.
     */
    @Test
    public void 카테고리별_상품_목록_조회_테스트() throws Exception {
        //given
        Category TEST_CATEGORY = Category.builder()
                .name("testCategoryName")
                .build();
        ReflectionTestUtils.setField(TEST_CATEGORY, "categoryId", 1l);

        given(categoryRepository.findById(any()))
                .willReturn(Optional.of(TEST_CATEGORY));

        Item TEST_ITEM = new Item("testName", 1000l, Color.RED);
        Page<Item> itemPage = mock(Page.class);
        given(itemPage.getTotalElements())
                .willReturn(1l);
        given(itemPage.getContent())
                .willReturn(List.of(TEST_ITEM));

        given(itemService.getItems(any(), any()))
                .willReturn(itemPage);

        List<ItemSummaryDTO> TEST_ITEM_SUMMARY_DTO = new ArrayList<>();
        given(itemService.getItemDTOS(any(), any()))
                .willReturn(TEST_ITEM_SUMMARY_DTO);

        //when
        ResultActions resultActions = mvc.perform(get("/shop")
                .param("categoryId", "1")
                .param("page", "1"));

        //then
        verify(categoryRepository, atLeastOnce()).findById(any());
        verify(itemService, atLeastOnce()).getItems(any(), any());
        verify(itemService, atLeastOnce()).getItemDTOS(any(), any());

        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("categoryName"))
                .andExpect(model().attributeExists("categoryId"))
                .andExpect(model().attributeExists("itemQunatity"))
                .andExpect(model().attributeExists("itemList"))
                .andExpect(model().attributeExists("pagination"))
                .andExpect(view().name("shop/itemList"));
    }
}
