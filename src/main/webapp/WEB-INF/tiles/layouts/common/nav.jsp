<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
        <!-- Navigation-->
        <nav class="navbar navbar-expand-lg navbar-dark fixed-top" id="mainNav">
            <div class="container">
                <div class="brandLogo">
                    <a class="navbar-brand" href="${pageContext.request.contextPath}/index"><img src="${pageContext.request.contextPath}/resources/common/img/index/navbar-logo.svg" alt="..." /></a>
                </div>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
                    Menu
                    <i class="fas fa-bars ms-1"></i>
                </button>
                <div class="collapse navbar-collapse" id="navbarResponsive">
                    <ul class="navbar-nav text-uppercase ms-auto py-4 py-lg-0">
                        <li class="nav-item dropdown">
                          <a class="nav-link" href="#services" class="dropdown-toggle" id="dropdownShop" data-bs-toggle="dropdown" aria-expanded="false">
                            Shop
                          </a>
                          <ul class="dropdown-menu" aria-labelledby="dropdownShop">
                            <li><a class="dropdown-item" href="#">Shop All</a></li>
                            <li><a class="dropdown-item" href="#">New Arrivals</a></li>
                            <li><a class="dropdown-item" href="#">Outerwear</a></li>
                            <li><a class="dropdown-item" href="#">Knitwear</a></li>
                            <li><a class="dropdown-item" href="#">Shirt</a></li>
                            <li><a class="dropdown-item" href="#">Jeans</a></li>
                            <li><a class="dropdown-item" href="#">Bags</a></li>
                          </ul>
                        </li>
                        <li class="nav-item"><a class="nav-link" href="#portfolio">Re:position</a></li>
                        <li class="nav-item"><a class="nav-link" href="#about">collection</a></li>
                        <li class="nav-item"><a class="nav-link" href="#team">about</a></li>
                        <sec:authorize access="hasRole('ROLE_ADMIN')" >
                            <li class="nav-item dropdown">
                                <a class="nav-link" href="#" class="dropdown-toggle" id="dropDownManager" data-bs-toggle="dropdown" aria-expanded="false">관리자 메뉴</a>
                                <ul class="dropdown-menu" aria-labelledby="dropDownManager">
                                  <li><a class="dropdown-item" href="${pageContext.request.contextPath}/clothes/clothesForm">상품 등록</a></li>
                                </ul>
                            </li>
                        </sec:authorize>
                        <sec:authorize access="hasRole('ROLE_USER')" >
                            <li class="nav-item dropdown">
                                <a class="nav-link" href="#" class="dropdown-toggle" id="dropDownUser" data-bs-toggle="dropdown" aria-expanded="false">myMenu</a>
                                <ul class="dropdown-menu" aria-labelledby="dropDownUser">
                                  <li><a class="dropdown-item" href="${pageContext.request.contextPath}/myOrder/list">주문내역</a></li>
                                </ul>
                            </li>
                        </sec:authorize>
                        <sec:authorize access="!isAuthenticated()">
                            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/auth/loginForm">login</a></li>
                        </sec:authorize>
                        <sec:authorize access="isAuthenticated()">
                            <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/auth/logout">logout</a></li>
                        </sec:authorize>
                    </ul>
                </div>
            </div>
        </nav>