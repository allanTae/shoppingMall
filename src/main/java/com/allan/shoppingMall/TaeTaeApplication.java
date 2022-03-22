package com.allan.shoppingMall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
public class TaeTaeApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(TaeTaeApplication.class, args);
		DispatcherServlet dispatcherServlet = (DispatcherServlet) ctx.getBean("dispatcherServlet");
		dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
	}

}
