package com.allan.shoppingMall;

import com.allan.shoppingMall.common.error.GlobalErrorController;
import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.item.ItemNotFoundException;
import com.allan.shoppingMall.domains.cart.service.CartService;
import com.allan.shoppingMall.domains.infra.AuthenticationConverter;
import com.allan.shoppingMall.domains.item.service.ClothesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(
        controllers = IndexController.class
)
public class GlobalErrorControllerTest {

    @Autowired
    MockMvc mvc;

    // securityConfig.class 의 SignInSuccessHandler 에서 사용 할 cartService.
    @MockBean
    CartService cartService;

    @MockBean
    ClothesService TEST_CLOTHES_SERVICE;

    @MockBean
    AuthenticationConverter TEST_AUTHENTICATION_CONVERTER;

    // mock test를 위한 설정.
    @BeforeEach
    void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/view/");
        viewResolver.setSuffix(".jsp");

        TEST_CLOTHES_SERVICE = mock(ClothesService.class);
        TEST_AUTHENTICATION_CONVERTER = mock(AuthenticationConverter.class);

        IndexController controller = new IndexController(TEST_CLOTHES_SERVICE, TEST_AUTHENTICATION_CONVERTER);
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(GlobalErrorController.class)
                .setViewResolvers(viewResolver)
                .build();

        // dispatcherServlet 에서 핸들 할 컨트롤러가 없으면 NoHandlerFoundException 을 발생하도록 설정.
        DispatcherServlet dispatcherServlet = mvc.getDispatcherServlet();
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
    }

    /**
     * 웹 애플리케이션에서 Not Found 테스트 입니다.(404 http status code 처리 합니다.)
     * 유효하지 않은 url 을 요청 하였을때, NoHandlerFoundException 를 감지하고 ControllerAdvice 에서 등록 한 ExceptionHandler 가
     * 잘 동작하는지 테스트 합니다.
     */
    @Test
    public void 웹애플리케이션_404_NOT_FOUND_테스트() throws Exception {
        // given, when
        ResultActions resultActions = mvc.perform(get("/invalid/url"));

        // then
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/404"));
    }

    /**
     * BusinessException 처리하는 테스트 입니다.(500 http status code 처리 합니다.)
     * 비즈니스 예외가 발생 했을 때, BusinessException 를 감지하고 ControllerAdvice 에서 등록 한 ExceptionHandler 가
     * 잘 동작하는지 테스트 합니다.
     */
    @Test
    public void 비즈니스_프로세스_예외_핸들링_테스트() throws Exception {
        // given
        doThrow(new ItemNotFoundException(ErrorCode.ENTITY_NOT_FOUND))
                .when(TEST_CLOTHES_SERVICE).getClothesSummary(any());

        // when
        ResultActions resultActions = mvc.perform(get("/index"));

        // then
        resultActions
                .andExpect(status().isInternalServerError())
                .andExpect(view().name("error/500"));
    }

}
