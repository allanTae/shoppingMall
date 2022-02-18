<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

    <!-- Banner -->
    <header class="masthead">
        <div class="container">
            <div class="masthead-subheading">Welcome To Our TaeTae Mall!</div>
            <div class="masthead-heading text-uppercase">It's Nice To Meet You</div>
        </div>
    </header>

    <!-- Collection -->
    <section class="page-section" id="services">
        <div class="container">
            <div class="text-center">
                <h2 class="section-heading text-uppercase">Winter Season Inspiration!</h2>
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
                                            <img src="${pageContext.request.contextPath}/resources/common/img/index/carousel/caro1.jpeg" class="img-fluid" width="500" height="650"/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="carousel-item" data-bs-interval="5000">
                                <div class="col-md-3">
                                    <div class="card">
                                        <div class="card-img">
                                            <img src="${pageContext.request.contextPath}/resources/common/img/index/carousel/caro2.jpeg" width="500" height="650" class="img-fluid">
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="carousel-item" data-bs-interval="5000">
                                <div class="col-md-3">
                                    <div class="card">
                                        <div class="card-img">
                                            <img src="${pageContext.request.contextPath}/resources/common/img/index/carousel/caro3.jpeg" width="500" height="650" class="img-fluid">
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="carousel-item" data-bs-interval="5000">
                                <div class="col-md-3">
                                    <div class="card">
                                        <div class="card-img">
                                            <img src="${pageContext.request.contextPath}/resources/common/img/index/carousel/caro4.jpeg" width="500" height="650" class="img-fluid">
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="carousel-item" data-bs-interval="5000">
                                <div class="col-md-3">
                                    <div class="card">
                                        <div class="card-img">
                                            <img src="${pageContext.request.contextPath}/resources/common/img/index/carousel/caro5.jpeg" width="500" height="650" class="img-fluid">
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="carousel-item" data-bs-interval="5000">
                                <div class="col-md-3">
                                    <div class="card">
                                        <div class="card-img">
                                            <img src="${pageContext.request.contextPath}/resources/common/img/index/carousel/caro6.jpeg" width="500" height="650" class="img-fluid">
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="carousel-item" data-bs-interval="5000">
                                <div class="col-md-3">
                                    <div class="card">
                                        <div class="card-img">
                                            <img src="${pageContext.request.contextPath}/resources/common/img/index/carousel/caro7.jpeg" width="500" height="650" class="img-fluid">
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="carousel-item" data-bs-interval="5000">
                                <div class="col-md-3">
                                    <div class="card">
                                        <div class="card-img">
                                            <img src="${pageContext.request.contextPath}/resources/common/img/index/carousel/caro8.jpeg" width="500" height="650" class="img-fluid">
                                        </div>
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
                <h3 class="section-subheading text-muted">Recently Updated Clothes.</h3>
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
                                      <a class="clothes-link" href="${pageContext.request.contextPath}/clothes?clothesId=${clothes.clothesId}">
                                          <img class="img-fluid" src=<c:out value="${pageContext.request.contextPath}/image/${clothes.profileImageIds[0]}" /> alt="..." />
                                      </a>
                                      <div class="clothes-caption">
                                          <div class="clothes-caption-heading"><c:out value="${clothes.clothesName}(${clothes.clothesColor})" /></div>
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
            <div class="row col-md-8 text-start" id="companyInfoWrap">
                <p class="mb-0"><b>Company</b></p>
                <p class="mb-0">Company : TAETAE OFFICIAL | Owner : TAETAE</p>
                <p class="mb-0">[000-00-00000] On-Line Register : 2021-광주남문로-0000 | [사업자정보확인]</p>
                <p class="mb-0">Tel : 000-0000-0000 | E-mail : taetae@taetaeofficial.com</p>
                <p class="mb-0">Address : 00000 주소</p>
                <p class="mb-0">개인정보관리책임자 : TAETAE</p>
            </div>
            <br />
            <div class="row align-items-center">
                <div class="col-lg-4 text-lg-start">Copyright &copy; 2021 TaeTaeOfficial | 태태 All right reserved.</div>
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


