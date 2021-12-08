<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>


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
                                    <div class="text-start"><p>Size: ${orderItem.size}-${orderItem.quantity}개</p></div>
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
                        <p class="text-start mb-1">최종결제 금액</p>
                        <div class="row col-md-12">
                            <p class="col-md-4 text-start">상품 가격</p>
                            <p class="col-md-3"> </p>
                            <p class="col-md-5 text-end"><fmt:formatNumber type="number" maxFractionDigits="3" value="${orderInfo.totalAmount}" />원</p>
                        </div>
                        <hr />
                        <div class="row col-md-12">
                            <p class="col-md-6 text-start">총 결제금액(${orderInfo.totalQuantity}개)</p>
                            <p class="col-md-6 text-end"><fmt:formatNumber type="number" maxFractionDigits="3" value="${orderInfo.totalAmount}" />원</p>
                        </div>
                    </div>
                    <div class="orderButtonWrap row col-md-12 m-0">
                        <button class="btn btn-secondary">주문하기</button>
                    </div>
				</div>
            </div>
        </div>
	</div>

</article>

<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
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
                console.log("data.zonecode: " + data.zonecode);
                document.getElementById('postcode').value = data.zonecode;

                document.getElementById("address").value = addr;
                // 커서를 상세주소 필드로 이동한다.
                document.getElementById("detailAddress").focus();
            }
        }).open();
    }
</script>

<script>

  //
  $(document).on("change", "#deliveryMemo", function(){
    console.log("change event!!");
    if($(this).val() === "직접입력"){
        console.log("true!!");
        var htmls = '<input type="text" placeholder="배송메모를 입력 해 주세요." class="form-control" id="customMemo" />';
        $('#customMemoWrap').append(htmls);
    }
    console.log("val: " + $(this).val());
    console.log("text: " + $(this).text());
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

  function getOrderInfo(){

  }

  // 주문 버튼 event.
  $(document).on("click", "#btnOrder", function(){
    if(quantityMapBySize.size < 1){
        alert("필수 옵션을 입력 해 주세요.");
        return;
    }else{
        let form =$('<form action="${pageContext.request.contextPath}/order/orderForm" method="post">' +
            getOrderForm() +
            '</form>');
        $("body").append(form);
        form.submit();
    }
  });
</script>