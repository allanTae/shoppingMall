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
        <script>
            function tempAlert(){
                alert("업데이트 예정입니다.");
            }

            // 조회한 상품 카테고리 정보를 저장하는 오브젝트.
            var shopNavCategoryObj;

            $(function(){
                    setShopNavCategory();
                });

            // 카테고리 정보 set 함수.
            function setShopNavCategory(){
                var headers = {"Content-Type" : "application/json; charset=UTF-8;"
                              , "X-HTTP-Method-Override" : "GET"};
                $.ajax({
                  url: "${pageContext.request.contextPath}/category/shop"
                  , headers : headers
                  , type : 'GET'
                  , dataType : 'json'
                  , success: function(result){
                    if(result.apiResultMessage === "카테고리 조회에 성공하였습니다."){
                        shopNavCategoryObj = result.category; // 카테고리 정보 저장.
                        if(Object.keys(result.category.child).length > 0){
                            var html = '';

                            $.each(result.category.child, function(key, value) {
                                html += '<li>';
                                html += '   <a class="dropdown-item" href="${pageContext.request.contextPath}/shop?categoryId=' + value.categoryId +'" >' + value.name + '</a>';
                                if(Object.keys(value.child).length > 0){
                                    html += '<ul class="dropdown-menu dropdown-submenu">';
                                    $.each(value.child, function(key, childValue) {
                                        html += '<li>';
                                        html += '   <a class="dropdown-item" href="${pageContext.request.contextPath}/shop?categoryId=' + childValue.categoryId +'" >' + childValue.name + '</a>';
                                        html += '</li>';
                                    });
                                    html += '</ul>';
                                }
                                html += '</li>';
                            });
                            $('#shopCategory').append(html);
                        }else{
                            alert("등록 된 카테고리가 없습니다. \n 우선 카테고리를 등록 해 주세요.");
                        }

                    }else{
                        alert(result.apiResultMessage);
                    }
                  }
                  , error:function(request,status,error){
                    console.log("error: " + error);
                  }
                });
            }

        </script>
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
                    <ul class="navbar-nav text-uppercase m-4 py-4 py-lg-0">
                        <li class="nav-item dropdown">
                            <a class="nav-link" href="#" class="dropdown-toggle" id="dropdownShop" data-bs-toggle="dropdown" aria-expanded="false">Shop</a>
                            <ul class="dropdown-menu" aria-labelledby="dropdownShop" id="shopCategory">
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/shop?categoryId=3">Shop All</a></li>
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
                                    <li>
                                        <a class="dropdown-item" href="${pageContext.request.contextPath}/manage/itemList">상품수정</a>
                                    </li>
                                    <li>
                                        <a class="dropdown-item" href="#">상품등록</a>
                                        <ul class="dropdown-menu dropdown-submenu">
                                            <li>
                                              <a class="dropdown-item" href="${pageContext.request.contextPath}/clothes/clothesForm">의류상품</a>
                                            </li>
                                            <li>
                                              <a class="dropdown-item" href="${pageContext.request.contextPath}/accessory/accessoryForm">악세서리상품</a>
                                            </li>
                                        </ul>
                                    </li>
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