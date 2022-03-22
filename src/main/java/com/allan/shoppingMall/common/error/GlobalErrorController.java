package com.allan.shoppingMall.common.error;

import com.allan.shoppingMall.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
@Slf4j
public class GlobalErrorController {

    /**
     * 404 server error 를 처리하는 메소드.
     * @param e
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNoHandlerFoundException(Exception e) {
        return "error/404";
    }

    /**
     * business exception 을 500 server error 로 처리하는 핸들러 메소드.
     * @param e
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BusinessException.class)
    public String handleBusinessException(Exception e){
        return "error/500";
    }
}
