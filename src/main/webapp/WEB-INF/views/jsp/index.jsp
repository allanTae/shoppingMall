<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <title>SeeAa Mall</title>

        <!-- Boot bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">

        <!-- Favicon-->
        <link rel="icon" type="image/x-icon" href="assets/favicon.ico" />
        <!-- Font Awesome icons (free version)-->
        <script src="https://use.fontawesome.com/releases/v5.15.4/js/all.js" crossorigin="anonymous"></script>
        <!-- Google fonts-->
        <link href="https://fonts.googleapis.com/css?family=Montserrat:400,700" rel="stylesheet" type="text/css" />
        <link href="https://fonts.googleapis.com/css?family=Roboto+Slab:400,100,300,700" rel="stylesheet" type="text/css" />
        <!-- Core theme CSS (includes Bootstrap)-->
        <link href="${pageContext.request.contextPath}/resources/common/css/indexStyle.css" rel="stylesheet" />

        <!-- carousel style -->
        <style>
            @media (max-width: 767px) {
                .carousel-inner .carousel-item > div {
                    display: none;
                }
                .carousel-inner .carousel-item > div:first-child {
                    display: block;
                }
            }

            .carousel-inner .carousel-item.active,
            .carousel-inner .carousel-item-next,
            .carousel-inner .carousel-item-prev {
                display: flex;
            }

            /* medium and up screens */
            @media (min-width: 768px) {
                .carousel-inner .carousel-item-end.active,
                .carousel-inner .carousel-item-next {
                  transform: translateX(25%);
                }
                .carousel-inner .carousel-item-start.active,
                .carousel-inner .carousel-item-prev {
                  transform: translateX(-25%);
                }
            }

            .carousel-inner .carousel-item-end,
            .carousel-inner .carousel-item-start {
              transform: translateX(0);
            }

        </style>

    </head>
    <body id="page-top">
        <!-- Navigation-->
        <nav class="navbar navbar-expand-lg navbar-dark fixed-top" id="mainNav">
            <div class="container">
                <div class="brandLogo">
                    <a class="navbar-brand" href="#page-top"><img src="${pageContext.request.contextPath}/resources/common/img/index/navbar-logo.svg" alt="..." /></a>
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
                                <a class="nav-link" href="#team" class="dropdown-toggle" id="dropDownManager" data-bs-toggle="dropdown" aria-expanded="false">관리자 메뉴</a>
                                <ul class="dropdown-menu" aria-labelledby="dropDownManager">
                                  <li><a class="dropdown-item" href="${pageContext.request.contextPath}/clothes/clothesForm">상품 등록</a></li>
                                </ul>
                            </li>
                        </sec:authorize>
                        <sec:authorize access="hasRole('ROLE_USER')" >
                            <li class="nav-item"><a class="nav-link" href="#team">myMenu</a></li>
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
        <!-- Banner -->
        <header class="masthead">
            <div class="container">
                <div class="masthead-subheading">Welcome To Our SeeAa Mall!</div>
                <div class="masthead-heading text-uppercase">It's Nice To Meet You</div>
            </div>
        </header>

        <!-- Collection -->
        <section class="page-section" id="services">
            <div class="container">
                <div class="text-center">
                    <h2 class="section-heading text-uppercase">Winter Season Collection!</h2>
                    <h3 class="section-subheading text-muted">2021 winter season items by designer SiHyeon.</h3>
                </div>
                <div class="container text-center my-3">
                    <div class="row mx-auto my-auto justify-content-center">
                        <div id="recipeCarousel" class="carousel slide" data-bs-ride="carousel">
                            <div class="carousel-inner" role="listbox">
                                <div class="carousel-item active" data-bs-interval="5000">
                                    <div class="col-md-3">
                                        <div class="card">
                                            <div class="card-img">
                                                <img src="//via.placeholder.com/500x650/31f?text=1" class="img-fluid">
                                            </div>
                                            <div class="card-img-overlay">Slide 1</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="carousel-item" data-bs-interval="5000">
                                    <div class="col-md-3">
                                        <div class="card">
                                            <div class="card-img">
                                                <img src="//via.placeholder.com/500x650/e66?text=2" class="img-fluid">
                                            </div>
                                            <div class="card-img-overlay">Slide 2</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="carousel-item" data-bs-interval="5000">
                                    <div class="col-md-3">
                                        <div class="card">
                                            <div class="card-img">
                                                <img src="//via.placeholder.com/500x650/7d2?text=3" class="img-fluid">
                                            </div>
                                            <div class="card-img-overlay">Slide 3</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="carousel-item" data-bs-interval="5000">
                                    <div class="col-md-3">
                                        <div class="card">
                                            <div class="card-img">
                                                <img src="//via.placeholder.com/500x650?text=4" class="img-fluid">
                                            </div>
                                            <div class="card-img-overlay">Slide 4</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="carousel-item" data-bs-interval="5000">
                                    <div class="col-md-3">
                                        <div class="card">
                                            <div class="card-img">
                                                <img src="//via.placeholder.com/500x650/aba?text=5" class="img-fluid">
                                            </div>
                                            <div class="card-img-overlay">Slide 5</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="carousel-item" data-bs-interval="5000">
                                    <div class="col-md-3">
                                        <div class="card">
                                            <div class="card-img">
                                                <img src="//via.placeholder.com/500x650/fc0?text=6" class="img-fluid">
                                            </div>
                                            <div class="card-img-overlay">Slide 6</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="carousel-item" data-bs-interval="5000">
                                    <div class="col-md-3">
                                        <div class="card">
                                            <div class="card-img">
                                                <img src="//via.placeholder.com/500x650/bac0?text=7" class="img-fluid">
                                            </div>
                                            <div class="card-img-overlay">Slide 7</div>
                                        </div>
                                    </div>
                                </div>
                                <div class="carousel-item" data-bs-interval="5000">
                                    <div class="col-md-3">
                                        <div class="card">
                                            <div class="card-img">
                                                <img src="//via.placeholder.com/500x650/fac0?text=8" class="img-fluid">
                                            </div>
                                            <div class="card-img-overlay">Slide 8</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <a class="carousel-control-prev bg-transparent w-aut" href="#recipeCarousel" role="button" data-bs-slide="prev">
                                <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                            </a>
                            <a class="carousel-control-next bg-transparent w-aut" href="#recipeCarousel" role="button" data-bs-slide="next">
                                <span class="carousel-control-next-icon" aria-hidden="true"></span>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </section>


        <!-- New Arrival Grid-->
        <section class="page-section bg-light" id="portfolio">
            <div class="container">
                <div class="text-center">
                    <h2 class="section-heading text-uppercase">New Arrival</h2>
                    <h3 class="section-subheading text-muted">Today Updated Clothes.</h3>
                </div>
                <c:choose>
                    <c:when test="${empty ClothesSummaryList }" >
                        <div class="text-center">
                            <h2 class="section-heading text-uppercase">Enrolled Clothes is Empty.....!</h2>
                        </div>
                    </c:when>
                    <c:when test="${!empty ClothesSummaryList}">
                        <div class="row">
                            <c:forEach var="clothes" items="${ClothesSummaryList}">
                                  <div class="col-lg-4 col-sm-6 mb-4">
                                      <!-- Portfolio item 1-->
                                      <div class="clothes-item">
                                          <a class="clothes-link" href="${pageContext.request.contextPath}/clothes/${clothes.clothesId}">
                                              <img class="img-fluid" src=<c:out value="${pageContext.request.contextPath}/image/${clothes.profileImageIds[0]}" /> alt="..." />
                                          </a>
                                          <div class="clothes-caption">
                                              <div class="clothes-caption-heading"><c:out value="${clothes.clothesName}" /></div>
                                              <div class="clothes-caption-subheading text-muted"><c:out value="${clothes.price}" /></div>
                                          </div>
                                      </div>
                                  </div>
                            </c:forEach>
                        </div>
                    </c:when>
                </c:choose>
                <div class="row">
                    <div class="col-lg-4 col-sm-6 mb-4">
                        <!-- Portfolio item 1-->
                        <div class="portfolio-item">
                            <a class="portfolio-link" data-bs-toggle="modal" href="#portfolioModal1">
                                <div class="portfolio-hover">
                                    <div class="portfolio-hover-content"><i class="fas fa-plus fa-3x"></i></div>
                                </div>
                                <img class="img-fluid" src="assets/img/portfolio/1.jpg" alt="..." />
                            </a>
                            <div class="portfolio-caption">
                                <div class="portfolio-caption-heading">Threads</div>
                                <div class="portfolio-caption-subheading text-muted">Illustration</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-4 col-sm-6 mb-4">
                        <!-- Portfolio item 2-->
                        <div class="portfolio-item">
                            <a class="portfolio-link" href="#portfolioModal2">
                                <div class="portfolio-hover">
                                    <div class="portfolio-hover-content"><i class="fas fa-plus fa-3x"></i></div>
                                </div>
                                <img class="img-fluid" src="assets/img/portfolio/2.jpg" alt="..." />
                            </a>
                            <div class="portfolio-caption">
                                <div class="portfolio-caption-heading">Explore</div>
                                <div class="portfolio-caption-subheading text-muted">Graphic Design</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-4 col-sm-6 mb-4">
                        <!-- Portfolio item 3-->
                        <div class="portfolio-item">
                            <a class="portfolio-link" data-bs-toggle="modal" href="#portfolioModal3">
                                <div class="portfolio-hover">
                                    <div class="portfolio-hover-content"><i class="fas fa-plus fa-3x"></i></div>
                                </div>
                                <img class="img-fluid" src="assets/img/portfolio/3.jpg" alt="..." />
                            </a>
                            <div class="portfolio-caption">
                                <div class="portfolio-caption-heading">Finish</div>
                                <div class="portfolio-caption-subheading text-muted">Identity</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-4 col-sm-6 mb-4 mb-lg-0">
                        <!-- Portfolio item 4-->
                        <div class="portfolio-item">
                            <a class="portfolio-link" data-bs-toggle="modal" href="#portfolioModal4">
                                <div class="portfolio-hover">
                                    <div class="portfolio-hover-content"><i class="fas fa-plus fa-3x"></i></div>
                                </div>
                                <img class="img-fluid" src="assets/img/portfolio/4.jpg" alt="..." />
                            </a>
                            <div class="portfolio-caption">
                                <div class="portfolio-caption-heading">Lines</div>
                                <div class="portfolio-caption-subheading text-muted">Branding</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-4 col-sm-6 mb-4 mb-sm-0">
                        <!-- Portfolio item 5-->
                        <div class="portfolio-item">
                            <a class="portfolio-link" data-bs-toggle="modal" href="#portfolioModal5">
                                <div class="portfolio-hover">
                                    <div class="portfolio-hover-content"><i class="fas fa-plus fa-3x"></i></div>
                                </div>
                                <img class="img-fluid" src="assets/img/portfolio/5.jpg" alt="..." />
                            </a>
                            <div class="portfolio-caption">
                                <div class="portfolio-caption-heading">Southwest</div>
                                <div class="portfolio-caption-subheading text-muted">Website Design</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-4 col-sm-6">
                        <!-- Portfolio item 6-->
                        <div class="portfolio-item">
                            <a class="portfolio-link" data-bs-toggle="modal" href="#portfolioModal6">
                                <div class="portfolio-hover">
                                    <div class="portfolio-hover-content"><i class="fas fa-plus fa-3x"></i></div>
                                </div>
                                <img class="img-fluid" src="assets/img/portfolio/6.jpg" alt="..." />
                            </a>
                            <div class="portfolio-caption">
                                <div class="portfolio-caption-heading">Window</div>
                                <div class="portfolio-caption-subheading text-muted">Photography</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <!-- Footer-->
        <footer class="footer py-4">
            <div class="container">
                <div class="row align-items-center">
                    <div class="col-lg-4 text-lg-start">Copyright &copy; Your Website 2021</div>
                    <div class="col-lg-4 my-3 my-lg-0">
                        <a class="btn btn-dark btn-social mx-2" href="#!"><i class="fab fa-twitter"></i></a>
                        <a class="btn btn-dark btn-social mx-2" href="#!"><i class="fab fa-facebook-f"></i></a>
                        <a class="btn btn-dark btn-social mx-2" href="#!"><i class="fab fa-linkedin-in"></i></a>
                    </div>
                    <div class="col-lg-4 text-lg-end">
                        <a class="link-dark text-decoration-none me-3" href="#!">Privacy Policy</a>
                        <a class="link-dark text-decoration-none" href="#!">Terms of Use</a>
                    </div>
                </div>
            </div>
        </footer>
        <!-- Portfolio Modals-->
        <!-- Portfolio item 1 modal popup-->

        <!-- Portfolio item 2 modal popup-->

        <!-- Portfolio item 3 modal popup-->

        <!-- Portfolio item 4 modal popup-->

        <!-- Portfolio item 5 modal popup-->

        <!-- Portfolio item 6 modal popup-->

        <!-- Bootstrap core JS-->
        <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js" integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.min.js" integrity="sha384-QJHtvGhmr9XOIpI6YVutG+2QOK9T+ZnN4kzFN1RtK3zEFEIsxhlmWl5/YESvpZ13" crossorigin="anonymous"></script>
        <!-- Core theme JS-->
        <script src="${pageContext.request.contextPath}/resources/common/js/indexScript.js"></script>


        <script>
            // collection section script
            let items = document.querySelectorAll('.carousel .carousel-item')

            items.forEach((el) => {
                const minPerSlide = 4
                let next = el.nextElementSibling
                for (var i=1; i<minPerSlide; i++) {
                    if (!next) {
                        // wrap carousel by using first child
                    	next = items[0]
                  	}
                    let cloneChild = next.cloneNode(true)
                    el.appendChild(cloneChild.children[0])
                    next = next.nextElementSibling
                }
            })


            // new arrival section script

        </script>
    </body>
</html>
