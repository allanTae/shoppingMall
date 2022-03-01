package com.allan.shoppingMall.domains.item.presentation;

import com.allan.shoppingMall.common.config.security.SecurityConfig;
import com.allan.shoppingMall.domains.category.domain.Category;
import com.allan.shoppingMall.domains.category.domain.CategoryCode;
import com.allan.shoppingMall.domains.category.domain.CategoryRepository;
import com.allan.shoppingMall.domains.item.domain.model.ClothesDTO;
import com.allan.shoppingMall.domains.item.service.ClothesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                )
        })
@AutoConfigureMockMvc
@WithMockUser
public class ItemControllerTest {

    @MockBean
    ClothesService clothesService;

    @MockBean
    CategoryRepository categoryRepository;

    @Autowired
    MockMvc mvc;

    @Test
    public void 의류_조회_테스트() throws Exception {
        //given
        ClothesDTO TEST_CLOTHES_DTO = ClothesDTO.builder()
                .itemImages(List.of())
                .build();
        given(clothesService.getClothes(any()))
                .willReturn(TEST_CLOTHES_DTO);

        Category TEST_CATEGORY = Category.builder()
                .categoryCode(CategoryCode.CLOTHES)
                .build();

        given(categoryRepository.findById(anyLong()))
                .willReturn(Optional.of(TEST_CATEGORY));

        //when
        ResultActions resultActions = mvc.perform(get("/item?categoryId=1&clothesId=1"));

        //then
        verify(categoryRepository, atLeastOnce()).findById(anyLong());
        verify(clothesService, atLeastOnce()).getClothes(anyLong());
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("clothesInfo"))
                .andExpect(view().name("clothes/clothesDetail"));
    }

}
