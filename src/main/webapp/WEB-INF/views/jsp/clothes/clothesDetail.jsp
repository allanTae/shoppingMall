<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

    <div class="container itemContainerWrap col-md-12">
        <div class="itemContainer row">
            <div class="imageContainer col-md-6 m-0">
                <img class="mainProfileImage col-md-12 p-2 img-fluid" src=<c:out value="${pageContext.request.contextPath}/image/${clothesInfo.previewImages[0]}" /> alt="">
                <ul class="col-md-12 row pr-3" style="list-style: none;">
                    <c:forEach var="image" items="${clothesInfo.previewImages}" varStatus="index">
                        <li class="col-md-2 p-0" ><a><img class="col-md-11 p-0 img-fluid" src=<c:out value="${pageContext.request.contextPath}/image/${image}" /> ></a></li>
                    </c:forEach>
                </ul>
            </div>
            <div class="goodsForm col-md-6 p-4">
                <header class="pb-5 border-bottom" >
                    <div class="itemName"><c:out value="${clothesInfo.clothesName}" /></div>
                    <div class="itemPrice"><fmt:formatNumber type="number" maxFractionDigits="3" value="${clothesInfo.price}" />원</div>
                </header>
                <div class="goodsSummary">
                    <div class="pt-3">
                        <p class="m-0"><c:out value="${clothesInfo.clothesName}" /></p>
                        <p class="m-0"><c:out value="${clothesInfo.engName}" /></p>
                        <p class="m-0"><br /></p>
                        <div class="form-group row m-0">
                            <table class="table col-md-12" id="fabricTable" style="border: 1px solid black;">
                              <thead>
                                <tr>
                                  <th colspan="2" class="text-start border-bottom-1">Fabric</th>
                                </tr>
                              </thead>
                              <tbody class="border-0">
                                <c:forEach var="clothesFabric" items="${clothesInfo.clothesFabrics}" varStatus="index" >
                                    <tr>
                                        <td class="text-left" ><c:out value="${clothesFabric.materialPart}" /></td>
                                        <td class="text-left" ><c:out value="${clothesFabric.materialDesc}" /></td>
                                    </tr>
                                </c:forEach>
                              </tbody>
                            </table>
                        </div>
                        <p class="m-0"><br /></p>
                        <div class="form-group row m-0">
                            <table class="table col-md-12" id="detailTable" style="border: 1px solid black;">
                              <thead>
                                <tr>
                                  <th class="text-start border-bottom-1">Detail</th>
                                </tr>
                              </thead>
                              <tbody class="border-0">
                                <c:forEach var="clothesDetail" items="${clothesInfo.clothesDetails}" varStatus="index" >
                                    <tr>
                                        <td class="text-left" ><c:out value="${clothesDetail.detailDesc}" /></td>
                                    </tr>
                                </c:forEach>
                              </tbody>
                            </table>
                        </div>
                        <p class="m-0"><br /></p>
                        <div class="form-group row m-0">
                            <table class="table col-md-12" id="sizeTable" style="border: 1px solid black;">
                              <thead>
                                <tr>
                                  <th colspan="8" class="text-start border-bottom-1">Size</th>
                                </tr>
                              </thead>
                              <tbody class="border-0">
                                <tr>
                                    <td class="text-left" >CM</td>
                                    <td class="text-left" >총장</td>
                                    <td class="text-left" >가슴둘레</td>
                                    <td class="text-left" >어깨넓이</td>
                                    <td class="text-left" >소매</td>
                                    <td class="text-left" >허리둘레</td>
                                    <td class="text-left" >엉덩이</td>
                                    <td class="text-left" >밑단둘레</td>
                                </tr>
                                <c:forEach var="clothesSize" items="${clothesInfo.clothesSizes}" varStatus="index" >
                                    <tr>
                                        <td class="text-left" ><c:out value="${clothesSize.sizeLabel}" /></td>
                                        <td class="text-left" ><c:out value="${clothesSize.backLength}" /></td>
                                        <td class="text-left" ><c:out value="${clothesSize.chestWidth}" /></td>
                                        <td class="text-left" ><c:out value="${clothesSize.shoulderWidth}" /></td>
                                        <td class="text-left" ><c:out value="${clothesSize.sleeveLength}" /></td>
                                        <td class="text-left" ><c:out value="${clothesSize.waistWidth}" /></td>
                                        <td class="text-left" ><c:out value="${clothesSize.heapWidth}" /></td>
                                        <td class="text-left" ><c:out value="${clothesSize.bottomWidth}" /></td>
                                    </tr>
                                </c:forEach>
                              </tbody>
                            </table>
                        </div>
                        <p class="m-0"><br /></p>
                        <div class="form-group row m-0">
                            <table class="table col-md-12" id="modelSizeTable" style="border: 1px solid black;">
                              <thead>
                                <tr>
                                  <th colspan="5" class="text-start border-bottom-1">ModelSize</th>
                                </tr>
                              </thead>
                              <tbody class="border-0">
                                <tr>
                                    <td class="text-left" >어깨</td>
                                    <td class="text-left" >허리</td>
                                    <td class="text-left" >엉덩이</td>
                                    <td class="text-left" >키</td>
                                    <td class="text-left" >몸무게</td>
                                </tr>
                                <c:forEach var="modelSize" items="${clothesInfo.modelSizes}" varStatus="index" >
                                    <tr>
                                        <td class="text-left" ><c:out value="${modelSize.modelShoulderSize}" /></td>
                                        <td class="text-left" ><c:out value="${modelSize.modelWaist}" /></td>
                                        <td class="text-left" ><c:out value="${modelSize.modelHeap}" /></td>
                                        <td class="text-left" ><c:out value="${modelSize.modelHeight}" /></td>
                                        <td class="text-left" ><c:out value="${modelSize.modelWeight}" /></td>
                                    </tr>
                                </c:forEach>
                              </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <!-- end of goodsSummary -->
                <div class="chooseOption">
                    <p class="m-0"><br /></p>
                    <p class="text-start m-0">필수옵션*</p>
                    <select class="form-select m-0" id="clothesSizeSelect" aria-label="Default select example" >
                      <option selected>사이즈를 선택 해 주세요.(필수)</option>
                      <c:forEach var="clothesSize" items="${clothesInfo.clothesSizes}" varStatus="index" >
                        <option value="${index.count}">${clothesSize.sizeLabel}</option>
                      </c:forEach>
                    </select>
                    <p class="m-0"><br /></p>
                    <select class="form-select" id="clothesColorSelect" aria-label="Default select example">
                      <option selected>색상을 선택 해 주세요.(필수)</option>
                      <c:forEach var="clothesColor" items="${clothesInfo.colors}" varStatus="index" >
                        <option class="" value="${index.count}">${clothesColor.color}</option>
                      </c:forEach>
                    </select>
                </div>
                <p class="m-0"><br /></p>
                <div class="selectedRequireOptionBlockWrap" id="selectedClothesArea"></div>
                <div class="totalAmountWrap">
                    <div id="totalAmount"></div>
                </div>
                <p class="m-0"><br /></p>
                <div class="buyBtns">
                    <button type="button" class="btn btn-secondary btn-lg">구매하기</button>
                    <button type="button" class="btn btn-secondary btn-lg">장바구니</button>
                </div>
            </div>
            <!-- end of goodsForm -->
        </div>
        <div class="detailContainerWrap row">
            <div class="detailHeader col-md-12">
                <p class="m-0"><br /></p>
                <p class="text-center border-bottom">상세정보</p>
            </div>
        </div>
        <div class="imageContainerWrap row">
            <div class="detailImages col-md-12">
                <c:forEach var="image" items="${clothesInfo.detailImages}" varStatus="index">
                    <img class="mainProfileImage rounded mx-auto d-block p-3 img-fluid" src=<c:out value="${pageContext.request.contextPath}/image/${image}" /> alt="">
                </c:forEach>
            </div>
        </div>
        <div class="itemPolicyWrap row">
            <div class="deliveryPolicy col-md-12 text-start">
                <hr class="w-75 mx-auto" />
                <p class="w-75 mx-auto">배송</p>
                <ul class="w-75 mx-auto">
                    <li>상품별로 상품 특성 및 배송지에 따라 배송유형 및 소요기간이 달라집니다.</li>
                    <li>일부 주문상품 또는 예약상품의 경우 기본 배송일 외에 추가 배송 소요일이 발생 될 수 있습니다.</li>
                    <li>동일 브랜드의 상품이라도 상품별 출고일시가 달라 각각 배송 될 수 있습니다.</li>
                    <li>도서 산간 지역은 별의 배송비와 반품비가 추가 될 수 있습니다.</li>
                    <li>상품의 배송비는 공급업체의 정책에 따라 다르며 공휴일 및 휴일은 배송이 불가합니다.</li>
                </ul>
            </div>
            <p class="m-0"><br /></p>
            <div class="exchangeRefundCancelPolicy col-md-12 text-start">
                <hr class="w-75 mx-auto" />
                <p class="w-75 mx-auto">교환/환불/취소</p>
                <ul class="w-75 mx-auto">
                    <li>상품하자 이외 사이즈, 색상교환 등 단순 변심에 의한 교환/환불 택배비 고객부담으로 왕복 택배비가 발생합니다.(전자상거래 등에서의 소비자보호에 관한 법률.....)</li>
                    <li>결제완료 직후 주문상태가 주문준비중인 경우 즉시 주문취소는 My page => 취소/교환/반품 신청 에서 가능합니다.</li>
                    <li>주문완료 후 재고 부족 등으로 인해 주문 취소 처리가 될 수도 있는 점 양해 부탁드립니다.</li>
                    <li>주문상태가 상품준비중인 경우 이미 배송을 했거나 포장을 완료 했을 수 있기 때문에 직접 처리가 불가하오 고객센터를 통해 문의하시길 바랍니다.</li>
                    <li>교환 신청은 최초 1회에...</li>
                    <li>반품/교환은 미사용 제품에 한해 배송완료 후 7일 이내에 접수해 주시길 바랍니다.</li>
                </ul>
                <hr class="w-75" />
            </div>
        </div>
    </div>

    <script>
      // 최소 주문량 체크 method.
      function validateOrderCount() {
        let orderCount = parseInt(document.getElementById("orderCountInput").value);

        if(orderCount <= 0) {
            alert("주문 수량은 1개 이상이어야 합니다.");
            return false;
        }
        return true;
      }

      // 셀렉트 된 상품의 수량 증가 버튼 이벤트.
      $(document).on("click", "#btnAddQuantity", function(){
        var beforeSelectedItemOrderCount = Number($(this).prev().val());
        var afterSelectedItemOrderCount = beforeSelectedItemOrderCount + 1;
        $(this).parent().next().next().text(addComma(afterSelectedItemOrderCount*defaultAmount) + "원");
       // console.log("val: " + $(this).parent().next().next().val());

        // 총가격 수정.
        addTotalAmount(1);
        totalAmountForm();
        $(this).prev().val(afterSelectedItemOrderCount);
      });

      // 셀렉트 된 상품의 수량 감소 버튼 이벤트.
      $(document).on("click", "#btnSubQuantity", function(){
        var beforeSelectedItemOrderCount = Number($(this).next().val());
        var afterSelectedItemOrderCount = beforeSelectedItemOrderCount - 1;
        if(beforeSelectedItemOrderCount === 1){
          alert("최소 1개이상은 주문해야 합니다.");
          return;
        }
        $(this).parent().next().next().text(addComma(afterSelectedItemOrderCount*defaultAmount) + "원");
      //  console.log("val: " + $(this).parent().next().next().text());

        // 총가격 수정.
        subTotalAmount(1);
        totalAmountForm();
        $(this).next().val(afterSelectedItemOrderCount);
      });

      // 셀렉트 된 상품의 주문량 클릭 이벤트.
      $(document).on("change","#selectedItemOrderQuantity",function(){
        var orderQuantity = Number($(this).val());
        if(orderQuantity === 0){
            alert("최소 1개이상은 주문해야 합니다.");
            return;
        }
        if(tempSelectedItemQuantity < orderQuantity){
            addTotalAmount(orderQuantity-tempSelectedItemQuantity);
            totalAmountForm();
        }else if(tempSelectedItemQuantity > orderQuantity){
            subTotalAmount(tempSelectedItemQuantity-orderQuantity);
            totalAmountForm();
        }
        $(this).blur(); // enter시 blur.
      });

      // 셀렉트 된 상품의 주문량 포커스 이벤트.
      // 주문량 값이 change 되기 전 주문량을 임시저장 합니다.(임시 저장된 값은 change event 가 발생하면, 변경 된 값을 총 주문량과 총 주문가격에 반영하기 위해 사용합니다.)
      $(document).on("focus", "#selectedItemOrderQuantity",function(){
        var selectedItemOrderQuantity = Number($(this).val());

        // 셀렉트 된 상품의 주문량이 0인 경우는 주문이 되지 않도록 하기 위해서
        // 0인 경우에 주문량은 임시저장하지 않는다.
        if(selectedItemOrderQuantity !== 0 )
            tempSelectedItemQuantity = Number($(this).val());
      });

      // 사용자가 선택(셀렉트)한 상품 필수 옵션을 저장하기 위한 배열.
      // 셀렉트 한 상품을 등록하기 위함(동일한 상품 추가 하지 않기 위함).
      var arrSelectedItem = [];

      // select 된 상품의 삭제 기능을 수행합니다.
      // select 된 상품 제거, 셀렉트 된 모든 상품의 총 가격 계산등의 기능이 $(this) 를 이용하여 접근하기 때문에
      // addSelectedItem() 에서 출력하는 tag 내용에 순서가 바뀌게 된다면, 에러가 발생 할 수 있으니, 주의 해야 합니다.
      // 셀렉트 상품 삭제 event.
      $(document).on("click", "#btnSelectedItemClose", function(event){
          var selectedOptionInfo = $(this).next().text(); // selectedSize + selectedColor 정보를 담고있는 span tag.
          var isArray = jQuery.inArray(selectedOptionInfo, arrSelectedItem);
          if(isArray !== -1){
            // 필수옵션 배열에서 필수옵션 값 제거.
            arrSelectedItem.splice(isArray, 1);
          }
          // selectItem form 제거.
          var selectedItem = $(this).parent().parent().remove();

          // totalAmount 차감 및 totalAmountform 수정.
          var seletedItemCount =  $(this).parent().next().next().children().children().next().val(); // 셀렉트한 상품의 수량 정보 input 태그 value.
          subTotalAmount(seletedItemCount);
          totalAmountForm();
      });

      // 상품 필수 옵션 체크 변수.
      var orderColorCheck = false;
      var orderSizeCheck = false;

      // 의상 사이즈 select form event.
      $("#clothesSizeSelect").on("change", function(event){
        var selectedSize = Number($(this).val());
        // select option 값이 숫자가 아니라면,
        if(isNaN(selectedSize) === true){
            orderSizeCheck = false;
        }else{
            orderSizeCheck = true;
            checkSelectOptions();
        }
      });

      // 의상 색상 select form event.
      $("#clothesColorSelect").on("change", function(event){
        var selectedColor = $(this).val();
        if(isNaN(selectedColor) === true){
           orderColorCheck = false;
        }else{
            orderColorCheck = true;
            checkSelectOptions();
        }
      });

      // 주문 필수 옵션 체크 및 필수옵션값 초기화 method.
      function checkSelectOptions(){
        if(orderSizeCheck === true && orderColorCheck === true){

            // selected text.
            var selectedSize = $("select[id=clothesSizeSelect] option:selected").text();
            var selectedColor = $("select[id=clothesColorSelect] option:selected").text();

            var selectedOptionInfo = selectedSize + selectedColor;
            // 셀렉트한 상품이 배열에 등록되지 않은 경우에만,
            if(jQuery.inArray(selectedOptionInfo, arrSelectedItem) === -1){
                // 셀렉트한 상품 배열에 등록.
                arrSelectedItem.push(selectedOptionInfo);

                // selected form 추가.
                addSelectedItem(selectedSize, selectedColor, selectedOptionInfo);

                // totalAmount form 추가.
                addTotalAmount(1);
                totalAmountForm();
            }else{
                alert("이미 등록 된 상품입니다.");
            }

            // 필수 옵션값 및 select tag option 초기화.
            $("#clothesSizeSelect option:eq(0)").prop("selected", true);
            $("#clothesColorSelect option:eq(0)").prop("selected", true);
            orderColorCheck = false;
            orderSizeCheck = false;
        }
      }

      // 셀렉트한 상품 tag 생성 method.
      // htmls 태그 내용이 수정 된다면, btnSelectedItemClose 에 click 이벤트를 처리하는 곳도 수정 해 주어야 합니다.
      function addSelectedItem(selectedSize,selectedColor,selectedOptionInfo){
        var htmls = "";
        htmls += '<div class="selectedRequireOptionBlock p-4 mb-1">';
        htmls += '  <div class="sizeOptionBlock row align-center">';
        htmls += '    <span class="sizeArea col-md-3 align-text-top">' + selectedSize + '(color: ' + selectedColor + ')</span>';
        htmls += '    <div class="col-md-7" role="paragraph"></div>';
        htmls += '    <button id="btnSelectedItemClose" class="btn btn-outline-dark col-md-1" >X</button>';
        htmls += '    <span id="selectedOptionInfo" style="display=none;">' + selectedOptionInfo +'</span>'; // 셀렉트한 상품을 삭제할 때 사용하기 위한 정보.
        htmls += '  </div>';
        htmls += '  <hr />';
        htmls += '  <div class="colorOptionBlock row">';
        htmls += '    <div class="btn-group col-md-3">';
        htmls += '      <button class="btn btn-outline-dark border-end-0 col-md-1" id="btnSubQuantity">-</button>';
        htmls += '      <input type="text" value="1" class="col-md-4 text-center border-1" id="selectedItemOrderQuantity" style="border-color: #212529";/>';
        htmls += '      <button class="btn btn-outline-dark border-start-0 col-md-1" id="btnAddQuantity">+</button>';
        htmls += '    </div>';
        htmls += '    <div class="col-md-5" role="paragraph"></div>';
        htmls += '    <span class="col-md-4 text-end align-center"><fmt:formatNumber type="number" maxFractionDigits="3" value="${clothesInfo.price}" />원</span>';
        htmls += '  </div>';
        htmls += '</div>';
        $("#selectedClothesArea").append(htmls);
      }

      var defaultAmount = ${clothesInfo.price};
      var totalAmount = 0;
      var totalCount = 0;
      var tempSelectedItemQuantity = 0;

      // 결제 금액 추가 메소드.
      function addTotalAmount(addQuantity){
        totalAmount = totalAmount + (defaultAmount * addQuantity);
        totalCount = totalCount + addQuantity;
      }

      // 결제 금액 차감 메소드.
      function subTotalAmount(substractQuantity){
        totalAmount = totalAmount - (defaultAmount * substractQuantity);
        totalCount = totalCount - substractQuantity;
      }

      // 총 금액 tag 생성 메소드.
      function totalAmountForm(){
        var totalAmountComma = addComma(totalAmount);
        var htmls="";
        htmls += '<div id="totalAmount">';
        htmls += '  <hr />';
        htmls += '  <div class="row">';
        htmls += '    <div class="col-md-6 text-start">총 상품 금액(<span></span>' + totalCount +'개)</div>';
        htmls += '    <div class="col-md-6 text-end">' + totalAmountComma + '원</div>';
        htmls += '  </div>';
        htmls += '</div>';

        $("#totalAmount").html(htmls);
      }

      //천단위 콤마 펑션
      function addComma(value){
          commaValue = String(value).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
          return commaValue;
      }

    </script>