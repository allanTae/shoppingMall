<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
        <style>
            .dropdown-menu li {
              position: relative;
            }
            .dropdown-menu .dropdown-submenu {
              display: none;
              position: absolute;
              left: 100%;
              top: -7px;
            }
            .dropdown-menu > li:hover > .dropdown-submenu {
              display: block;
            }
        </style>
        <!-- Navigation-->
        <nav class="navbar navbar-expand-lg navbar-dark fixed-top" id="mainNav">
            <div class="container">
                <div class="brandLogo">
                    <a href="${pageContext.request.contextPath}/index">
                        <img src="${pageContext.request.contextPath}/resources/common/img/index/logo.jpeg" width="100" alt="..." />
                    </a>
                </div>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
                    Menu
                    <i class="fas fa-bars ms-1"></i>
                </button>
                <div class="collapse navbar-collapse" id="navbarResponsive">
                    <ul class="navbar-nav text-uppercase ms-auto py-4 py-lg-0">
                        <li class="nav-item dropdown">
                            <a class="nav-link" href="#" class="dropdown-toggle" id="dropdownShop" data-bs-toggle="dropdown" aria-expanded="false">Shop</a>
                            <ul class="dropdown-menu" aria-labelledby="dropdownShop">
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/shop?categoryId=3">Shop All</a></li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/shop?categoryId=9999">NEW ARRIVAL</a></li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/shop?categoryId=9">OUTER</a></li>
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/shop?categoryId=5">TOPS</a>
                                    <ul class="dropdown-menu dropdown-submenu">
                                        <li>
                                          <a class="dropdown-item" href="${pageContext.request.contextPath}/shop?categoryId=11">Knit</a>
                                        </li>
                                        <li>
                                          <a class="dropdown-item" href="${pageContext.request.contextPath}/shop?categoryId=8">Shirt</a>
                                        </li>
                                        <li>
                                          <a class="dropdown-item" href="${pageContext.request.contextPath}/shop?categoryId=12">T-shirt & Sweatshirts</a>
                                        </li>
                                    </ul>
                                </li>
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/shop?categoryId=6">BOTTOM</a>
                                    <ul class="dropdown-menu dropdown-submenu">
                                        <li>
                                          <a class="dropdown-item" href="${pageContext.request.contextPath}/shop?categoryId=13">Jeans</a>
                                        </li>
                                        <li>
                                          <a class="dropdown-item" href="${pageContext.request.contextPath}/shop?categoryId=14">Trousers & Shorts</a>
                                        </li>
                                    </ul>
                                </li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/shop?categoryId=4">ACCESSORIES</a></li>
                            </ul>
                        </li>
                        <li class="nav-item"><a class="nav-link" href="#">inspiration</a></li>
                        <li class="nav-item"><a class="nav-link" href="#">collection</a></li>
                        <li class="nav-item"><a class="nav-link" href="#">about</a></li>
                        <li class="nav-item"><a class="nav-link" href="#">contact</a></li>
                        <li class="nav-item dropdown">
                            <a class="nav-link" href="#" class="dropdown-toggle" id="dropdownAccount" data-bs-toggle="dropdown" aria-expanded="false">account</a>
                            <ul class="dropdown-menu" aria-labelledby="dropdownAccount">
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/cart">Cart</a></li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/myOrder/list">OrderList</a></li>
                                <li><a class="dropdown-item" href="#">MyShop</a></li>
                            </ul>
                        </li>
                        <li class="nav-item dropdown">
                            <a class="nav-link" href="#" class="dropdown-toggle" id="dropdownCommunity" data-bs-toggle="dropdown" aria-expanded="false">community</a>
                            <ul class="dropdown-menu" aria-labelledby="dropdownCommunity">
                                <li><a class="dropdown-item" href="#">Notice</a></li>
                                <li><a class="dropdown-item" href="#">Q&A</a></li>
                                <li><a class="dropdown-item" href="#">Review</a></li>
                            </ul>
                        </li>
                        <sec:authorize access="hasRole('ROLE_ADMIN')" >
                            <li class="nav-item dropdown">
                                <a class="nav-link" href="#" class="dropdown-toggle" id="dropDownManager" data-bs-toggle="dropdown" aria-expanded="false">관리자 메뉴</a>
                                <ul class="dropdown-menu" aria-labelledby="dropDownManager">
                                  <li><a class="dropdown-item" href="${pageContext.request.contextPath}/clothes/clothesForm">의류상품 등록</a></li>
                                  <li><a class="dropdown-item" href="${pageContext.request.contextPath}/accessory/accessoryForm">악세서리상품 등록</a></li>
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