<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<article>
	<div class="container orderDetailContainer col-md-10" role="main">
	   <h2 class="text-start">주문 / 배송조회</h2>
	   <div class="text-start">
	     <span>주문일자: ${orderInfo.orderDate} </span> <span class="ms-2">주문번호: ${orderInfo.orderId}</span>
	   </div>
	   <div class="orderItemsWrap mb-5">
            <table class="table-responsive col-md-12" >
                <colgroup class="border-top-2">
                    <col class="col-md-7" />
                    <col class="col-sm-1" />
                    <col class="col-sm-1" />
                    <col class="col-sm-1" />
                    <col class="col-sm-1" />
                    <col class="col-sm-3" />
                </colgroup>
                <thead class="border-top-2">
                    <tr style="border-top: 2px solid black; border-bottom: 2px solid #c9c9c9;">
                      <th scope="col" class="align-middle p-2">상품(${fn:length(orderInfo.orderItems)})</th>
                      <th scope="col" class="align-middle ">주문상태</th>
                      <th scope="col" class="align-middle ">수량</th>
                      <th scope="col" class="align-middle ">판매가</th>
                      <th scope="col" class="align-middle ">할인</th>
                      <th scope="col" class="align-middle ">할인적용금액</th>
                    </tr>
                </thead>
                <tbody class="border-0">
                    <c:forEach var="orderItem" items="${orderInfo.orderItems}" varStatus="index" >
                        <tr style="border-bottom: 2px solid black; border-bottom: 2px solid #c9c9c9;">
                            <td>
                                <div class="itemInfoImageWrap row col-md-12 m-0">
                                    <div class="itemImgWrap col-md-2 p-0 m-1">
                                        <img src="${pageContext.request.contextPath}/image/${orderItem.profileImg}" class="col-md-8" />
                                    </div>
                                    <div class="itemInfoWrap col-md-8 p-0 text-start">
                                        <p class="m-0">${orderItem.name}</p>
                                        <p class="m-0">사이즈: ${orderItem.size}</p>
                                        <p class="m-0">색상: ${orderItem.color}</p>
                                    </div>
                                </div>
                            </td>
                            <td>${orderInfo.orderStatus}</td>
                            <td>${orderItem.orderQuantity}</td>
                            <td><fmt:formatNumber type="number" maxFractionDigits="3" value="${orderItem.price}" />원</td>
                            <td>${orderItem.discountName}</td>
                            <td><fmt:formatNumber type="number" maxFractionDigits="3" value="${orderItem.price - orderItem.discountPrice}" />원</td>
                        </tr>
                    </c:forEach>

                </tbody>
            </table>
	   </div>
	   <!-- end of orderItemsWrap -->

       <h5 class="text-start">배송지 정보</h5>
	   <div class="deliveryInfoWrap row col-md-12 m-0 mb-5" style="border-top: 2px solid black; border-bottom: 2px solid #c9c9c9;">
           <div class="deliveryInfoHead col-md-1 p-2 text-start">
            <p class="mb-1">수령인</p>
            <p class="mb-1">배송지 주소</p>
            <p class="mb-1">연락처</p>
            <p class="mb-1">배송메모</p>
           </div>
           <div class="deliveryInfoBody col-md-6 p-2 text-start bg-white">
            <p class="mb-1">${orderInfo.recipient}</p>
            <p class="mb-1">${orderInfo.address}</p>
            <p class="mb-1">${orderInfo.recipientPhone}</p>
            <p class="mb-1">${orderInfo.deliveryMemo}</p>
           </div>
           <div class="ordererInfo col-md-4 p-2 text-start">
            <h5>주문자 정보</h5>
            <p class="mb-1">주문자: ${orderInfo.ordererInfo.ordererName}</p>
            <p class="mb-1">이메일: ${orderInfo.ordererInfo.ordererEmail}</p>
            <p class="mb-1">연락처: ${orderInfo.ordererInfo.ordererPhone}</p>
           </div>
       </div>
	   <!-- end of deliveryInfoWrap -->

	   <h5 class="text-start">결제 정보</h5>
       	   <div class="deliveryInfoWrap row col-md-12 m-0 mb-5" style="border-top: 2px solid black; border-bottom: 2px solid #c9c9c9;">
                  <div class="deliveryInfoHead col-md-1 p-2 text-start">
                   <p class="mb-1">결제방식</p>
                   <p class="mb-1">${orderInfo.paymentInfo.payMethod}</p>
                  </div>
                  <div class="deliveryInfoBody col-md-6 p-2 text-start bg-white">
                   <p class="mb-1">카드이름: ${orderInfo.paymentInfo.cardName}</p>
                   <p class="mb-1">카드번호: ${orderInfo.paymentInfo.cardNum}</p>
                  </div>
                  <div class="ordererInfo col-md-4 p-2 text-start">
                   <h5>결제금액 정보</h5>
                   <p class="mb-1">상품금액: ${orderInfo.paymentInfo.totalAmount}</p>
                   <p class="mb-1">배송비: ${orderInfo.paymentInfo.deliveryAmount}</p>
                   <c:if test="${orderInfo.paymentInfo.mileagePoint > 0}">
                     <p class="mb-1">마일리지 사용: ${orderInfo.paymentInfo.mileagePoint}</p>
                   </c:if>
                  </div>
              </div>
       	   <!-- end of deliveryInfoWrap -->

       <div class="btnWrap mb-5">
        <c:if test="${orderInfo.isCancelOrder}">
            <button type="button" class="btn btn-warning" id="btnOrderCancel">주문 취소</button>
        </c:if>
        <button type="button" class="btn btn-warning" id="btnPrev">이전 페이지</button>
       </div>
	</div>
</article>

<script>
    // 이전 페이지 button event.
    $(document).on("click", "#btnPrev", function(){
        var url= "${pageContext.request.contextPath}/myOrder/list";
        location.href = url;
    });

    // 주문 취소 button event.
    $(document).on("click", "#btnOrderCancel", function(){
    });
</script>