<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

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

    <script>
        // carousel collection section script
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


