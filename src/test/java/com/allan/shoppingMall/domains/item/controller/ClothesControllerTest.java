package com.allan.shoppingMall.domains.item.controller;

import com.allan.shoppingMall.domains.item.domain.model.ClothesForm;
import com.allan.shoppingMall.domains.item.presentation.ClothesController;
import com.allan.shoppingMall.domains.item.service.ClothesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(
        controllers = ClothesController.class
)
@AutoConfigureMockMvc
public class ClothesControllerTest {

    @MockBean
    ClothesService clothesService;

    @Autowired
    MockMvc mvc;

    @Test
    public void 의류상품_추가_테스트() throws Exception {
        //given
        ClothesForm TEST_CLOTHES_REQEUST = createClothesRequest();

        //when
        ResultActions resultActions = mvc.perform(post("/clothes/save")
                                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                            .flashAttr("clothesRequest", TEST_CLOTHES_REQEUST));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/main"));
        verify(clothesService, atLeastOnce()).saveClothes(any());
    }

    private ClothesForm createClothesRequest() {
        ClothesForm clothesRequest = new ClothesForm();
        return clothesRequest;
    }
}
