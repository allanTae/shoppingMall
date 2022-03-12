<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<style>
	body{padding : 0px}
	#tile_header { width:100%; }
	#tile_body { width:100%; float:left; }
</style>

<script>
    // 선택한 카테고리 아아디.
    let selectedCategoryId = 0;

    // 조회한 상품 카테고리 정보를 저장하는 오브젝트.
    var categoryObj;

    $(function(){
        setCategory();
    });

    // 카테고리 정보 set 함수.
    function setCategory(){
        var headers = {"Content-Type" : "application/json; charset=UTF-8;"
                      , "X-HTTP-Method-Override" : "GET"};
        $.ajax({
          url: "${pageContext.request.contextPath}/category/shop"
          , headers : headers
          , type : 'GET'
          , dataType : 'json'
          , success: function(result){
            if(result.apiResultMessage === "카테고리 조회에 성공하였습니다."){
                categoryObj = result.category; // 카테고리 정보 저장.

                var htmls = '';
                var mainCategoryItemLength = Object.keys(result.category.child).length;
                if(mainCategoryItemLength > 0){
                     $.each(result.category.child, function(key, value) {
                        // clothes 상품인 경우만 select tag 에 추가합니다.
                        if(value.code === 2)
                            htmls += '<option value="' + key + '">' + value.name + '</option>';
                    });
                    $('#mainCategory').append(htmls);
                }else{
                    alert("등록 된 카테고리가 없습니다. \n 우선 카테고리를 등록 해 주세요.");
                    $('#mainCategory').html('');
                    $('#mainCategory').css('display', 'none');
                    $('#middleCategory').html('');
                    $('#middleCategory').css('display', 'none');
                    $('#subCategory').html('');
                    $('#subCategory').css('display', 'none');
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

    // 대분류 카테고리 이벤트.
    $(document).on("change", "#mainCategory", function(){
        var htmls = '<option selected>중분류</option>';
        selectedCategoryId = ($(this).val() !== "대분류") ? categoryObj.child[$(this).val()].categoryId : 0;

        // 하위(중분류) 카테고리 수.
        var midleCategoryItemLength = ($(this).val() !== "대분류") ? Object.keys(categoryObj.child[$(this).val()].child).length : 0 ;

        // 하위 카테고리가 존재한다면,
        if(midleCategoryItemLength > 0){
            $.each(categoryObj.child[$(this).val()].child, function(key, value) {
                htmls += '<option value="' + key + '">' + value.name + '</option>';
            });
            $('#middleCategory').html(htmls);
            $('#middleCategory').css('display', 'inline');
            $('#subCategory').html('');
            $('#subCategory').css('display', 'none');

        // 하위 카테고리가 없다면,
        }else{
            // 하위 카테고리 초기화.
            $('#middleCategory').html('');
            $('#middleCategory').css('display', 'none');
            $('#subCategory').html('');
            $('#subCategory').css('display', 'none');
        }
    });

    // 중분류 카테고리 이벤트.
    $(document).on("change", "#middleCategory", function(){
        var mainCategoryId = $('#mainCategory option:selected').val(); // 대분류 카테고리 아이디.
        selectedCategoryId = ($(this).val() !== "중분류") ? categoryObj.child[mainCategoryId].child[$(this).val()].categoryId : mainCategoryId;

        var htmls = '<option selected>소분류</option>';

        // 하위(소분류) 카테고리 수.
        var subCategoryItemLength =  ($(this).val() !== "중분류") ? Object.keys(categoryObj.child[mainCategoryId].child[$(this).val()].child).length : 0;

        // 하위 카테고리가 존재한다면,
        if(subCategoryItemLength > 0){
            $.each(categoryObj.child[mainCategoryId].child[$(this).val()].child, function(key, value) {
                htmls += '<option value="' + key + '">' + value.name + '</option>';
            });
            $('#subCategory').html(htmls);
            $('#subCategory').css('display', 'inline');
        }else{
            // 하위 카테고리 초기화.
            $('#subCategory').html('');
            $('#subCategory').css('display', 'none');
        }

    });

    // 소분류 카테고리 이벤트.
    $(document).on("change", "#subCategory", function(){
        var mainCategoryId = $('#mainCategory option:selected').val(); // 대분류 카테고리 아이디.
        var middleCategoryId = $('#middleCategory option:selected').val(); // 중분류 카테고리 아이디.

        selectedCategoryId = categoryObj.child[mainCategoryId].child[middleCategoryId].child[$(this).val()].categoryId;
    });

    function validateForm(){
        // 색상 유효성 검사.
        let colorCheck = /^[0-9]{1,3}$/;

        // 카테고리 유효성 검사.
        if(selectedCategoryId < 1){
            alert("카테고리를 입력 해 주세요.");
            return false;
        }

        // 색상 유효성 검사.
        if(!colorCheck.test($('#clothesColor option:selected').val())){
            alert("색상을 선택 해 주세요.");
            return false;
        }

        return true;
    }

    // 회원가입 이벤트.
    $(document).on('click', '#btnEnroll', function(e){
        if(validateForm()){
            let category = '<input type="text" name="categoryId" value="' + selectedCategoryId + '" style="display:none;"/>';
            <c:if test = "${param.mode == 'edit'}">
                let clothesId = '<input type="text" name="clothesId" value="' + ${clothesInfo.clothesId} + '" style="display:none;"/>';
                let mode = '<input type="hidden" name="mode" value="${param.mode}"/>';
                $("#clothesIdWrap").html(clothesId);
                $("#clothesIdWrap").append(mode);
            </c:if>
            $("#selectedCategoryWrap").html(category);
            $("#form").submit();
        }
    });

    // 원단 테이블 추가 버튼.
    $(document).on('click', "#btnFabricAdd", function(e){
        var fabricTableIndex = (Number($(".fabricTableContent").length) + 1);
        e.preventDefault();
        var htmls ='<tr class="fabricTableContent">';
        htmls += '<th scope="row">' + fabricTableIndex + '</th>';
        htmls += '<td>';
        htmls += '  <select class="form-select" name="itemFabrics[' + (fabricTableIndex-1)  + '].materialPart">';
        htmls += '      <option selected>원단 부의를 선택해 주세요.</option>';
        htmls += '      <option value="겉면">겉면</option>';
        htmls += '      <option value="속면">속면</option>';
        htmls += '  </select>';
        htmls += '</td>';
        htmls += '<td colspan="2"><input type="text" class="form-control" name="itemFabrics[' + (fabricTableIndex-1)  + '].materialDesc' +'" placeholder="원단 설명"></td>';
        htmls += '</tr>';
        $("#fabricTable:last-child").append(htmls);
    });

    // 디테일 테이블 추가 버튼.
    $(document).on('click', "#btnDetailAdd", function(e){
        var detailTableIndex = (Number($(".detailTableContent").length) + 1);
        e.preventDefault();
        var htmls ='<tr class="detailTableContent">';
        htmls += '<th scope="row">' + detailTableIndex + '</th>';
        htmls += '<td><input type="text" class="form-control" name="itemDetails[' + (detailTableIndex-1) + '].detailDesc' +'" placeholder="세부설명을 입력 해 주세요."></td>';
        htmls += '</tr>';
        $("#detailTable:last-child").append(htmls);
    });

    // 사이즈 테이블 추가 버튼.
    $(document).on('click', "#btnSizeAdd", function(e){
        var sizeTableIndex = (Number($(".sizeTableContent").length) + 1);
        e.preventDefault();
        var htmls ='<tr class="sizeTableContent">';
        htmls += '<th scope="row">' + sizeTableIndex + '</th>';
        htmls += '<td class="col-md-2">';
        htmls += '  <select class="form-select" name="clothesSizes[' + (sizeTableIndex-1) + '].sizeLabel">';
        htmls += '    <option selected>사이즈</option>';

        <c:forEach items="${sizeLabels}" var="sizeLabel">
        htmls += '    <option value="${sizeLabel.id}">${sizeLabel.key}</option>';
        </c:forEach>

        htmls += '  </select>';
        htmls += '</td>';
        htmls += '<td><input type="text" class="form-control" name="clothesSizes['+ (sizeTableIndex-1) + '].backLength' + '" placeholder="총장"></td>';
        htmls += '<td><input type="text" class="form-control" name="clothesSizes[' + (sizeTableIndex-1) + '].chestWidth' + '" placeholder="가슴둘레"></td>';
        htmls += '<td><input type="text" class="form-control" name="clothesSizes[' + (sizeTableIndex-1) + '].shoulderWidth' + '" placeholder="어깨넓이"></td>';
        htmls += '<td><input type="text" class="form-control" name="clothesSizes[' + (sizeTableIndex-1) + '].sleeveLength' + '" placeholder="소매길이"></td>';
        htmls += '<td><input type="text" class="form-control" name="clothesSizes[' + (sizeTableIndex-1) + '].waistWidth' + '" placeholder="허리둘레"></td>';
        htmls += '<td><input type="text" class="form-control" name="clothesSizes[' + (sizeTableIndex-1) + '].heapWidth' + '" placeholder="엉덩이"></td>';
        htmls += '<td><input type="text" class="form-control" name="clothesSizes[' + (sizeTableIndex-1) + '].bottomWidth' + '" placeholder="밑단둘레"></td>';
        htmls += '<td><input type="text" class="form-control" name="clothesSizes[' + (sizeTableIndex-1) + '].stockQuantity' + '" placeholder="수량"></td>';
        htmls += '</tr>';
        $("#sizeTable:last-child").append(htmls);
    });

    // 모델 사이즈 테이블 추가 버튼.
    $(document).on('click', "#btnModelSizeAdd", function(e){
        var modelSizeTableIndex = (Number($(".modelSizeTableContent").length) + 1);
        e.preventDefault();
        var htmls ='<tr class="modelSizeTableContent">';
        htmls += '<th scope="row">' + modelSizeTableIndex + '</th>';
        htmls += '<td><input type="text" class="form-control" name="modelSizes[' + (modelSizeTableIndex-1) + '].modelShoulderSize' + '" placeholder="모델 어깨"></td>';
        htmls += '<td><input type="text" class="form-control" name="modelSizes[' + (modelSizeTableIndex-1) + '].modelWaist' + '" placeholder="모델 허리"></td>';
        htmls += '<td><input type="text" class="form-control" name="modelSizes[' + (modelSizeTableIndex-1) + '].modelHeap' + '" placeholder="모델 엉덩이"></td>';
        htmls += '<td><input type="text" class="form-control" name="modelSizes[' + (modelSizeTableIndex-1) + '].modelHeight' + '" placeholder="모델 키"></td>';
        htmls += '<td><input type="text" class="form-control" name="modelSizes[' + (modelSizeTableIndex-1) + '].modelWeight' + '" placeholder="모델 몸무게"></td>';
        htmls += '</tr>';
        $("#modelSizeTable:last-child").append(htmls);
    });

    // 취소 버튼 이벤트.
	$(document).on('click', '#btnCancle', function(e){
		e.preventDefault();
		<c:choose>
            <c:when test="${param.mode != 'edit'}">
                location.href="${pageContext.request.contextPath}/index";
            </c:when>
            <c:when test="${param.mode == 'edit'}">
                location.href="${pageContext.request.contextPath}/manage/itemList";
            </c:when>
        </c:choose>

	});


</script>
<article>
	<div class="container clothesCardContainer col-md-10" role="main">
	    <!--form card-->
		<div class="card">
		    <c:choose>
                <c:when test="${param.mode != 'edit'}">
                    <div class="card-header">의류 상품 등록</div>
                </c:when>
                <c:when test="${param.mode == 'edit'}">
                    <div class="card-header">의류 상품 수정</div>
                </c:when>
            </c:choose>
			<div class="card-body">
				<form:form name="form" id="form" class="form-signup" role="form" modelAttribute="clothesForm" method="post" action="${pageContext.request.contextPath}/clothes/save" enctype="multipart/form-data">
					<div id="categoryWrap" class="row">
					    <label for="name" class="col-md-2 col-form-label text-md-right">카테고리</label>
                        <div class="col-md-3 pe-0">
                            <select class="form-select" data-size="5" id="mainCategory">
                                <c:choose>
                                   <c:when test="${param.mode != 'edit'}">
                                       <option selected>대분류</option>
                                   </c:when>
                                   <c:when test="${param.mode == 'edit'}">
                                       <option selected>기존 카테고리: ${clothesInfo.categoryName}</option>
                                   </c:when>
                                </c:choose>
                            </select>
                        </div>

                        <div class="col-md-3 p-0">
                            <select class="form-select" data-size="5" style="display:none;" id="middleCategory">
                            </select>
                        </div>

                        <div class="col-md-3 p-0">
                            <select class="form-select" data-size="5" style="display:none;" id="subCategory">
                                <option selected>소분류</option>
                            </select>
                        </div>

                    </div>
					<div class="row">
                        <label for="name" class="col-md-2 col-form-label text-md-right">상품명</label>
                        <div class="col-md-4">
                            <c:choose>
                                <c:when test="${param.mode != 'edit'}">
                                    <input name="name" id="name" class="form-control" placeholder="상품이름(필수입력)" />
                                </c:when>
                                <c:when test="${param.mode == 'edit'}">
                                    <input name="name" id="name" class="form-control" value="${clothesInfo.clothesName}" />
                                </c:when>
                            </c:choose>
                        </div>
                        <label for="engName" class="col-md-2 col-form-label text-md-right text-nowrap">영문 상품명</label>
                        <div class="col-md-4">
                            <c:choose>
                                <c:when test="${param.mode != 'edit'}">
                                    <form:input path="engName" id="engName" class="form-control" placeholder="영어상품이름(필수입력)" />
                                </c:when>
                                <c:when test="${param.mode == 'edit'}">
                                    <form:input path="engName" id="engName" class="form-control" value="${clothesInfo.engName}" />
                                </c:when>
                            </c:choose>
                        </div>
					</div>
					<div class="row">
						<label for="nameError" class="col-sd-2" text-md-right></label>
						<form:errors path="name" id="nameError" class="col-md-4 form-group text-left" style="font-size:15px; color:red; width=100px;"/>

                        <label for="engNameError" class="col-sd-2" text-md-right></label>
                        <form:errors path="engName" id="engNameError" class="col-md-4 form-group text-left" style="font-size:15px; color:red; width=100px;"/>
					</div>

                    <div class="row">
                        <label for="price" class="col-md-2 col-form-label text-md-right">가격</label>
                        <div class="col-md-4">
                            <c:choose>
                                <c:when test="${param.mode != 'edit'}">
                                    <form:input path="price" id="price" class="form-control" placeholder="상품 가격" />
                                </c:when>
                                <c:when test="${param.mode == 'edit'}">
                                    <form:input path="price" id="price" class="form-control" value="${clothesInfo.price}" />
                                </c:when>
                            </c:choose>
                        </div>

                        <label for="price" class="col-md-2 col-form-label text-md-right">색상</label>
                        <div class="col-md-4">
                            <select class="form-select" name="clothesColor" id="clothesColor">
                                <c:choose>
                                    <c:when test="${param.mode != 'edit'}">
                                        <option selected>색상을 선택 해 주세요.</option>
                                    </c:when>
                                    <c:when test="${param.mode == 'edit'}">
                                        <option selected>기존 색상: ${clothesInfo.color}</option>
                                    </c:when>
                                </c:choose>
                                <option value="1">RED</option>
                                <option value="2">BLUE</option>
                                <option value="3">YELLOW</option>
                                <option value="4">BLACK</option>
                                <option value="5">WHITE</option>
                                <option value="6">GRAY</option>
                            </select>
                        </div>
                    </div>

                    <div class="row">
                        <label for="priceError" class="col-sd-2" text-md-right></label>
                        <form:errors path="price" id="priceError" class="col-md-4 form-group text-left" style="font-size:15px; color:red; width=100px;"/>
                    </div>
                    <p class="m-0"><br /></p>
                    <div class="clothesInfoContainer">
                        <!-- fabricTable -->
                        <div class="row">
                            <p class="col-md-2 text-start m-0" for="fabricTable">Fabric</p>
                            <table class="table col-md-5" id="fabricTable" style="border: 1px solid black;">
                              <thead>
                                <tr >
                                  <th scope="col" class="align-middle">#</th>
                                  <th scope="col" class="align-middle">원단 부위</th>
                                  <th scope="col" class="align-middle">원단 설명</th>
                                  <th scope="col" class="align-middle"><button type="button" class="btn btn-prmary" id="btnFabricAdd">+</button></th>
                                </tr>
                              </thead>
                              <tbody class="border-0">
                                <c:choose>
                                    <c:when test="${param.mode != 'edit'}">
                                        <tr class="fabricTableContent">
                                          <th scope="row">1</th>
                                          <td>
                                            <select class="form-select" name="itemFabrics[0].materialPart">
                                                <option selected>원단 부의를 선택해 주세요.</option>
                                                <option value="겉면">겉면</option>
                                                <option value="속면">속면</option>
                                            </select>
                                          </td>
                                          <td colspan="2"><input type="text" class="form-control" name="itemFabrics[0].materialDesc" placeholder="원단 설명"></td>
                                        </tr>
                                    </c:when>
                                    <c:when test="${param.mode == 'edit'}">
                                        <c:forEach items="${clothesInfo.itemFabrics}" var="itemFabric" varStatus="status">
                                            <tr class="fabricTableContent">
                                              <th scope="row "><c:out value="${status.index + 1}" /></th>
                                              <td class="col-md-2">
                                                <select class="form-select" name="itemFabrics[<c:out value="${status.index}" />].materialPart">
                                                    <option value="${itemFabric.materialPart}">${itemFabric.materialPart}</option>
                                                </select>
                                              </td>
                                              <td colspan="2"><input type="text" class="form-control" name="itemFabrics[<c:out value="${status.index}" />].materialDesc" value="${itemFabric.materialDesc}"></td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                </c:choose>
                              </tbody>
                            </table>
                        </div>
                        <!-- end fabricTable -->

                        <!-- detailTable -->
                        <div class="form-group row">
                            <p class="col-md-2 text-start m-0">Detail</p>
                            <table class="table col-md-10" id="detailTable" style="border: 1px solid black;">
                              <thead>
                                <tr >
                                  <th scope="col" class="align-middle">#</th>
                                  <th scope="col" class="align-middle">세부 설명</th>
                                  <th scope="col" class="align-middle"><button type="button" class="btn btn-prmary" id="btnDetailAdd">+</button></th>
                                </tr>
                              </thead>
                              <tbody class="border-0">
                                <c:choose>
                                    <c:when test="${param.mode != 'edit'}">
                                        <tr class="detailTableContent">
                                          <th scope="row">1</th>
                                          <td><input type="text" class="form-control" name="itemDetails[0].detailDesc" placeholder="세부설명을 입력 해 주세요."></td>
                                        </tr>
                                    </c:when>
                                    <c:when test="${param.mode == 'edit'}">
                                        <c:forEach items="${clothesInfo.itemDetails}" var="itemDetail" varStatus="status">
                                            <tr class="detailTableContent">
                                              <th scope="row "><c:out value="${status.index + 1}" /></th>
                                              <td><input type="text" class="form-control" name="itemDetails[<c:out value="${status.index}" />].detailDesc" value="${itemDetail.detailDesc}"></td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                </c:choose>
                              </tbody>
                            </table>
                        </div>
                        <!-- end detail table -->

                        <!-- sizeTable -->
                        <div class="form-group row">
                            <p class="col-md-2 text-start m-0">Size</p>
                            <table class="table col-md-10" id="sizeTable" style="border: 1px solid black;">
                              <thead>
                                <tr >
                                  <th scope="col" class="align-middle">#</th>
                                  <th scope="col" class="align-middle">사이즈</th>
                                  <th scope="col" class="align-middle">총장</th>
                                  <th scope="col" class="align-middle">가슴둘레</th>
                                  <th scope="col" class="align-middle">어깨넓이</th>
                                  <th scope="col" class="align-middle">소매길이</th>
                                  <th scope="col" class="align-middle">허리둘레</th>
                                  <th scope="col" class="align-middle">엉덩이</th>
                                  <th scope="col" class="align-middle">밑단둘레</th>
                                  <th scope="col" class="align-middle">수량</th>
                                  <th scope="col" class="align-middle"><button type="button" class="btn btn-prmary" id="btnSizeAdd">+</button></th>
                                </tr>
                              </thead>
                              <tbody class="border-0">
                                <c:choose>
                                    <c:when test="${param.mode != 'edit'}">
                                        <tr class="sizeTableContent">
                                          <th scope="row ">1</th>
                                          <td class="col-md-2">
                                            <select class="form-select" name="clothesSizes[0].sizeLabel">
                                                <option selected>사이즈</option>
                                                <c:forEach items="${sizeLabels}" var="sizeLabel">
                                                    <option value="${sizeLabel.id}">${sizeLabel.key}</option>
                                                </c:forEach>
                                            </select>
                                          </td>
                                          <td><input type="text" class="form-control" name="clothesSizes[0].backLength" placeholder="총장"></td>
                                          <td><input type="text" class="form-control" name="clothesSizes[0].chestWidth" placeholder="가슴둘레"></td>
                                          <td><input type="text" class="form-control" name="clothesSizes[0].shoulderWidth" placeholder="어깨넓이"></td>
                                          <td><input type="text" class="form-control" name="clothesSizes[0].sleeveLength" placeholder="소매길이"></td>
                                          <td><input type="text" class="form-control" name="clothesSizes[0].waistWidth" placeholder="허리둘레"></td>
                                          <td><input type="text" class="form-control" name="clothesSizes[0].heapWidth" placeholder="엉덩이"></td>
                                          <td><input type="text" class="form-control" name="clothesSizes[0].bottomWidth" placeholder="밑단둘레"></td>
                                          <td><input type="text" class="form-control" name="clothesSizes[0].stockQuantity" placeholder="수량"></td>
                                        </tr>
                                    </c:when>
                                    <c:when test="${param.mode == 'edit'}">
                                        <c:forEach items="${clothesInfo.clothesSizes}" var="clothesSize" varStatus="status">
                                            <tr class="sizeTableContent">
                                              <th scope="row "><c:out value="${status.index + 1}" /></th>
                                              <td class="col-md-2">
                                                <select class="form-select" name="clothesSizes[<c:out value="${status.index}" />].sizeLabel">
                                                    <option value=${clothesSize.labelInfo.id}>${clothesSize.labelInfo.key}</option>
                                                </select>
                                              </td>
                                              <td><input type="text" class="form-control" name="clothesSizes[<c:out value="${status.index}" />].backLength" value="${clothesSize.backLength}"></td>
                                              <td><input type="text" class="form-control" name="clothesSizes[<c:out value="${status.index}" />].chestWidth" value="${clothesSize.chestWidth}"></td>
                                              <td><input type="text" class="form-control" name="clothesSizes[<c:out value="${status.index}" />].shoulderWidth" value="${clothesSize.shoulderWidth}"></td>
                                              <td><input type="text" class="form-control" name="clothesSizes[<c:out value="${status.index}" />].sleeveLength" value="${clothesSize.sleeveLength}"></td>
                                              <td><input type="text" class="form-control" name="clothesSizes[<c:out value="${status.index}" />].waistWidth" value="${clothesSize.waistWidth}"></td>
                                              <td><input type="text" class="form-control" name="clothesSizes[<c:out value="${status.index}" />].heapWidth" value="${clothesSize.heapWidth}"></td>
                                              <td><input type="text" class="form-control" name="clothesSizes[<c:out value="${status.index}" />].bottomWidth" value="${clothesSize.bottomWidth}"></td>
                                              <td><input type="text" class="form-control" name="clothesSizes[<c:out value="${status.index}" />].stockQuantity" value="${clothesSize.stockQuantity}"></td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                </c:choose>
                              </tbody>
                            </table>
                        </div>
                        <!-- end size table -->

                        <!-- modelSizeTable -->
                        <div class="form-group row">
                            <p class="col-md-2 text-start m-0">ModelSize</p>
                            <table class="table col-md-10" id="modelSizeTable" style="border: 1px solid black;">
                              <thead>
                                <tr >
                                  <th scope="col" class="align-middle">#</th>
                                  <th scope="col" class="align-middle">모델 어깨</th>
                                  <th scope="col" class="align-middle">모델 허리</th>
                                  <th scope="col" class="align-middle">모델 엉덩이</th>
                                  <th scope="col" class="align-middle">모델 키</th>
                                  <th scope="col" class="align-middle">모델 몸무게</th>
                                  <th scope="col" class="align-middle"><button type="button" class="btn btn-prmary" id="btnModelSizeAdd">+</button></th>
                                </tr>
                              </thead>
                              <tbody class="border-0">
                                  <c:choose>
                                    <c:when test="${param.mode != 'edit'}">
                                        <tr class="modelSizeTableContent">
                                          <th scope="row">1</th>
                                          <td><input type="text" class="form-control" name="modelSizes[0].modelShoulderSize" placeholder="모델 어깨"></td>
                                          <td><input type="text" class="form-control" name="modelSizes[0].modelWaist" placeholder="모델 허리"></td>
                                          <td><input type="text" class="form-control" name="modelSizes[0].modelHeap" placeholder="모델 엉덩이"></td>
                                          <td><input type="text" class="form-control" name="modelSizes[0].modelHeight" placeholder="모델 키"></td>
                                          <td><input type="text" class="form-control" name="modelSizes[0].modelWeight" placeholder="모델 몸무게"></td>
                                        </tr>
                                    </c:when>
                                    <c:when test="${param.mode == 'edit'}">
                                        <c:forEach items="${clothesInfo.modelSizes}" var="modelSize" varStatus="status">
                                            <tr class="modelSizeTableContent">
                                              <th scope="row "><c:out value="${status.index + 1}" /></th>
                                              <td><input type="text" class="form-control" name="modelSizes[<c:out value="${status.index}" />].modelShoulderSize" value="${modelSize.modelShoulderSize}"></td>
                                              <td><input type="text" class="form-control" name="modelSizes[<c:out value="${status.index}" />].modelWaist" value="${modelSize.modelWaist}"></td>
                                              <td><input type="text" class="form-control" name="modelSizes[<c:out value="${status.index}" />].modelHeap" value="${modelSize.modelHeap}"></td>
                                              <td><input type="text" class="form-control" name="modelSizes[<c:out value="${status.index}" />].modelHeight" value="${modelSize.modelHeight}"></td>
                                              <td><input type="text" class="form-control" name="modelSizes[<c:out value="${status.index}" />].modelWeight" value="${modelSize.modelWeight}"></td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                </c:choose>
                              </tbody>
                            </table>
                        </div>
                        <!-- end model size table-->

                        <!-- image file -->
                        <!-- 소개용 이미지 -->
                        <c:choose>
                          <c:when test="${param.mode != 'edit'}">
                            <div class="form-group row">
                                <label class="col-md-2">프로필 사진</label>
                                <div class="col-md-4">
                                    <input type="file" name="profileImageFiles" multiple="multiple" />
                                </div>
                            </div>
                            <!-- 상세설명용 이미지 -->
                            <div class="form-group row">
                                <label class="col-md-2">상세 사진</label>
                                <div class="col-md-4">
                                    <input type="file" name="detailImageFiles" multiple="multiple" />
                                </div>
                            </div>
                          </c:when>
                        </c:choose>
                        <!-- end image files -->

                    </div>
                    <div id="selectedCategoryWrap"></div>
                    <div id="clothesIdWrap"></div>
				</form:form>
			</div>
		</div>
		<!-- end form card -->

		<div style="margin-top:10px">
			<button type="button" class="btn btn-sm btn-primary" id="btnEnroll">상품등록</button>
			<button type="button" class="btn btn-sm btn-primary" id="btnCancle">취소</button>
		</div>
	</div>

</article>