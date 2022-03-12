package com.allan.shoppingMall.domains.item.presentation;

import com.allan.shoppingMall.domains.cart.service.CartService;
import com.allan.shoppingMall.domains.item.domain.item.ImageType;
import com.allan.shoppingMall.domains.item.domain.item.ItemImage;
import com.allan.shoppingMall.domains.item.domain.model.AccessoryDTO;
import com.allan.shoppingMall.domains.item.domain.model.AccessoryForm;
import com.allan.shoppingMall.domains.item.domain.model.ClothesForm;
import com.allan.shoppingMall.domains.item.presentation.accessory.AccessoryController;
import com.allan.shoppingMall.domains.item.service.AccessoryService;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AccessoryController.class)
@AutoConfigureMockMvc
@WithMockUser
public class AccessoryControllerTest {

    @MockBean
    AccessoryService accessoryService;

    @Autowired
    MockMvc mvc;

    // securityConfig.class 의 SignInSuccessHandler 에서 사용 할 cartService.
    @MockBean
    CartService cartService;

    @Test
    public void 악세서리_상품_등록테스트() throws Exception {
        //given
        AccessoryForm TEST_ACCESSORY_FORM = createAccessoryForm(false);

        //when
        ResultActions resultActions = mvc.perform(post("/accessory/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("accessoryForm", TEST_ACCESSORY_FORM));

        //then
        verify(accessoryService, atLeastOnce()).saveAccessory(any());
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/index"));
    }

    @Test
    public void 악세서리_상품_수정테스트() throws Exception {
        //given
        AccessoryForm TEST_ACCESSORY_FORM = createAccessoryForm(true);

        //when
        ResultActions resultActions = mvc.perform(post("/accessory/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("accessoryForm", TEST_ACCESSORY_FORM));

        //then
        verify(accessoryService, atLeastOnce()).updateAccessory(any());
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/index"));
    }

    @Test
    public void 악세서리_등록_폼테스트() throws Exception {
        //given
        AccessoryForm TEST_ACCESSORY_FORM = createAccessoryForm(false);

        //when
        ResultActions resultActions = mvc.perform(get("/accessory/accessoryForm")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("accessoryForm", TEST_ACCESSORY_FORM));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("sizeLabels"))
                .andExpect(view().name("accessory/accessoryForm"));
    }

    @Test
    public void 악세서리_수정_폼테스트() throws Exception {
        //given
        AccessoryForm TEST_ACCESSORY_FORM = createAccessoryForm(false);
        ItemImage TEST_ITEM_PRFILE_IMAGE = ItemImage.builder()
                .imageType(ImageType.PREVIEW)
                .build();
        ItemImage TEST_ITEM_PRODUCT_IMAGE = ItemImage.builder()
                .imageType(ImageType.PRODUCT)
                .build();
        ReflectionTestUtils.setField(TEST_ITEM_PRFILE_IMAGE, "itemImageId", 1l);
        ReflectionTestUtils.setField(TEST_ITEM_PRODUCT_IMAGE, "itemImageId", 2l);

        AccessoryDTO TEST_ACCESSORY_DTO = AccessoryDTO.builder()
                .itemImages(List.of(TEST_ITEM_PRFILE_IMAGE, TEST_ITEM_PRODUCT_IMAGE))
                .build();
        given(accessoryService.getAccessory(any()))
                .willReturn(TEST_ACCESSORY_DTO);

        //when
        ResultActions resultActions = mvc.perform(get("/accessory/accessoryForm")
                .param("accessoryId", "1")
                .param("mode", "edit")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("accessoryForm", TEST_ACCESSORY_FORM));

        //then
        verify(accessoryService, atLeastOnce()).getAccessory(any());
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("accessoryInfo"))
                .andExpect(model().attributeExists("sizeLabels"))
                .andExpect(view().name("accessory/accessoryForm"));
    }

    private AccessoryForm createAccessoryForm(Boolean isEdit){
        AccessoryForm accessoryForm = new AccessoryForm();
        if(isEdit){
            accessoryForm.setMode("edit");
        }
        return accessoryForm;
    }
}
