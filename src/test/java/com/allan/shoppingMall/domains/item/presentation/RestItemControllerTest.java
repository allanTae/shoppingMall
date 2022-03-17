package com.allan.shoppingMall.domains.item.presentation;

import com.allan.shoppingMall.domains.cart.service.CartService;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesSummeryDTO;
import com.allan.shoppingMall.domains.item.domain.item.ItemSummaryDTOForCart;
import com.allan.shoppingMall.domains.item.presentation.clothes.ClothesResult;
import com.allan.shoppingMall.domains.item.service.ItemService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = RestItemController.class
)
@AutoConfigureMockMvc
public class RestItemControllerTest {
    @MockBean
    ItemService itemService;

    @Autowired
    MockMvc mockMvc;

    // securityConfig.class 의 SignInSuccessHandler 에서 사용 할 cartService.
    @MockBean
    CartService cartService;

    @Test
    public void 의류요약정보_조회테스트() throws Exception {
        //given
        ItemSummaryDTOForCart TEST_ITEM_DTO = ItemSummaryDTOForCart.builder().build();

        given(itemService.getItemSummaryDTO(any()))
                .willReturn(TEST_ITEM_DTO);

        //when
        ResultActions resultActions = mockMvc.perform(get("/item/1"));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("apiResultMessage").value(ItemResult.GET_ITEM_INFO_SUCCESS.getMessage()))
                .andExpect(jsonPath("apiResult").value(ItemResult.GET_ITEM_INFO_SUCCESS.getResult()));
    }
}
