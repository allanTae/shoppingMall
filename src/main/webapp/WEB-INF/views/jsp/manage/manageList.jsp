<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<meta charset="UTF-8">
<c:url var="getBoardList" value="/board/getBoardList">
</c:url>
<script>
	function fn_contentView(itemId, categoryId){
		var url= "${pageContext.request.contextPath}/manage?itemId=" + itemId + "&categoryId=" + categoryId;
		location.href = url;
	}

    $(document).on('click', '#btnPrev', function(e){
        e.preventDefault();
        var url = "#";
        <c:if test="${pagination.isPrev}">
            url = "${pageContext.request.contextPath}/myOrder/list?page=${pagination.page} - 1";
        </c:if>

        location.href = url;
    });

    $(document).on('click', '#btnPage', function(e){
        e.preventDefault();
        var page = $(this).text();
        console.log("page: " + page);
        var url = "${pageContext.request.contextPath}/myOrder/list?page=" + page;

        location.href = url;
    });

    $(document).on('click', '#btnNext', function(e){
        e.preventDefault();
        var url = "#"
        <c:if test="${pagination.isNext}">
            url = "${pageContext.request.contextPath}/myOrder/list?page=${pagination.page} + 1";
        </c:if>

        location.href = url;
    });
</script>



<title>board</title>

</head>

<body>


<article>

	<!-- container {s} -->
	<div class="container itemsContainer">
		<h2>상품내역</h2>

		<!-- content-left {s} -->
		<div class="myOrderTableWrap" id="con-left">
            <c:choose>
                <c:when test="${empty itemList }" >
                    <tr><td colspan="5" align="center">데이터가 없습니다.</td></tr>
                </c:when>
                <c:when test="${!empty itemList}">
                    <c:forEach var="item" items="${itemList}">
                        <a href="#" onclick="fn_contentView('${item.itemId}', '${item.categoryId}')">
                           <div class="orderItemWrap border mb-2">
                             <div class="row orderItem com-md-12">
                                <div class="orderProfileImg col-md-2 p-0">
                                    <img class="p-1" src="${pageContext.request.contextPath}/image/${item.profileImageIds[0]}" width="90px" height="90px"/>
                                </div>
                                <div class="orderInfoWrap col-md-6 text-start mt-1">
                                    <div class="orderInfo_by_orderName_orderNumWrap">
                                        <div class="col-md-8 name">상품이름: <c:out value="${item.name}" /></div>
                                        <div class="col-md-4 price">금액: <c:out value="${item.price}" /></div>
                                        <div class="col-md-4 color">색상: <c:out value="${item.itemColor}" /></div>
                                    </div>
                                </div>
                                <div class="orderDateWrap col-md-4 mt-4">
                                    <div class="createdDate"><c:out value="${item.createdDate}"/></div>
                                </div>
                             </div>
                           </div>
                        </a>
                    </c:forEach>
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
                <c:if test="${pagination.isPrev}">
                    <button type="button" class="btn btn-dark" id="btnPrev">이전</button>
                </c:if>
                <c:forEach begin="${pagination.startPage}" end="${pagination.endPage}" var="idx">
                    <button type="button" class="btn btn-dark" id="btnPage">${idx}</button>
                </c:forEach>
                <c:if test="${pagination.isNext}">
                    <button type="button" class="btn btn-dark" id="btnNext">다음</button>
                </c:if>
		    </div>
		</div>
		<!-- pagination{e} -->

	</div>
	<!-- paginationContainer{e} -->

</article>
