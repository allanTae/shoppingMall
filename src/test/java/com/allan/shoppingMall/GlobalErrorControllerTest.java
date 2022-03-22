package com.allan.shoppingMall;

import com.allan.shoppingMall.common.error.GlobalErrorController;
import com.allan.shoppingMall.domains.cart.service.CartService;
import com.allan.shoppingMall.domains.infra.AuthenticationConverter;
import com.allan.shoppingMall.domains.item.service.ClothesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.DispatcherServlet;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(
        controllers = IndexController.class
)
public class GlobalErrorControllerTest {

    @MockBean
    ClothesService clothesService;

    @MockBean
    AuthenticationConverter authenticationConverter;

    @Autowired
    MockMvc mvc;

    // securityConfig.class 의 SignInSuccessHandler 에서 사용 할 cartService.
    @MockBean
    CartService cartService;

    // mock test를 위한 설정.
    @BeforeEach
    void setUp() {
        ClothesService TEST_CLOTHES_SERVICE = mock(ClothesService.class);
        AuthenticationConverter TEST_AUTHENTICATION_CONVERTER = mock(AuthenticationConverter.class);

        IndexController controller = new IndexController(TEST_CLOTHES_SERVICE, TEST_AUTHENTICATION_CONVERTER);
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(GlobalErrorController.class)
                .build();

        // dispatcherServlet 에서 핸들 할 컨트롤러가 없으면 NoHandlerFoundException 을 발생하도록 설정.
        DispatcherServlet dispatcherServlet = mvc.getDispatcherServlet();
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
    }

    @Test
    public void 홈페이지_404_NOT_FOUND_테스트() throws Exception {
        //given, when
        ResultActions resultActions = mvc.perform(get("/invalid/url"));

        //then
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/404"));
    }

}
