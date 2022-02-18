package com.allan.shoppingMall.domains.item.presentation;

import com.allan.shoppingMall.common.TestUserDetailsService;
import com.allan.shoppingMall.domains.cart.service.CartService;
import com.allan.shoppingMall.domains.item.domain.model.ClothesDTO;
import com.allan.shoppingMall.domains.item.domain.model.ClothesForm;
import com.allan.shoppingMall.domains.item.service.ClothesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = ClothesController.class
)
@AutoConfigureMockMvc
@WithUserDetails("test")
@Import(TestUserDetailsService.class)
public class ClothesControllerTest {

    @MockBean
    ClothesService clothesService;

    @Autowired
    MockMvc mvc;

    // securityConfig.class 의 SignInSuccessHandler 에서 사용 할 cartService.
    @MockBean
    CartService cartService;

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
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/index"));
        verify(clothesService, atLeastOnce()).saveClothes(any());
    }

    @Test
    public void 의류_등록_폼() throws Exception {
        //given
        ClothesForm TEST_CLOTHES_FORM = createClothesRequest();

        //when
        ResultActions resultActions = mvc.perform(get("/clothes/clothesForm"));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("clothes/clothesForm"));
    }

    @Test
    public void 의류_조회_테스트() throws Exception {
        //given
        ClothesDTO TEST_CLOTHES_DTO = ClothesDTO.builder()
                .itemImages(List.of())
                .build();
        given(clothesService.getClothes(any()))
                .willReturn(TEST_CLOTHES_DTO);

        //when
        ResultActions resultActions = mvc.perform(get("/clothes?clothesId=1"));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("clothesInfo"))
                .andExpect(view().name("clothes/clothesDetail"));
    }


    private ClothesForm createClothesRequest() {
        ClothesForm clothesRequest = new ClothesForm();
        return clothesRequest;
    }
}
