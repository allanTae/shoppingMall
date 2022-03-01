package com.allan.shoppingMall.domains.item.presentation;

import com.allan.shoppingMall.domains.cart.service.CartService;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesSummeryDTO;
import com.allan.shoppingMall.domains.item.presentation.clothes.ClothesResult;
import com.allan.shoppingMall.domains.item.presentation.clothes.RestClothesController;
import com.allan.shoppingMall.domains.item.service.ClothesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = RestClothesController.class
)
@AutoConfigureMockMvc
public class RestClothesControllerTest {
    @MockBean
    ClothesService clothesService;

    @Autowired
    MockMvc mockMvc;

    // securityConfig.class 의 SignInSuccessHandler 에서 사용 할 cartService.
    @MockBean
    CartService cartService;

    @Test
    public void 의류요약정보_조회테스트() throws Exception {
        //given
        ClothesSummeryDTO TEST_CLOTHES_SUMMARY_DTO = ClothesSummeryDTO.builder().build();
        given(clothesService.getClothesSummaryDTO(any()))
                .willReturn(TEST_CLOTHES_SUMMARY_DTO);

        //when
        ResultActions resultActions = mockMvc.perform(get("/clothes/1"));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("apiResultMessage").value(ClothesResult.GET_CLOTHES_INFO_SUCCESS.getMessage()))
                .andExpect(jsonPath("apiResult").value(ClothesResult.GET_CLOTHES_INFO_SUCCESS.getResult()));
    }
}
