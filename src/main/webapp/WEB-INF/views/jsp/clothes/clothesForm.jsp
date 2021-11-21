<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<style>
	body{padding : 0px}
	#tile_header { width:100%; }
	#tile_body { width:100%; float:left; }
</style>

<script>
    var materialTableIndex = 2;
    var detailTableIndex = 2;
    var sizeTableIndex = 2;
    var modelSizeTableIndex = 2;

    // 회원가입 이벤트.
    $(document).on('click', '#btnEnroll', function(e){
        $("#form").submit();
    });

    // 원단 테이블 추가 버튼.
    $(document).on('click', "#btnMaterialAdd", function(e){
        e.preventDefault();
        var htmls ='<tr>';
        htmls += '<th scope="row">' + materialTableIndex++ + '</th>';
        htmls += '<td><input type="email" class="form-control" id="materialPart" placeholder="원단 부위"></td>';
        htmls += '<td colspan="2"><input type="email" class="form-control" id="materialDesc" placeholder="원단 설명"></td>';
        htmls += '</tr>';
        $("#materialTable:last-child").append(htmls);
    });

    // 디테일 테이블 추가 버튼.
    $(document).on('click', "#btnDetailAdd", function(e){
        e.preventDefault();
        var htmls ='<tr>';
        htmls += '<th scope="row">' + detailTableIndex++ + '</th>';
        htmls += '<td><input type="email" class="form-control" id="materialPart" placeholder="세부설명을 입력 해 주세요."></td>';
        htmls += '</tr>';
        $("#detailTable:last-child").append(htmls);
    });

    // 사이즈 테이블 추가 버튼.
    $(document).on('click', "#btnSizeAdd", function(e){
        e.preventDefault();
        var htmls ='<tr>';
        htmls += '<th scope="row">' + sizeTableIndex++ + '</th>';
        htmls += '<td><input type="text" class="form-control" id="sizeLabel" placeholder="사이즈"></td>';
        htmls += '<td><input type="text" class="form-control" id="backLength" placeholder="총장"></td>';
        htmls += '<td><input type="text" class="form-control" id="chestWidth" placeholder="가슴둘레"></td>';
        htmls += '<td><input type="text" class="form-control" id="shoulderWidth" placeholder="어깨넓이"></td>';
        htmls += '<td><input type="text" class="form-control" id="sleeveLength" placeholder="소매길이"></td>';
        htmls += '<td><input type="text" class="form-control" id="waistWidth" placeholder="허리둘레"></td>';
        htmls += '<td><input type="text" class="form-control" id="heapWidth" placeholder="엉덩이"></td>';
        htmls += '<td><input type="text" class="form-control" id="bottomWidth" placeholder="밑단둘레"></td>';
        htmls += '</tr>';
        $("#sizeTable:last-child").append(htmls);
    });

    // 모델 사이즈 테이블 추가 버튼.
    $(document).on('click', "#btnModelSizeAdd", function(e){
        e.preventDefault();
        var htmls ='<tr>';
        htmls += '<th scope="row">' + modelSizeTableIndex++ + '</th>';
        htmls += '<td><input type="text" class="form-control" id="modelShoulderSize" placeholder="모델 어깨"></td>';
        htmls += '<td><input type="text" class="form-control" id="modelWaist" placeholder="모델 허리"></td>';
        htmls += '<td><input type="text" class="form-control" id="modelHeap" placeholder="모델 엉덩이"></td>';
        htmls += '<td><input type="text" class="form-control" id="modelHeight" placeholder="모델 키"></td>';
        htmls += '<td><input type="text" class="form-control" id="modelWeight" placeholder="모델 몸무게"></td>';
        htmls += '</tr>';
        $("#modelSizeTable:last-child").append(htmls);
    });

    // 취소 버튼 이벤트.
	$(document).on('click', '#btnCancle', function(e){
		e.preventDefault();
		location.href="${pageContext.request.contextPath}/clothes/getBoardList";
	});


</script>
<article>
	<div class="container col-md-8" role="main">
		<div class="card">
			<div class="card-header">상품 등록</div>
			<div class="card-body">
				<form:form name="form" id="form" class="form-signup" role="form" modelAttribute="clothesForm" method="post" action="${pageContext.request.contextPath}/clothes/save">
					<div class="form-group row">
						<label for="name" class="col-md-2 col-form-label text-md-right">상품명</label>
						<div class="col-md-4">
							<input name="name" id="name" class="form-control" placeholder="상품이름" />
						</div>

                        <label for="engName" class="col-md-2 col-form-label text-md-right text-nowrap">영문 상품명</label>
                        <div class="col-md-4">
                            <form:input path="engName" id="engName" class="form-control" placeholder="영어상품이름" />
                        </div>
					</div>
					<div class="form-group row">
						<label for="nameError" class="col-sd-2" text-md-right></label>
						<form:errors path="name" id="nameError" class="col-md-4 form-group text-left" style="font-size:15px; color:red; width=100px;"/>

                        <label for="engNameError" class="col-sd-2" text-md-right></label>
                        <form:errors path="engName" id="engNameError" class="col-md-4 form-group text-left" style="font-size:15px; color:red; width=100px;"/>
					</div>

                    <div class="form-group row">
                        <label for="price" class="col-md-2 col-form-label text-md-right">가격</label>
                        <div class="col-md-4">
                            <form:input path="price" id="price" class="form-control" placeholder="상품 가격" />
                        </div>

                        <label for="stockQuantity" class="col-md-2 col-form-label text-md-right">재고수량</label>
                        <div class="col-md-4">
                            <form:input path="stockQuantity" id="stockQuantity" class="form-control" placeholder="상품 재고 수량" />
                        </div>
                    </div>

                    <div class="form-group row">
                        <label for="priceError" class="col-sd-2" text-md-right></label>
                        <form:errors path="price" id="priceError" class="col-md-4 form-group text-left" style="font-size:15px; color:red; width=100px;"/>

                        <label for="stockQuantityError" class="col-sd-2" text-md-right></label>
                        <form:errors path="stockQuantity" id="stockQuantityError" class="col-md-4 form-group text-left" style="font-size:15px; color:red; width=100px;"/>
                    </div>

                    <!-- materialTable -->
					<div class="form-group row">
					    <label class="col-md-2">Fabric</label>
                        <table class="table col-md-10" id="materialTable">
                          <thead>
                            <tr >
                              <th scope="col" class="align-middle">#</th>
                              <th scope="col" class="align-middle">원단 부위</th>
                              <th scope="col" class="align-middle">원단 설명</th>
                              <th scope="col" class="align-middle"><button type="button" class="btn btn-prmary" id="btnMaterialAdd">+</button></th>
                            </tr>
                          </thead>
                          <tbody>
                            <tr>
                              <th scope="row">1</th>
                              <td><input type="text" class="form-control" name="clothesFabrics[0].materialPart" placeholder="원단 부위"></td>
                              <td colspan="2"><input type="text" class="form-control" name="clothesFabrics[0].materialDesc" placeholder="원단 설명"></td>
                            </tr>
                          </tbody>
                        </table>
                    </div>

                    <!-- detailTable -->
                    <div class="form-group row">
                        <label class="col-md-2">Detail</label>
                        <table class="table col-md-10" id="detailTable">
                          <thead>
                            <tr >
                              <th scope="col" class="align-middle">#</th>
                              <th scope="col" class="align-middle">세부 설명</th>
                              <th scope="col" class="align-middle"><button type="button" class="btn btn-prmary" id="btnDetailAdd">+</button></th>
                            </tr>
                          </thead>
                          <tbody>
                            <tr>
                              <th scope="row">1</th>
                              <td><input type="text" class="form-control" name="clothesDetails[0].detailDesc" placeholder="세부설명을 입력 해 주세요."></td>
                            </tr>
                          </tbody>
                        </table>
                    </div>

                    <!-- sizeTable -->
                    <div class="form-group row">
                        <label class="col-md-2">Size</label>
                        <table class="table col-md-10" id="sizeTable">
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
                              <th scope="col" class="align-middle"><button type="button" class="btn btn-prmary" id="btnSizeAdd">+</button></th>
                            </tr>
                          </thead>
                          <tbody>
                            <tr>
                              <th scope="row">1</th>
                              <td><input type="text" class="form-control" name="clothesSizes[0].sizeLabel" placeholder="사이즈"></td>
                              <td><input type="text" class="form-control" name="clothesSizes[0].backLength" placeholder="총장"></td>
                              <td><input type="text" class="form-control" name="clothesSizes[0].chestWidth" placeholder="가슴둘레"></td>
                              <td><input type="text" class="form-control" name="clothesSizes[0].shoulderWidth" placeholder="어깨넓이"></td>
                              <td><input type="text" class="form-control" name="clothesSizes[0].sleeveLength" placeholder="소매길이"></td>
                              <td><input type="text" class="form-control" name="clothesSizes[0].waistWidth" placeholder="허리둘레"></td>
                              <td><input type="text" class="form-control" name="clothesSizes[0].heapWidth" placeholder="엉덩이"></td>
                              <td><input type="text" class="form-control" name="clothesSizes[0].bottomWidth" placeholder="밑단둘레"></td>
                            </tr>
                          </tbody>
                        </table>
                    </div>

                    <!-- modelSizeTable -->
                    <div class="form-group row">
                        <label class="col-md-2">ModelSize</label>
                        <table class="table col-md-10" id="modelSizeTable">
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
                          <tbody>
                            <tr>
                              <th scope="row">1</th>
                              <td><input type="text" class="form-control" name="modelSize[0].modelShoulderSize" placeholder="모델 어깨"></td>
                              <td><input type="text" class="form-control" name="modelSize[0].modelWaist" placeholder="모델 허리"></td>
                              <td><input type="text" class="form-control" name="modelSize[0].modelHeap" placeholder="모델 엉덩이"></td>
                              <td><input type="text" class="form-control" name="modelSize[0].modelHeight" placeholder="모델 키"></td>
                              <td><input type="text" class="form-control" name="modelSize[0].modelWeight" placeholder="모델 몸무게"></td>
                            </tr>
                          </tbody>
                        </table>
                    </div>

				</form:form>
			</div>
		</div>
		<div style="margin-top:10px">
			<button type="button" class="btn btn-sm btn-primary" id="btnEnroll">상품등록</button>
			<button type="button" class="btn btn-sm btn-primary" id="btnCancle">취소</button>
		</div>
	</div>
</article>