<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<article>
	<div class="container orderCardContainer col-md-10" role="main">
	    <!--form card-->
		<div class="card border-0">
			<div class="card-header mb-3">주문 하기</div>
			<div class="card-body row p-0 m-0">
				<div class="orderInfoleftWrap col-md-7">
                    <div class="orderItemsWrap p-3 row mb-3">
                        <p class="text-start">주문 상품 정보</p>
                        <c:forEach var="orderItem" items="${orderInfo.orderItems}" varStatus="status">
                            <c:if test="${status.index != 0}">
                                <hr class="m-0" />
                            </c:if>
                            <div class="row col-md-12 p-3">
                                <div class="col-md-3 item_img_wrap p-0">
                                    <img class="orderItemImg" src="${pageContext.request.contextPath}/image/${orderItem.previewImg}" width="90" height="90" />
                                </div>
                                <div class="col-md-8 item_info_wrap p-0">
                                    <div class="text-start" style="font-size:16px;"><span>${orderItem.itemName}</span></div>
                                    <div class="row">
                                        <div class="col-md-5">
                                           <c:forEach var="requiredOption" items="${orderItem.requiredOptions}" varStatus="index">
                                               <div class="text-start" style="font-size:14px;"><p class="m-0">- ${requiredOption.itemSize} / ${requiredOption.itemQuantity}개</p></div>
                                           </c:forEach>
                                        </div>
                                    </div>
                                    <div class="text-start" style="font-size:14px;"><p class="m-0"><fmt:formatNumber type="number" maxFractionDigits="3" value="${orderItem.price}" />원</p></div>
                                </div>
                                <div class="col-md-1">
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <div class="ordererInfoWrap row p-3 mb-3">
                        <p class="text-start">주문자 정보</p>
                        <span><button class="btn btn-secondary" style="float:right;" onclick="getOrdererInputForm()">수정</button></span>
                        <div class="userInfoWrap col-md-12 pe-1">
                            <div class="p-0 text-start" id="userInfo_name">
                                ${userInfo.name}
                                <p class="text-danger fs-6 text-start fieldError" style="display:none;" role="error" id="ordererNameError"></p>
                            </div>
                            <div class="p-0 text-start" id="userInfo_phone">
                                ${userInfo.phone}
                                <p class="text-danger fs-6 text-start fieldError" style="display:none;" role="error" id="ordererPhoneError"></p>
                            </div>
                            <div class="p-0 text-start" id="userInfo_email">
                                ${userInfo.email}
                                <p class="text-danger fs-6 text-start fieldError" style="display:none;" role="error" id="ordererEmailError"></p>
                            </div>
                        </div>
                    </div>
                    <div class="deliveryInfoWrap row p-3 mb-3">
                        <p class="text-start">배송 정보</p>
                        <div class="col-md-12 text-start p-0">
                            <input class="form-check-input" type="checkbox" value="" id="checkOrdererInfo">
                            <label class="form-check-label" for="flexCheckDefault">주문자 정보와 동일</label>
                        </div>
                        <div class="recipientInfoWrap row col-md-12 pe-1">
                            <div class="col-md-5 p-0 mb-1">
                                <input type="text" placeholder="수령인" class="form-control" id="recipientName" />
                                <p class="text-danger fs-6 text-start fieldError" style="display:none;" role="error" id="recipientNameError"></p>
                            </div>
                            <div class="col-md-1">
                            </div>
                            <div class="col-md-5 p-0 ">
                                <input type="text" placeholder="연락처 Ex) 01012123434" class="form-control" id="recipientPhone" />
                                <p class="text-danger fs-6 text-start fieldError" style="display:none;" role="error" id="recipientPhoneError"></p>
                            </div>
                        </div>
                        <div class="postcodeWrap row col-md-12 pe-1 mb-1">
                            <div class="col-md-3 p-0 me-1">
                                <input type="text" name="postcode" placeholder="우편번호" class="form-control" id="postcode" disabled/>
                                <p class="text-danger fs-6 text-start fieldError" style="display:none;" role="error" id="postcodeError"></p>
                            </div>
                            <div class="col-md-3 p-0">
                                <button class="btn btn-secondary" onclick="getPostcode()">주소찾기</button>
                            </div>
                        </div>
                        <div class="row col-md-11 pe-1 mb-1">
                            <input type="text" placeholder="주소" class="form-control" id="address" disabled/>
                            <p class="text-danger fs-6 text-start fieldError" style="display:none;" role="error" id="addressError"></p>
                        </div>
                        <div class="row col-md-11 pe-1 mb-5">
                            <input type="text" placeholder="상세주소" class="form-control" id="detailAddress" />
                            <p class="text-danger fs-6 text-start fieldError" style="display:none;" role="error" id="detailAddressError"></p>
                        </div>

                        <p class="text-start">배송메모.</p>
                        <select class="form-select mb-1" id="deliveryMemo" name="deliveryMemo"  >
                          <option selected>배송메모를 선택 해 주세요.</option>
                          <option>배송전에 미리 연락 바랍니다.</option>
                          <option>부재시 경비실에 맡겨주세요.</option>
                          <option>부재시 전화나 메시지를 남겨 주세요.</option>
                          <option>직접입력</option>
                        </select>
                        <div class="p-0" id="customMemoWrap">

                        </div>
                    </div>
				</div>
				<div class="orderInfoRightWrap col-md-5 pe-0">
                    <div class="paymentInfoWrap p-3 mb-3">
                        <h5 class="text-start mb-1">최종결제 금액</h5>
                        <div class="row col-md-12 mb-0">
                            <p class="col-md-4 text-start mb-0">상품 가격</p>
                            <p class="col-md-3 mb-0"> </p>
                            <p class="col-md-5 mb-0 text-end"><fmt:formatNumber type="number" maxFractionDigits="3" value="${orderInfo.totalAmount}" />원</p>
                        </div>
                        <c:if test="${orderInfo.deliveryAmount >= 0}">
                             <div class="row col-md-12 mb-0" id="deliveryAmountWrap">
                               <p class="col-md-4 mb-0 text-start">배송비</p>
                               <p class="col-md-3 mb-0"> </p>
                               <p class="col-md-5 mb-0 text-end">+<fmt:formatNumber type="number" maxFractionDigits="3" value="${orderInfo.deliveryAmount}" />원</p>
                             </div>
                        </c:if>
                        <div class="row col-md-12 mb-0" id="mileageAmountWrap">
                        </div>
                        <hr />
                        <div class="row col-md-12" id="orderTotalAmountWrap">
                            <p class="col-md-6 text-start">총 결제금액(${orderInfo.totalQuantity}개)</p>
                            <p class="col-md-6 text-end" id="orderTotalAmount"><fmt:formatNumber type="number" maxFractionDigits="3" value="${orderInfo.totalAmount + orderInfo.deliveryAmount}" />원</p>
                        </div>
                    </div>
                    <div class="mileageInfoWrap p-3 mb-3 text-start">
                        <h5 class="mb-1">마일리지</h5>
                        <p></p>
                        <label for="mileage" class="mb-1 form-label">마일리지 포인트</label>
                        <div class="row col-md-12 ps-1">
                            <input type="text" class="col-md-6" id="mileagePoint" value="0"/>
                            <p class="col-sm-1 p-0" role="paragraph"></p>
                            <button class="btn btn-secondary col-md-4" id="btnMileageUse">모두 사용하기</button>
                        </div>
                    </div>
                    <div class="orderButtonWrap row col-md-12 m-0">
                        <button class="btn btn-secondary" id="btnOrder">주문하기</button>
                    </div>
				</div>
            </div>
        </div>
	</div>

</article>

<!-- sweetalert -->
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>

<script type="text/javascript">
	var paymentConfirm = function(titleText, contentText, orderResponse) {
        swal({
          title: titleText,
          text: contentText,
          icon: "warning",
          buttons: {
            cancel: "계속해서 주문",
            orderResult: {
                text: "주문결과 창으로 이동하시겠습니까.?",
                value: 1,
            },
          },
        })
        .then((value) => {
          if(value === 1){
            swal("주문결과창으로 이동합니다.", {
              icon: "success",
            });
            postOrderResultForm(orderResponse);
          }
        });
	}

	var paymentAlert = function(titleText, contentText, icon) {
	    swal({
          title: titleText,
          text: contentText,
          icon: icon,
          button: "확인",
        });
	}
</script>

<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
    let roadAddr = '';
    let jibunAddr = '';

    // daum postcode api.
    function getPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var addr = ''; // 주소 변수
                var extraAddr = ''; // 참고항목 변수

                //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    addr = data.roadAddress;
                    roadAddr = data.roadAddress;
                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    addr = data.jibunAddress;
                    jibunAddr = data.jibunAddress;
                }

                // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
                if(data.userSelectedType === 'R'){
                    // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                    // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                    if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                        extraAddr += data.bname;
                    }
                    // 건물명이 있고, 공동주택일 경우 추가한다.
                    if(data.buildingName !== '' && data.apartment === 'Y'){
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                    if(extraAddr !== ''){
                        extraAddr = ' (' + extraAddr + ')';
                    }
                    // 조합된 참고항목을 해당 필드에 넣는다.
                    addr += " 참고 주소: " + extraAddr;

                } else {
                    addr += '';
                }
                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('postcode').value = data.zonecode;
                document.getElementById("address").value = addr;
                // 커서를 상세주소 필드로 이동한다.
                document.getElementById("detailAddress").focus();
            }
        }).open();
    }
</script>


<script type="text/javascript" src="https://cdn.iamport.kr/js/iamport.payment-1.2.0.js"></script>

<script>

  // 배송메모 select event.
  $(document).on("change", "#deliveryMemo", function(){
    if($(this).val() === "직접입력"){
        var htmls = '<input type="text" placeholder="배송메모를 입력 해 주세요." class="form-control" id="customMemo" />';
        $('#customMemoWrap').append(htmls);
    }else{
        $('#customMemo').remove();
    }
  });

  // checkbox click event.
  $(document).on("click", "#checkOrdererInfo", function(){
    if($('#checkOrdererInfo').is(':checked')){
        var recipientName = $('#userInfo_name').val() === '' ? $('#userInfo_name').text() : $('#userInfo_name').val();
        var recipientPhone = $('#userInfo_phone').val() === '' ? $('#userInfo_phone').text() : $('#userInfo_phone').val();
        $('#recipientName').val(recipientName.trim());
        $('#recipientPhone').val(recipientPhone.trim());
    }else{
        $('#recipientName').val("");
        $('#recipientPhone').val("");
    }
  });

  // 주문자 정보 수정 버튼시 click 시, 주문자 정보 수정 메소드.
  function getOrdererInputForm(){
    var htmls = "";
    htmls += '<p class="text-start">주문자 정보</p>';
    htmls += '<div class="ordererNamePhoneWrap row col-md-12 pe-1">';
    htmls += '    <div class="col-md-5 p-0 mb-1">';
    htmls += '        <input type="text" name="ordererName" placeholder="이름" value="${userInfo.name}" class="form-control" id="userInfo_name" />';
    htmls += '        <p class="text-danger fs-6 text-start fieldError" style="display:none;" role="error" id="ordererNameError"></p>';
    htmls += '    </div>';
    htmls += '    <div class="col-md-1">';
    htmls += '    </div>';
    htmls += '    <div class="col-md-5 p-0 ">';
    htmls += '        <input type="text" name="ordererPhone" placeholder="연락처" value="${userInfo.phone}" class="form-control" id="userInfo_phone" />';
    htmls += '        <p class="text-danger fs-6 text-start fieldError" style="display:none;" role="error" id="ordererPhoneError"></p>';
    htmls += '    </div>';
    htmls += '</div>';
    htmls += '<div class="row col-md-11 pe-1">';
    htmls += '    <input type="text" name="ordererEmail" placeholder="이메일" value="${userInfo.email}" class="form-control" id="userInfo_email" />';
    htmls += '    <p class="text-danger fs-6 text-start fieldError" style="display:none;" role="error" id="ordererEmailError"></p>';
    htmls += '</div>';

    $(".ordererInfoWrap").html(htmls);
  }

  function checkValidation(){
     var selectedOptionIndex = $('#deliveryMemo option').index($('#deliveryMemo option:selected'));
     if(selectedOptionIndex === 0){
        paymentAlert("알림", "배송메모를 선택 해 주세요.", "warning");
        return false;
     }

     return true;
  }

  // 주문 요청 메소드.
  $(document).on('click', '#btnOrder', function(e){
  		e.preventDefault();
  		if(checkValidation() === false)
  		    return;

  		// errorFiled 안보이게 초기화.
        $('.fieldError').css("display", "none");

        var ordererName = $('#userInfo_name').val() === '' ? $('#userInfo_name').text() : $('#userInfo_name').val();
        var ordererPhone = $('#userInfo_phone').val() === '' ? $('#userInfo_phone').text().replace(/-/gi, "") : $('#userInfo_phone').val().replace(/-/gi, "");
        var ordererEmail = $('#userInfo_email').val() === '' ? $('#userInfo_email').text() : $('#userInfo_email').val();
        var recipientName = $('#recipientName').val();
        var recipientPhone = $('#recipientPhone').val().replace(/-/gi, "");
        var postcode = $('#postcode').val();
        var address = $('#address').val();
        var detailAddress = $('#detailAddress').val();
        var deliveryMemo;

        if($('#customMemo').val()===undefined){
            deliveryMemo = $('#deliveryMemo').val();
        }else{
            deliveryMemo = $('#customMemo').val();
        }

        var orderItemList = [];
        let itemId, orderQuantity, size;
        <c:forEach var="orderItem" items="${orderInfo.orderItems}" varStatus="index">
            <c:forEach var="requiredOption" items="${orderItem.requiredOptions}" varStatus="index">
                orderItemList.push({
                    "itemId": ${orderItem.itemId},
                    "orderQuantity": ${requiredOption.itemQuantity},
                    "size": "${requiredOption.itemSize}",
                    "categoryId": ${orderItem.categoryId}
                });
            </c:forEach>
        </c:forEach>

        var usedMileage = Number($('#mileagePoint').val());

        var paramData = JSON.stringify({"orderItems": orderItemList,
                                        "ordererName": ordererName,
                                        "ordererPhone": ordererPhone,
                                        "ordererEmail": ordererEmail,
                                        "recipientName": recipientName,
                                        "recipientPhone": recipientPhone,
                                        "postcode": postcode,
                                        "address": address,
                                        "roadAddress": roadAddr,
                                        "jibunAddress": jibunAddr,
                                        "detailAddress": detailAddress,
                                        "deliveryMemo": deliveryMemo,
                                        "usedMileage": usedMileage
        });

        var headers = {"Content-Type" : "application/json; charset=UTF-8;"
              , "X-HTTP-Method-Override" : "POST"};
        $.ajax({
          url: "${pageContext.request.contextPath}/order"
          , headers : headers
          , data : paramData
          , type : 'POST'
          , dataType : 'json'
          , success: function(result){
              if(result.apiResultMessage === "주문에 성공하였습니다." && result.apiResult === true){
                // 결제 요청.
                requestPay(result.orderNum);
              }else{
                // order domain create fail
                if(result.errorResponse.fieldErrors.length < 1){
                    paymentAlert("알림", result.apiResultMessage + "\n 사유: " + result.errorResponse.errMsg);
                }
                else {
                    $(result.errorResponse.fieldErrors).each(function(){
                        $('#' + this.field + 'Error').text(this.reason);
                        $('#' + this.field + 'Error').css("display", "block");
                    });	//each end
                }
              }
          }
          , error:function(request,status,error){
            console.log("error: " + error);
            paymentAlert("알림", "주문에 실패했습니다.", "warning");
          }
        });
  	});

    // 결제 요청 메소드.
  	function requestPay(orderId) {
  	      var IMP = window.IMP;
          IMP.init("imp19951233");
          var orderName = "";
          if(${fn:length(orderInfo.orderItems)} > 1){
            orderName = "${orderInfo.orderItems[0].itemName}" + "외 " + ${fn:length(orderInfo.orderItems)} +"건";
          }else{
            orderName = "${orderInfo.orderItems[0].itemName}";
          }

          var totalAmount = ${orderInfo.totalAmount} + ${orderInfo.deliveryAmount};

          totalAmount = totalAmount - Number($('#mileagePoint').val());

          // IMP.request_pay(param, callback) 결제창 호출
          IMP.request_pay({
              // param
		      pg : 'html5_inicis',
		      pay_method : 'card',
              merchant_uid: orderId,
              name: orderName,
              amount: totalAmount,
              buyer_email: $('#userInfo_email').val(),
              buyer_name: $('#userInfo_name').val(),
              buyer_tel: $('#userInfo_phone').val(),
              buyer_addr: $('#address').val() + ' ' + $('#detailAddress').val(),
              buyer_postcode: $('#postcode').val(),

              // confirm_url : "${pageContext.request.contextPath}/order/validate", // iamport 측에서 제공하는 검증 url(단, 사용하기전 iamport사측에 요청해야 함)
              m_redirect_url : '${pageContext.request.contextPath}/order/error' // 모바일버전을 위한 리다이렉트 url.
          }, function (rsp) { // callback
              // iamport 결제 응답 성공.
              if (rsp.success) {
                  var headers = {"Content-Type" : "application/json; charset=UTF-8;"
                                , "X-HTTP-Method-Override" : "POST"};
                  var paramData = JSON.stringify({"imp_uid": rsp.imp_uid,
                                                  "merchant_uid": rsp.merchant_uid
                  });
                  // 결제 성공 시 결제 유효성 검사
                   $.ajax({
                    url: "${pageContext.request.contextPath}/order/complete"
                    , headers : headers
                    , data : paramData
                    , type : 'POST'
                    , dataType : 'json'
                    , success: function(result){
                        // 결제 유효성 검사 성공
                        if(result.apiResultMessage === "결제에 성공하였습니다." && result.apiResult === true){
                            paymentAlert("알림", "주문을 성공하였습니다.", "success");
                            postOrderResultForm(result);
                        }else{
                            // 결제 유효성 검사 실패
                            paymentConfirm("알림", result.apiResultMessage + '\n' + result.errorResponse.errMsg, result);
                        }
                    }
                    , error:function(request,status,error){
                      paymentAlert("알림", "주문을 실패했습니다.", "warning");
                    }
                   });
              } else {
                  // iamport api 결제 실패 시 로직
                  var orderResponse = {
                    orderResult: "결제에 실패하였습니다.",
                    orderNum: orderId,
                    errorResponse: {
                        errorCode: rsp.error_code,
                        errMsg: rsp.error_msg
                    }
                  };
                  paymentConfirm("안내", '결제를 실패 하였습니다. \n' + rsp.error_msg, orderResponse);
              }
          });
    }

    // 주문 결과 폼 전송 메소드.
    function postOrderResultForm(orderResponse){
        let form =$('<form action="${pageContext.request.contextPath}/order/orderResult" method="post">' +
            getOrderForm(orderResponse) +
            '</form>');
        $("body").append(form);
        form.submit();
    }

    // 주문 및 결제 결과 폼 내용 생성 메소드.
    function getOrderForm(orderResponse){
        var orderFormInfo = "";
        orderFormInfo += '<input type="text" name="orderResult" value="' + orderResponse.apiResultMessage + '" />';
        orderFormInfo += '<input type="text" name="orderNum" value="' + orderResponse.orderNum + '" />';
        if(orderResponse.errorResponse !== null){
            orderFormInfo += '<input type="text" name="errorResponse.errorCode" value="' + orderResponse.errorResponse.errorCode + '" />';
            orderFormInfo += '<input type="text" name="errorResponse.errMsg" value="' + orderResponse.errorResponse.errMsg + '" />';
        }
        return orderFormInfo;
    }

    // 마일리지 버튼
    var availableMileage = ${availableMileage};
    $(document).on('click', '#btnMileageUse', function(e){
        $('#mileagePoint').val(availableMileage).trigger('change');
    });

    // 셀렉트 된 상품의 주문량 change 이벤트.
    $(document).on("change","#mileagePoint",function(){
        // 적용가능한 마일리지 계산.
        var mileage = Number($(this).val()) > ${orderInfo.totalAmount} ? ${orderInfo.totalAmount}:Number($(this).val());
        $(this).val(mileage);

        var orderTotalAmount = ${orderInfo.totalAmount + orderInfo.deliveryAmount} - mileage;

        $('#orderTotalAmount').text(orderTotalAmount.toLocaleString('ko-KR') + '원');

        if(mileage > availableMileage){
            paymentAlert("알림", "최대로 사용 할 수 있는 포인트는 " + availableMileage + "입니다.", "warning");
            $(this).val(availableMileage).trigger('change');
            return;
        }else if( mileage < 0 ){
            paymentAlert("알림", "최소 1포인트 이상부터 사용이 가능합니다.", "warning");
            $(this).val(0).trigger('change');
            return;
        }

        if(mileage > 0){
            var htmls = '';
            htmls += '<p class="col-md-4 mb-0 text-start">마일리지</p>';
            htmls += '<p class="col-md-3 mb-0"> </p>';
            htmls += '<p class="col-md-5 mb-0 text-end">-' + mileage.toLocaleString('ko-KR') + '원</p>';

            $('#mileageAmountWrap').html(htmls);
        }else{
            var htmls = '';
            $('#mileageAmountWrap').html(htmls);
        }

        $(this).blur(); // enter시 blur.
    });

    // 전화번호 하이픈 추가 함수.
    function formatPhone(number){
         if (phoneNumber.length() == 8) {
            return phoneNumber.replace("^([0-9]{4})([0-9]{4})$", "$1-$2");
         } else if (phoneNumber.length() == 12) {
            return phoneNumber.replace("(^[0-9]{4})([0-9]{4})([0-9]{4})$", "$1-$2-$3");
         }
         return phoneNumber.replace("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
    }

     // 배송 받는사람 전화 번호 키 입력시 이벤트.
     $("#recipientPhone").on("keyup", function(){
        $(this).val(phoneNumber($(this).val()));
     });

     function phoneNumber(value) {
       if (!value) {
         return "";
       }

       value = value.replace(/[^0-9]/g, "");

       let result = [];
       let restNumber = "";

       // 지역번호와 나머지 번호로 나누기
       if (value.startsWith("02")) {
         // 서울 02 지역번호
         result.push(value.substr(0, 2));
         restNumber = value.substring(2);
       } else if (value.startsWith("1")) {
         // 지역 번호가 없는 경우
         // 1xxx-yyyy
         restNumber = value;
       } else {
         // 나머지 3자리 지역번호
         // 0xx-yyyy-zzzz
         result.push(value.substr(0, 3));
         restNumber = value.substring(3);
       }

       if (restNumber.length === 7) {
         // 7자리만 남았을 때는 xxx-yyyy
         result.push(restNumber.substring(0, 3));
         result.push(restNumber.substring(3));
       } else {
         result.push(restNumber.substring(0, 4));
         result.push(restNumber.substring(4));
       }

       return result.filter((val) => val).join("-");
     }

</script>