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
</style>
<script>
    //천단위 콤마 추가 함수.
    function addComma(value){
         value = value.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
         return value;
    }

    //천단위 콤마 제거 함수.
    function deleteComma(value){
         value = value.replace(/[^\d]+/g, "");
         return value;
    }

    // 금액, 수량 설정 함수.
    $(function(){
        var totalItemPrice = 0;      // 상품금액.
        var totalDeliveryPrice = 0;  // 배송비.
        var discountPrice = 0;       // 할인금액.
        var expectedPoint = 0;       // 적립 포인트.
        var totalPrice = 0;          // 총결제금액.

        // 장바구니 상품 수량 Map(상품별 수량 정보를 저장하는 map(key: cartItem + itekId, value:itemQuantity).
        var cartQuantityMap = new Map();
        // 장바구니 수량 Map 초기화.
        <c:if test= "${!empty cartInfo.cartItems}">
            <c:forEach var="cartItem" items="${cartInfo.cartItems}">
                cartQuantityMap.set("cartItem" + ${cartItem.value.itemId}, 0);
            </c:forEach>
        </c:if>

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
    });

    // 개별 상품 주문 함수.
    function orderCartItem(itemId){
        console.log("itemId: " + itemId);

        var url= "${pageContext.request.contextPath}/cart/" + itemId + "/order";
        location.href = url;
    }

</script>
<div class="cartContainerWraps">
    <div class="cartContainer">
       <h3 class="text-start m-2">장바구니.</h3>
       <div id="cartItemWrap">
           <c:if test= "${empty cartInfo.cartItems}">
               <p>장바구니가 비어 있습니다.</p>
           </c:if>
           <c:if test= "${!empty cartInfo.cartItems}">
                <c:forEach var="cartItem" items="${cartInfo.cartItems}">
                  <div class="cartItemWrap">
                    <div class="row cartItem border-top border-bottom m-2">
                       <div class="cartItemInfo col-sm-4 row p-0">
                           <div class="col-sm-4 py-3"><img class="p-1" src="${pageContext.request.contextPath}/image/${cartItem.value.itemProfileImg}" width="80px" height="80px"/></div>
                           <div class="col-sm-7 py-3 text-start">
                            <div>상품이름: <c:out value="${cartItem.value.itemName}" /></div>
                            <c:forEach var="requiredOption" items="${cartItem.value.requiredOptions}">
                                <div class="mt-1">- ${requiredOption.itemSize} / <span class="itemQuantity cartItem${cartItem.value.itemId}">${requiredOption.itemQuantity}</span>개</div>
                            </c:forEach>
                           </div>
                       </div>
                       <div class="cartItemInfoWrap col-sm-8 row p-0 text-start mt-1">
                           <div class="col-sm-1 p-0 py-3">수량: <span id="cartItem${cartItem.value.itemId}Quantity"></span>개</div>
                           <div class="col-sm-1 p-0 py-3" role="paragraph"></div>
                           <div class="col-sm-3 p-0 py-3">상품금액: <span id="cartItem${cartItem.value.itemId}ItemPrice" class="cartItemPrice"><fmt:formatNumber type="number" maxFractionDigits="3" value="${cartItem.value.itemPrice}" /></span>원</div>
                           <div class="col-sm-2 p-0 py-3">배송비: <span id="cartItem${cartItem.value.itemId}DeliveryPrice" class="cartItemDeliveryPrice"><fmt:formatNumber type="number" maxFractionDigits="3" value="${cartItem.value.deliveryPrice}" /></span>원</div>
                           <div class="col-sm-1 p-0 py-3"><button class="btn btn-secondary" onclick="orderCartItem(${cartItem.value.itemId})">주문</button></div>
                       </div>
                    </div>
                  </div>
                </c:forEach>
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
                        <button class="btn btn-secondary">전체 주문</button>
                    </div>
                </div>
           </c:if>
       </div>
    </div>
</div>
