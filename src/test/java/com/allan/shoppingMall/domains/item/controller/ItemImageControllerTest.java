package com.allan.shoppingMall.domains.item.controller;

import com.allan.shoppingMall.domains.item.domain.ItemImage;
import com.allan.shoppingMall.domains.item.domain.ItemImageRepository;
import com.allan.shoppingMall.domains.item.presentation.ItemImageController;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = ItemImageController.class
)
@WithMockUser
@AutoConfigureMockMvc
public class ItemImageControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    ItemImageRepository itemImageRepository;

    @Test
    public void 상품_이미지_테스트() throws Exception {
        //given
        ItemImage TEST_ITEM_IMAGE = createItemImage();
        given(itemImageRepository.findById(any()))
                .willReturn(Optional.of(TEST_ITEM_IMAGE));

        //when
        ResultActions resultActions = mvc.perform(get("/image/2"));

        //then
        verify(itemImageRepository,atLeastOnce()).findById(any());
        resultActions
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type","image/jpeg"));
    }

    @Test
    public void 이미지_스트림으로_조회_테스트() throws Exception {
        //given
        ItemImage TEST_ITEM_IMAGE = createItemImage();
        given(itemImageRepository.findById(any()))
                .willReturn(Optional.of(TEST_ITEM_IMAGE));

        //when
        ResultActions resultActions = mvc.perform(get("/image/stream/2"));

        //then
        verify(itemImageRepository,atLeastOnce()).findById(any());
        resultActions
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type","image/jpeg"));
    }

    private ItemImage createItemImage() {
        return ItemImage.builder()
                .itemImagePath("images/item/20211125/1193583152716179.jpeg")
                .build();
    }
}
