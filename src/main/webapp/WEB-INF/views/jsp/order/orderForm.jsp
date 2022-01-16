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
                        <c:forEach var="orderItem" items="${orderInfo.orderItems}" varStatus="index">
                            <div class="row col-md-12 p-0">
                                <p class="text-start">주문 상품 정보</p>
                                <div class="col-md-4 item_img_wrap p-0">
                                    <img class="orderItemImg" src="${pageContext.request.contextPath}/image/${orderItem.previewImg}" width="90" height="90" />
                                </div>
                                <div class="col-md-8 item_info_wrap p-0">
                                    <div class="text-start"><span>${orderItem.itemName}</span></div>
                                    <div class="text-start"><p>Size: ${orderItem.size}-${orderItem.orderQuantity}개</p></div>
                                    <div class="text-start"><p><fmt:formatNumber type="number" maxFractionDigits="3" value="${orderItem.price}" />원</p></div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <div class="ordererInfoWrap row p-3 mb-3">
                        <p class="text-start">주문자 정보</p>
                        <span><button class="btn btn-secondary" style="float:right;" onclick="getOrdererInputForm()">수정</button></span>
                        <div class="userInfoWrap col-md-12 pe-1">
                            <div class="p-0 text-start" id="userInfo_name">${userInfo.name}</div>
                            <div class="p-0 text-start" id="userInfo_phone">${userInfo.phone}</div>
                            <div class="p-0 text-start" id="userInfo_email">${userInfo.email}</div>
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
                            </div>
                            <div class="col-md-1">
                            </div>
                            <div class="col-md-5 p-0 ">
                                <input type="text" placeholder="연락처" class="form-control" id="recipientPhone" />
                            </div>
                        </div>
                        <div class="postcodeWrap row col-md-12 pe-1 mb-1">
                            <div class="col-md-3 p-0 me-1">
                                <input type="text" name="postcode" placeholder="우편번호" class="form-control" id="postcode" />
                            </div>
                            <div class="col-md-3 p-0">
                                <button class="btn btn-secondary" onclick="getPostcode()">주소찾기</button>
                            </div>
                        </div>
                        <div class="row col-md-11 pe-1 mb-1">
                            <input type="text" placeholder="주소" class="form-control" id="address" />
                        </div>
                        <div class="row col-md-11 pe-1 mb-5">
                            <input type="text" placeholder="상세주소" class="form-control" id="detailAddress" />
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
                        <c:if test="${orderInfo.isDeliveryFree eq false}">
                             <div class="row col-md-12 mb-0" id="deliveryAmountWrap">
                               <p class="col-md-4 mb-0 text-start">배송비</p>
                               <p class="col-md-3 mb-0"> </p>
                               <p class="col-md-5 mb-0 text-end">+<fmt:formatNumber type="number" maxFractionDigits="3" value="${orderInfo.deliveryAmount}" />원</p>
                             </div>
                        </c:if>
                        <hr />
                        <div class="row col-md-12">
                            <p class="col-md-6 text-start">총 결제금액(${orderInfo.totalQuantity}개)</p>
                            <c:choose>
                                <c:when test="${orderInfo.isDeliveryFree eq false}">
                                    <p class="col-md-6 text-end"><fmt:formatNumber type="number" maxFractionDigits="3" value="${orderInfo.totalAmount + orderInfo.deliveryAmount}" />원</p>
                                </c:when>
                                <c:when test="${orderInfo.isDeliveryFree eq true}">
                                    <p class="col-md-6 text-end"><fmt:formatNumber type="number" maxFractionDigits="3" value="${orderInfo.totalAmount}" />원</p>
                                </c:when>
                            </c:choose>

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
                value: "orderResult",
            },
          },
        })
        .then((value) => {
          if(value === "orderResult"){
            swal("주문결과창으로 이동합니다.", {
              icon: "success",
            });
            postOrderResultForm(orderResponse);
          }
        });
	}
</script>

<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
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
                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    addr = data.jibunAddress;
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
        $('#recipientName').val($('#userInfo_name').text());
        $('#recipientPhone').val($('#userInfo_phone').text());
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
    htmls += '    </div>';
    htmls += '    <div class="col-md-1">';
    htmls += '    </div>';
    htmls += '    <div class="col-md-5 p-0 ">';
    htmls += '        <input type="text" name="ordererPhone" placeholder="연락처" value="${userInfo.phone}" class="form-control" id="userInfo_phone" />';
    htmls += '    </div>';
    htmls += '</div>';
    htmls += '<div class="row col-md-11 pe-1">';
    htmls += '    <input type="text" name="ordererEmail" placeholder="이메일" value="${userInfo.email}" class="form-control" id="userInfo_email" />';
    htmls += '</div>';

    $(".ordererInfoWrap").html(htmls);
  }

  function checkValidation(){
     var selectedOptionIndex = $('#deliveryMemo option').index($('#deliveryMemo option:selected'));
     if(selectedOptionIndex === 0){
        alert("배송메모를 선택 해 주세요.");
        return false;
     }

     return true;
  }

  // 주문 요청 메소드.
  $(document).on('click', '#btnOrder', function(e){
  		e.preventDefault();

        var ordererName = $('#userInfo_name').val();
        var ordererPhone = $('#userInfo_phone').val();
        var ordererEmail = $('#userInfo_email').val();

        var recipient = $('#recipientName').val();
        var recipientPhone = $('#recipientPhone').val();
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
        <c:forEach var="orderItem" items="${orderInfo.orderItems}" varStatus="index">
            orderItemList.push({
                "itemId": ${orderItem.itemId},
                "orderQuantity": ${orderItem.orderQuantity},
                "size": "${orderItem.size}"
            });
        </c:forEach>

        var usedMileage = Number($('#mileagePoint').val());

        var paramData = JSON.stringify({"orderItems": orderItemList,
                                        "ordererName": ordererName,
                                        "ordererPhone": ordererPhone,
                                        "ordererEmail": ordererEmail,
                                        "recipient": recipient,
                                        "recipientPhone": recipientPhone,
                                        "postcode": postcode,
                                        "address": address,
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
              // order domain create success
              console.log("message: " + result.message);
              console.log("orderNum: " + result.orderNum);
              requestPay(result.orderNum);
          }
          , error:function(request,status,error){
            console.log("error: " + error);
            alert("주문에 실패했습니다.");
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

          var totalAmount = ${orderInfo.totalAmount};
          // 배송비.
          if(!${orderInfo.isDeliveryFree}){
            totalAmount = totalAmount + ${orderInfo.deliveryAmount} ;
          }
          totalAmount = totalAmount - Number($('#mileagePoint').val());

          console.log("총 결제 금액: " + totalAmount);
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
              m_redirect_url : '${pageContext.request.contextPath}/order/orderForm' // 모바일버전을 위한 리다이렉트 url.
          }, function (rsp) { // callback
              // iamport 결제 응답 성공.
              if (rsp.success) {
                  var headers = {"Content-Type" : "application/json; charset=UTF-8;"
                                , "X-HTTP-Method-Override" : "POST"};
                  var paramData = JSON.stringify({"imp_uid": rsp.imp_uid,
                                                  "merchant_uid": rsp.merchant_uid
                  });
                  console.log("결제 성공");
                  // 결제 성공 시 결제 유효성 검사
                   $.ajax({
                    url: "${pageContext.request.contextPath}/order/complete"
                    , headers : headers
                    , data : paramData
                    , type : 'POST'
                    , dataType : 'json'
                    , success: function(result){
                        // 결제 유효성 검사 성공
                        if(result.orderResult === "결제 성공"){
                            alert("주문을 성공하였습니다.");
                            postOrderResultForm(result);
                        }else{
                            // 결제 유효성 검사 실패
                            paymentConfirm("결제결과 안내", result.orderResult + '하였습니다. \n' + result.errorResponse.errMsg, result);
                        }
                    }
                    , error:function(request,status,error){
                      alert("주문을 실패했습니다.");
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
                  paymentConfirm("결제결과 안내", '결제를 실패 하였습니다. \n' + rsp.error_msg, orderResponse);
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
        orderFormInfo += '<input type="text" name="orderResult" value="' + orderResponse.orderResult + '" />';
        orderFormInfo += '<input type="text" name="orderNum" value="' + orderResponse.orderNum + '" />';
        if(orderResponse.errorResponse !== null){
            console.log("getOrderForm() orderResponse.errorResponse: " + orderResponse.errorResponse);
            orderFormInfo += '<input type="text" name="orderErrorResponse.errorCode" value="' + orderResponse.errorResponse.errorCode + '" />';
            orderFormInfo += '<input type="text" name="orderErrorResponse.errMsg" value="' + orderResponse.errorResponse.errMsg + '" />';
        }
        console.log(orderFormInfo);
        return orderFormInfo;
    }

    // 마일리지 버튼
    var availableMileage = ${availableMileage};
    $(document).on('click', '#btnMileageUse', function(e){
        console.log("mileage text: " + availableMileage);
        $('#mileagePoint').val(availableMileage)
    });

    // 셀렉트 된 상품의 주문량 change 이벤트.
    $(document).on("change","#mileagePoint",function(){
        var mileage = Number($(this).val());
        console.log("mileage: " + mileage);
        if(mileage > availableMileage){
            alert("최대로 사용 할 수 있는 포인트는 " + availableMileage + "입니다.");
            $(this).val(availableMileage)
        }

        $(this).blur(); // enter시 blur.
    });

</script>