<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<meta charset="UTF-8">
<c:url var="getBoardList" value="/board/getBoardList">
</c:url>
<script>
	function fn_contentView(orderNum){
		var url= "${pageContext.request.contextPath}/order/" + orderNum;
		location.href = url;
	}

    $(document).on('click', '#btnPrev', function(e){
        e.preventDefault();
        var url = "#";
        <c:if test="${pagination.isPrev}">
            url = "${pageContext.request.contextPath}/shop?categoryId=${categoryId}&page=${pagination.page} - 1";
        </c:if>
        location.href = url;
    });

    $(document).on('click', '#btnPage', function(e){
        e.preventDefault();
        var page = $(this).text();
        console.log("page: " + page);
        var url = "${pageContext.request.contextPath}/shop?categoryId=${categoryId}&page=" + page;

        location.href = url;
    });

    $(document).on('click', '#btnNext', function(e){
        e.preventDefault();
        var url = "#"
        <c:if test="${pagination.isNext}">
            url = "${pageContext.request.contextPath}/shop?categoryId=${categoryId}&page=${pagination.page} + 1";
        </c:if>

        location.href = url;
    });
</script>



<title>board</title>

</head>

<body>


<article>

	<!-- container {s} -->
	<div class="container shopContainer">

		<!-- content-left {s} -->
		<div class="itemWrap" id="con-left">
            <div class="text-start">
                <h5 class="section-subheading text-muted">${categoryName} Items <fmt:formatNumber type="number" maxFractionDigits="3" value="${itemQunatity}" /></h3>
            </div>
            <c:choose>
                <c:when test="${empty itemList }" >
                    <div class="text-center">
                        <h2 class="section-heading text-uppercase">Enrolled Item is Empty.....!</h2>
                    </div>
                </c:when>
                <c:when test="${!empty itemList}">
                    <div class="row">
                        <c:forEach var="item" items="${itemList}">
                              <div class="col-lg-4 col-sm-6 mb-4">
                                  <!-- Portfolio item 1-->
                                  <div class="item-item">
                                      <a class="item-link" href="${pageContext.request.contextPath}/item?categoryId=${item.categoryId}&clothesId=${item.itemId}">
                                          <img class="img-fluid" src=<c:out value="${pageContext.request.contextPath}/image/${item.profileImageIds[0]}" /> alt="..." />
                                      </a>
                                      <div class="item-caption">
                                          <div class="item-caption-heading"><c:out value="${item.name}(${item.itemColor})" /></div>
                                          <div class="item-caption-subheading text-muted"><c:out value="${item.price}" /></div>
                                      </div>
                                  </div>
                              </div>
                        </c:forEach>
                    </div>
                </c:when>
            </c:choose>
		</div>
		<!-- content-left {e} -->

    <p class="m-0"><br /></p>
	</div>
	<!-- container{e} -->

	<!-- paginationContainer{s} -->
    <p class="m-0"><br /></p>

	<div id="paginationContainer" class="container">

		<!-- pagination{s} -->
		<div id="paginationBoxWrap">
		    <div class="btn-group" role="group" >
                <button type="button" class="btn btn-dark" id="btnPrev">이전</button>
                <c:forEach begin="${pagination.startPage}" end="${pagination.endPage}" var="idx">
                    <button type="button" class="btn btn-dark" id="btnPage">${idx}</button>
                </c:forEach>

                <button type="button" class="btn btn-dark" id="btnNext">다음</button>
		    </div>
		</div>
		<!-- pagination{e} -->

	</div>
	<!-- paginationContainer{e} -->

</article>
