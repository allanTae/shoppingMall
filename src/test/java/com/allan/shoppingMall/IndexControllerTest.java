package com.allan.shoppingMall;

import com.allan.shoppingMall.domains.cart.service.CartService;
import com.allan.shoppingMall.domains.infra.AuthenticationConverter;
import com.allan.shoppingMall.domains.item.domain.model.ClothesSummaryDTOForIndex;
import com.allan.shoppingMall.domains.item.service.ClothesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(
        controllers = IndexController.class
)
@AutoConfigureMockMvc
public class IndexControllerTest {

    @MockBean
    ClothesService clothesService;

    @MockBean
    AuthenticationConverter authenticationConverter;

    @Autowired
    MockMvc mvc;

    // securityConfig.class 의 SignInSuccessHandler 에서 사용 할 cartService.
    @MockBean
    CartService cartService;

    @Test
    public void 홈페이지_테스트() throws Exception {
        //given
        List<ClothesSummaryDTOForIndex> TEST_CLOTHES_SUMMARY_DTO = List.of();
        given(clothesService.getClothesSummary(any()))
                .willReturn(TEST_CLOTHES_SUMMARY_DTO);

        //when
        ResultActions resultActions = mvc.perform(get("/index"));

        //then
        verify(clothesService, atLeastOnce()).getClothesSummary(any());
        resultActions
                .andExpect(view().name("/index"));
    }
}
