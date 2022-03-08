<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.*"%>
<style>
    .paymentInfoWrap > .paymentInfo , .totalPriceWrap{
        margin-right: 200px;
    }

    .paymentInfo > div {
        margin-top: 10px;
    }

    .paymentInfo > div:last-child {
        margin-bottom: 10px;
    }

    .paymentInfo > div

    .btn{  <!-- 모든 버튼에대한 css설정 -->
          text-decoration: none;
          font-size:2rem;
          color:white;
          padding:10px 20px 10px 20px;
          margin:20px;
          display:inline-block;
          border-radius: 10px;
          transition:all 0.1s;
          text-shadow: 0px -2px rgba(0, 0, 0, 0.44);
          font-family: 'Lobster', cursive; <!-- google font -->
        }
        .btn:active{
          transform: translateY(3px);
        }
        .btn.blue{
          background-color: #1f75d9;
          border-bottom:5px solid #165195;
        }
        .btn.blue:active{
          border-bottom:2px solid #165195;
        }
        .btn.red{
          background-color: #ff521e;
          border-bottom:5px solid #c1370e;
        }
        .btn.red:active{
          border-bottom:2px solid #c1370e;
        }

        .btn.gray{
          background-color: #1C1C1C;
          border-bottom:5px solid black;
        }
        .btn.gray:hover{
          border-bottom:2px solid black;
        }
</style>
<script>
    //천단위 콤마 추가 함수.
    function addComma(value){
         value = String(value).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
         return value;
    }

    //천단위 콤마 제거 함수.
    function deleteComma(value){
         value = String(value).replace(/[^\d]+/g, "");
         return value;
    }

    $(function(){
        showCartList();
        setPriceAndQuantity();
    });

    // 금액, 수량 설정 함수.
    function setPriceAndQuantity(){
        var totalItemPrice = 0;      // 상품금액.
        var totalDeliveryPrice = 0;  // 배송비.
        var discountPrice = 0;       // 할인금액.
        var expectedPoint = 0;       // 적립 포인트.
        var totalPrice = 0;          // 총결제금액.

        // 장바구니 상품 수량 Map(상품별 수량 정보를 저장하는 map(key: cartItem + itekId, value:itemQuantity).
        var cartQuantityMap = new Map();
        // 장바구니 수량 Map 초기화.
        $('.cartItem').each(function(index, item){
            cartQuantityMap.set($(item).attr('id'), 0);
        });

        // 장바구니 상품 수량 설정.
        $('.itemQuantity').each(function(index, item){
            var cartQuantityMapKey = $(item).attr('class').split(/\s+/)[1]; // cartQuantity 장바구니 상품의 두번째 클래스 속성으로 조회.(두번째 클래스는 cartItem + itemId 입니다.)
            cartQuantityMap.set(cartQuantityMapKey, Number(cartQuantityMap.get(cartQuantityMapKey)) + Number($(item).text()));
        });

        // 장바구니 상품별 수량 및 금액 설정.
        cartQuantityMap.forEach(function(value, key){
            $('#' + key + 'Quantity').text(Number(value));

            let cartItemPrice = Number(deleteComma($('#' + key + 'ItemPrice').text())) * value;
            $('#' + key + 'ItemPrice').text(addComma(String(cartItemPrice)));
        });

        // 상품금액 설정.
        $('.cartItemPrice').each(function(index, item) {
            totalItemPrice += Number(deleteComma($(item).text()));
        });
        $('#totalItemPrice').text(addComma(String(totalItemPrice)));

        // 배송비 설정.
        $('.cartItemDeliveryPrice').each(function(index, item){
            totalDeliveryPrice += Number(deleteComma($(item).text()));
        });
        $('#deliveryPrice').text(addComma(String(totalDeliveryPrice)));

        // 할인금액 설정.
        $('#discountPrice').text(addComma(String(discountPrice)));

        // 적립포인트 적립.
        expectedPoint = totalItemPrice * 0.1;
        $('#expectedPoint').text(addComma(String(expectedPoint)));

        // 총 결제금액 설정.
        totalPrice = totalItemPrice + totalDeliveryPrice - discountPrice;
        $('#totalPrice').text(addComma(String(totalPrice)));
    }

    // 개별 상품 주문 함수.
    function orderCartItem(itemId){

        var url= "${pageContext.request.contextPath}/cart/" + itemId + "/order";
        location.href = url;
    }

    // 전체 상품 주문 함수.
    function orderCartItems(){
        var url= "${pageContext.request.contextPath}/cart/order";
        location.href = url;
    }

</script>
<div class="cartContainerWraps">
    <div class="cartContainer">
       <h3 class="text-start m-2">장바구니.</h3>
       <div id="cartDetailWrap">
            <div id ="cartItemsWrap">

            </div>
            <div class="paymentInfoWrap border-bottom m-2 text-end">
                <div class="paymentInfo">
                    <div>상품금액: <span id="totalItemPrice"></span>원</div>
                    <div>배송비: <span id="deliveryPrice"></span>원</div>
                    <div>상품 할인금액: <span id="discountPrice"></span>원</div>
                    <div>적립 예정 포인트: <span id="expectedPoint"></span>포인트</div>
                </div>
            </div>
            <div>
                <div class="m-2 text-end">
                    <div class="totalPriceWrap">결제금액: <span id="totalPrice"></span>원</div>
                </div>
                <div>
                    <button class="btn btn-secondary" onclick="orderCartItems()">전체 주문</button>
                </div>
            </div>
       </div>
    </div>
</div>

<!-- Modal -->
<div class="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="staticBackdropLabel">옵션변경</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <div id="modalItemWrap">

        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" id="btnModalCancel" data-bs-dismiss="modal">취소</button>
        <button type="button" class="btn btn-dark" id="btnModifyCartItem">변경</button>
      </div>
    </div>
  </div>
</div>

<script>
    // 사용자가 선택(셀렉트)한 상품 필수 옵션(사이즈-수량)을 저장하기 위한 배열.
    // 셀렉트 한 상품을 등록하기 위함(동일한 상품 추가 하지 않기 위함).
    let quantityMapBySize = new Map(); // 셀렉트한 상품을 담기 위한 Map.

    // 금액 수정을 위한 변.
    let defaultAmount = 0;
    let totalAmount = 0;
    let totalOrderQuantity = 0;
    let tempSelectedItemQuantity = 0;
    let modifyCartItemId = 0;

    // 모달 취소 버튼.
    $(document).on('click', '#btnModalCancel', function(){
        resetModalFormData();
    });

    function resetModalFormData(){
        // 모달 상품 정보 초기화.
        $("#modalItemWrap").html('');

        // 수량,사이즈 map 초기화.
        quantityMapBySize = new Map();

        // 금액 정보 초기화.
        defaultAmount = 0;
        totalAmount = 0;
        totalOrderQuantity = 0;
        tempSelectedItemQuantity = 0;

        // 모달로 변경하고자 하는 장바구니 상품 도메인의 아이디.
        let modifyCartItemId = 0;
    }

    function setModalItemInfo(itemId){
        // 변경 할 아이템 아이디 저장
        modifyCartItemId = itemId;

        // 모달페이지에 item의 총수량.
        var totalCartItemQuantity = 0;

        var headers = {"Content-Type" : "application/json; charset=UTF-8;"
                  , "X-HTTP-Method-Override" : "GET"};
        $.ajax({
          url: "${pageContext.request.contextPath}/clothes/" + itemId
          , headers : headers
          , type : 'GET'
          , dataType : 'json'
          , success: function(result){
             var htmls = "";
             htmls += '<div class="modalCartItemWrap">';
             htmls += '  <div class="modalCartItemInfo row">';
             htmls += '      <div class="col-md-2">';
             htmls += '          <img class="p-1" src="${pageContext.request.contextPath}/image/' + result.clothesInfo.profileImgId + '" width="80px" height="80px"/>';
             htmls += '      </div>';
             htmls += '      <div class="col-md-8 text-start">';
             htmls += '          <p id="modal_cartItem_name">' + result.clothesInfo.clothesName + '</p>';
             htmls += '          <p><span id="modal_cartItem_price">' + result.clothesInfo.clothesPrice + '</span>원</p>';
             htmls += '      </div>';
             htmls += '  </div>';
             htmls += '  <div>';
             htmls += '      <p class="text-start mb-0">SIZE *</p>';
             htmls += '      <select class="form-select m-0" id="itemSizeSelect" aria-label="Default select example" >';
             htmls += '        <option selected>사이즈를 선택 해 주세요.(필수)</option>';
             for(var i=0; i<result.clothesInfo.sizes.length; i++){
             htmls += '        <option value="' + i+1 + '">' + result.clothesInfo.sizes[i] + '</option>';
             }
             htmls += '        <c:forEach var="size" items="${result.clothesInfo.sizes}" varStatus="index" >';
             htmls += '          <option value="${index.count}">${size.sizeLabel}</option>';
             htmls += '        </c:forEach>';
             htmls += '      </select>';
             htmls += '  </div>';
             htmls += '  <div class="selectedRequireOptionBlockWrap" id="selectedClothesArea"></div>';
             htmls += '</div>';
             htmls += '<div class="totalAmountWrap">';
             htmls += '    <div id="totalAmount">2000원</div>';
             htmls += '</div>';

             // 모달 아이템 추가.
             $("#modalItemWrap").append(htmls);

             // 상품 총 가격 계산을 위한 상품 가격 설정.
             defaultAmount = result.clothesInfo.clothesPrice;

             $('#cartItem' + itemId + ' .itemSize').each(function(index, element){
                var cartItemSize = $(this).text(); // 아이템 사이즈.
                var cartItemQuantity = Number($(this).next(".itemQuantity").text()); // 아이템 수량.

                totalCartItemQuantity += cartItemQuantity;

                // 셀렉트한 상품 맵에 추가.
                quantityMapBySize.set(cartItemSize, cartItemQuantity);

                // selected form 추가.
                addSelectedItem(cartItemSize, cartItemQuantity, (result.clothesInfo.clothesPrice*cartItemQuantity));

                // totalAmount form 추가.
                addTotalAmount(cartItemQuantity);
                totalAmountForm();
             });

          }
          , error:function(request,status,error){
            console.log("error: " + error);
          }
        });
    }

    // 셀렉트 된 상품의 수량 증가 버튼 이벤트.
    $(document).on("click", "#btnAddQuantity", function(){
        var beforeSelectedItemOrderCount = Number($(this).prev().val());
        var afterSelectedItemOrderCount = beforeSelectedItemOrderCount + 1;
        $(this).parent().next().next().text(addComma(afterSelectedItemOrderCount*defaultAmount) + "원"); // 셀렉트 된 상품의 가격을 input 태그 값 변경.

        // sizeLavel 에 해당하는 상품의 주문량 증가.
        var selectedItemSizeLabe = $(this).next().text();
        quantityMapBySize.set(selectedItemSizeLabe, quantityMapBySize.get(selectedItemSizeLabe) + 1);

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
        $(this).parent().next().next().text(addComma(afterSelectedItemOrderCount*defaultAmount) + "원"); // 셀렉트 된 상품의 가격 input 태그 값 변경.

        // sizeLabel 에 해당하는 상품의 주문량 감소.
        var selectedItemSizeLavel = $(this).next().next().next().text();
        quantityMapBySize.set(selectedItemSizeLavel, quantityMapBySize.get(selectedItemSizeLavel) - 1);

        // 총가격 수정.
        subTotalAmount(1);
        totalAmountForm();

        // 셀렉트 한 상품의 수량 text tag 변경.
        $(this).next().val(afterSelectedItemOrderCount);
    });

    // 셀렉트 된 상품의 주문량 change 이벤트.
    $(document).on("change","#selectedItemOrderQuantity",function(){
        var orderQuantity = Number($(this).val());
        if(orderQuantity === 0){
            alert("최소 1개이상은 주문해야 합니다.");
            return;
        }
        $(this).parent().next().next().text(addComma(orderQuantity*defaultAmount) + "원"); // 셀렉트 된 상품의 가격 input 태그 값 변경.

        // 셀렉트 된 상품의 전체 금액 설정 및 전체금액 폼 수정.
        if(tempSelectedItemQuantity < orderQuantity){
            addTotalAmount(orderQuantity-tempSelectedItemQuantity);
            totalAmountForm();
        }else if(tempSelectedItemQuantity > orderQuantity){
            subTotalAmount(tempSelectedItemQuantity-orderQuantity);
            totalAmountForm();
        }

        // 셀렉트
        var selectedItemSizeLabel = $(this).next().next().text();
        quantityMapBySize.set(selectedItemSizeLabel, orderQuantity);

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

    // select 된 상품의 삭제 기능을 수행합니다.
    // select 된 상품 제거, 셀렉트 된 모든 상품의 총 가격 계산등의 기능이 $(this) 를 이용하여 접근하기 때문에
    // addSelectedItem() 에서 출력하는 tag 내용에 순서가 바뀌게 된다면, 에러가 발생 할 수 있으니, 주의 해야 합니다.
    // 셀렉트 상품 삭제 event.
    $(document).on("click", "#btnSelectedItemClose", function(event){
        if($('.selectedRequireOptionBlock').length == 1){
            alert('최소 1개 이상은 주문하셔야 합니다.');
            return;
        }

        var selectedOptionInfo = $(this).next().text(); // selectedSize + selectedColor 정보를 담고있는 span tag.

        // 셀렉트 한 상품 정보를 담고 있는 맵에서 삭제.
        if(quantityMapBySize.has(selectedOptionInfo) === true){
            quantityMapBySize.set(selectedOptionInfo, 0);
            //quantityMapBySize.delete(selectedOptionInfo);
        }

        // selectItem form 제거.
        var selectedItem = $(this).parent().parent().remove();

        // totalAmount 차감 및 totalAmountform 수정.
        var seletedItemCount =  $(this).parent().next().next().children().children().next().val(); // 셀렉트한 상품의 수량 정보 input 태그 value.
        subTotalAmount(seletedItemCount);
        totalAmountForm();
    });

    // 상품 필수 옵션 체크 변수.
    let orderSizeCheck = false;

    // 의상 사이즈 select form event.
    $(document).on('change', '#itemSizeSelect', function(event){
        var selectedSize = Number($(this).val());

        // select option 값이 숫자가 아니라면,
        if(isNaN(selectedSize) === true){
            orderSizeCheck = false;
        }else{
            orderSizeCheck = true;
            checkSelectOptions();
        }
    });

    // 주문 필수 옵션 체크 및 필수옵션값 초기화 method.
    function checkSelectOptions(){
        if(orderSizeCheck === true){

            // selected text.
            var selectedSize = $("select[id=itemSizeSelect] option:selected").text();

            var selectedOptionInfo = selectedSize;
            // 셀렉트한 상품이 배열에 등록되지 않은 경우에만,
            if(quantityMapBySize.has(selectedOptionInfo) === false || quantityMapBySize.get(selectedOptionInfo) === 0){
                // 셀렉트한 상품 맵에 추가.
                quantityMapBySize.set(selectedOptionInfo, 1);

                // selected form 추가.
                addSelectedItem(selectedSize, 1, defaultAmount);

                // totalAmount form 추가.
                addTotalAmount(1);
                totalAmountForm();
            }else{
                alert("이미 등록 된 상품입니다.");
            }

            // 필수 옵션값 및 select tag option 초기화.
            $("#clothesSizeSelect option:eq(0)").prop("selected", true);
            $("#clothesColorSelect option:eq(0)").prop("selected", true);
            orderSizeCheck = false;
        }
    }

    // 셀렉트한 상품 tag 생성 method.
    // htmls 태그 내용이 수정 된다면, btnSelectedItemClose 에 click 이벤트를 처리하는 곳도 수정 해 주어야 합니다.
    function addSelectedItem(selectedSize, selectedItemOrderQuantity, itemPrice){
        var htmls = "";
        htmls += '<div class="selectedRequireOptionBlock p-4 m-3" style="background-color: rgba(79,79,79,0.03);">';
        htmls += '  <div class="sizeOptionBlock row align-center">';
        htmls += '    <span class="sizeArea col-md-3 align-text-top">' + selectedSize + '</span>';
        htmls += '    <div class="col-md-7" role="paragraph"></div>';
        htmls += '    <button id="btnSelectedItemClose" class="btn btn-outline-dark col-md-1" >X</button>';
        htmls += '    <span id="selectedOptionInfo" style="display: none;">' + selectedSize +'</span>';
        htmls += '  </div>';
        htmls += '  <hr />';
        htmls += '  <div class="colorOptionBlock row">';
        htmls += '    <div class="btn-group col-md-3">';
        htmls += '      <button class="btn btn-outline-dark border-end-0 col-md-1" id="btnSubQuantity">-</button>';
        htmls += '      <input type="text" value="' + selectedItemOrderQuantity + '" class="col-md-4 text-center border-1" id="selectedItemOrderQuantity" style="border-color: #212529";/>';
        htmls += '      <button class="btn btn-outline-dark border-start-0 col-md-1" id="btnAddQuantity">+</button>';
        htmls += '      <span id="selectedOptionInfo" style="display: none;">' + selectedSize +'</span>';
        htmls += '    </div>';
        htmls += '    <div class="col-md-5" role="paragraph"></div>';
        htmls += '    <span class="col-md-4 text-end align-center">' + addComma(itemPrice) + '원</span>';
        htmls += '  </div>';
        htmls += '</div>';
        $("#selectedClothesArea").append(htmls);
    }

    // 결제 금액 추가 메소드.
    function addTotalAmount(addQuantity){
        totalAmount = totalAmount + (defaultAmount * addQuantity);
        totalOrderQuantity = totalOrderQuantity + addQuantity;
    }

    // 결제 금액 차감 메소드.
    function subTotalAmount(substractQuantity){
        totalAmount = totalAmount - (defaultAmount * substractQuantity);
        totalOrderQuantity = totalOrderQuantity - substractQuantity;
    }

    // 총 금액 tag 생성 메소드.
    function totalAmountForm(){
        var totalAmountComma = addComma(totalAmount);
        var htmls="";
        htmls += '<div id="totalAmount">';
        htmls += '  <hr />';
        htmls += '  <div class="row">';
        htmls += '    <div class="col-md-6 text-start">총 상품 금액(<span></span>' + totalOrderQuantity +'개)</div>';
        htmls += '    <div class="col-md-6 text-end">' + totalAmountComma + '원</div>';
        htmls += '  </div>';
        htmls += '</div>';

        $("#totalAmount").html(htmls);
    }

    // 셀렉트한 상품 정보 배열을 반환하는 method.
    function getOrderQuantities(){
        var arrQuantityObj = [];
        for(var item of quantityMapBySize){
            var quantityObj = {
                size: item[0],
                quantity: item[1],
                price: item[1] * defaultAmount
            }
            arrQuantityObj.push(quantityObj);
        }
        return arrQuantityObj;
    }

    // 장바구니 변경버튼.
    $(document).on('click', "#btnModifyCartItem", function(){
      if(quantityMapBySize.size < 1){
          alert("필수 옵션을 입력 해 주세요.");
          return;
      }else{
          var cartItemList = [];
          var orderItems = getOrderQuantities();
          for(var i = 0; i<orderItems.length; i++){
              cartItemList.push({
                  "itemId": modifyCartItemId,
                  "cartQuantity": orderItems[i].quantity,
                  "size": orderItems[i].size
              });
          }

          var paramData = JSON.stringify({"cartItems": cartItemList});
          var headers = {"Content-Type" : "application/json; charset=UTF-8;"
                  , "X-HTTP-Method-Override" : "PUT"};
          $.ajax({
              url: "${pageContext.request.contextPath}/cart"
              , headers : headers
              , data : paramData
              , type : 'PUT'
              , dataType : 'json'
              , success: function(result){
                  alert(result.apiResultMessage);
                  $('#btnModalCancel').trigger('click');
                  resetModalFormData(); // 모달폼 데이터 삭제.
                  showCartList(); // 장바구니 최신화.
              }
              , error:function(request,status,error){
                  alert("장바구니에 상품을 변경하지 못했습니다.");
              }
          });
      }
    });

    function showCartList(){
        var headers = {"Content-Type" : "application/json; charset=UTF-8;"
                          , "X-HTTP-Method-Override" : "GET"};
        $.ajax({
          url: "${pageContext.request.contextPath}/cart/list"
          , headers : headers
          , type : 'GET'
          , dataType : 'json'
          , success: function(result){
                var htmls = "";
                if(!result.cartInfo || Object.getOwnPropertyNames(result.cartInfo.cartItems).length < 1){
                  htmls += "<strong> 장바구니가 비어 있습니다.</strong>";
                  $("#cartDetailWrap").html(htmls);
                } else {
                  $.each(result.cartInfo.cartItems, function(index, cartItem){
                    htmls += '<div class="cartItemWrap">';
                    htmls += '  <div class="row cartItem border-top border-bottom m-2" id="cartItem' + cartItem.itemId + '">';
                    htmls += '       <div class="cartItemInfo col-sm-4 row p-0">';
                    htmls += '           <div class="col-sm-4 py-3"><img class="p-1" src="${pageContext.request.contextPath}/image/' + cartItem.itemProfileImg +  '" width="80px" height="80px"/></div>';
                    htmls += '           <div class="col-sm-7 py-3 text-start">';
                    htmls += '             <div>상품이름: ' + cartItem.itemName + '</div>';
                    $.each(cartItem.requiredOptions, function(index, requiredOption){
                        htmls += '          <div class="mt-1">- <span class="itemSize">' + requiredOption.itemSize + '</span> / <span class="itemQuantity cartItem' + cartItem.itemId + '">' + requiredOption.itemQuantity + '</span>개</div>';
                    });
                    htmls += '           </div>';
                    htmls += '        </div>';
                    htmls += '        <div class="cartItemQuantityAndPrice col-sm-8 row p-0 text-start mt-1">';
                    htmls += '          <div class="col-sm-2 p-0 py-3">';
                    htmls += '              <div class="text-center mb-2"><span id="cartItem' + cartItem.itemId + 'Quantity"></span>개</div>';
                    htmls += '              <div align="center"><a class="p-2 btn gray text-light" data-bs-toggle="modal" data-bs-target="#staticBackdrop" onclick="setModalItemInfo(' + cartItem.itemId + ')">변경</a></div>';
                    htmls += '          </div>';
                    htmls += '          <div class="col-sm-1 p-0 py-3" role="paragraph"></div>';
                    htmls += '          <div class="col-sm-3 p-0 py-3">상품금액: <span id="cartItem' + cartItem.itemId + 'ItemPrice" class="cartItemPrice"> ' + cartItem.itemPrice + '</span>원</div>';
                    htmls += '          <div class="col-sm-2 p-0 py-3">배송비: <span id="cartItem' + cartItem.itemId + 'DeliveryPrice" class="cartItemDeliveryPrice">' + cartItem.deliveryPrice + '</span>원</div>';
                    htmls += '          <div class="col-sm-1 p-0 py-3"><button class="btn btn-secondary" onclick="orderCartItem(' + cartItem.itemId + ')">주문</button></div>';
                    htmls += '        </div>';
                    htmls += '  </div>';
                    htmls += '</div>';

                  });	//each end
                  $("#cartItemsWrap").html(htmls);
                  setPriceAndQuantity();
                }
          },	   // Ajax success end
            error:function(request,status,error){
                alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);}
        });	// Ajax end
    }
</script>