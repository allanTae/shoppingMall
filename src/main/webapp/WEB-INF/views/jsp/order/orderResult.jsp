<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script>
    // 이전 페이지 button event.
    $(document).on("click", "#btnHome", function(){
        var url= "${pageContext.request.contextPath}/index";
        location.href = url;
    });
</script>
<div class="orderCardContainer">
    <div id="orderHeader" class="border-bottom border-3">
        <h1>주문 결과 페이지</h1>
        <div class="text-end fw-bold me-4">
            <h5><span class="text-black-50">주문/결제 > </span>주문결과</h5>
        </div>
    </div>
    <div class ="row">
        <div id="leftContentWrap" class="col-md-7 text-start p-5" >
            <c:choose>
                <c:when test="${orderResult.orderResult == '결제에 성공하였습니다.'}">
                    <h2>주문이 완료 되었습니다.</h2>
                    <br />
                    <p class="m-0">상품을 주문 하여주셔서 감사합니다.</p>
                    <p class="m-0">주문 건에 대한 자세한 내용은 상단메뉴 <span class="text-warning">ACCOUNT > ORDERLIST</span>를 참고 하시길 바랍니다.</p>
                    <p class="m-0">앞으로도 좋은 옷, 서비스를 제공하겠습니다.</p>
                    <br />
                    <div class="table-responsive">
                        <table class="table mt-4 table-borderless">
                          <tbody>
                            <tr>
                              <th>주문번호</th>
                              <td>${orderResult.orderNum}</td>
                            </tr>
                            <tr>
                              <th style="width:15%">배송지정보</th>
                              <td style="width:85%">
                                <p class="m-0">${orderInfo.recipientPhone}</p>
                                <p class="m-0">${orderInfo.recipient}</p>
                                <p class="m-0">${orderInfo.address}</p>
                              </td>
                            </tr>
                            <tr>
                              <th>배송메모</th>
                              <td>${orderInfo.deliveryMemo}</td>
                            </tr>
                            <tr>
                              <th>입금액</th>
                              <td><fmt:formatNumber type="number" maxFractionDigits="3" value="${orderInfo.paymentInfo.totalAmount}" />원</td>
                            </tr>
                          </tbody>
                        </table>
                    </div>
                    <div class="table-responsive">
                        <table class="table mt-4 table-bordered">
                          <tbody>
                            <tr>
                              <td>
                                <p class="m-0" >구매혜택</p>
                                <hr class="m-0" />
                                <p class="m-0" >마일리지: <fmt:formatNumber type="number" maxFractionDigits="3" value="${accumulatedMileage.mileagePoint}" />마일리지가 적립 됩니다.</p>
                              </td>
                          </tbody>
                        </table>
                    </div>
                </c:when>
                <c:when test="${orderResult.orderResult != '결제에 성공하였습니다.'}">
                    <h1>주문을 실패 하였습니다.</h1>
                    <br />
                    <p class="m-0">실패 사유에 대해서는 아래를 참고 해 주세요.</p>
                    <p class="m-0">실패건에 대한 좀더 자세히 알고 싶으시면 Q&A 게시판에 주문번호와 함께 문의 부탁드립니다.</p>
                    <br />
                    <div class="table-responsive">
                        <table class="table mt-4 table-borderless">
                          <tbody>
                            <tr>
                              <th>주문번호</th>
                              <td>${orderResult.orderNum}</td>
                            </tr>
                            <tr>
                              <th>실패사유</th>
                              <td>${orderResult.errorResponse.errMsg}</td>
                            </tr>
                          </tbody>
                        </table>
                    </div>
                </c:when>
            </c:choose>
        </div>
        <div id="rightContentWrap" class="col-md-5 p-5" >
             <c:choose>
                <c:when test="${orderResult.orderResult == '결제에 성공하였습니다.'}">
                    <div class="table-responsive">
                        <table class="table mt-4 table-borderless">
                          <tbody>
                            <c:forEach var="orderItem" items="${orderInfo.orderItems}" varStatus="index" >
                                <tr style="border-bottom: 2px solid black; border-bottom: 2px solid #c9c9c9;">
                                    <td>
                                        <div class="itemInfoImageWrap row col-md-12 m-0">
                                            <div class="itemImgWrap col-md-2 p-1 m-2">
                                                <img src="${pageContext.request.contextPath}/image/${orderItem.profileImg}" class="col-md-12"  height="90" width="90" />
                                            </div>
                                            <div class="itemInfoWrap col-md-8 p-1 text-start">
                                                <p class="m-0">${orderItem.name}</p>
                                                <p class="m-0">색상: ${orderItem.color}</p>
                                                <p class="m-0">상품금액: <fmt:formatNumber type="number" maxFractionDigits="3" value="${orderItem.price}" />원</p>
                                                <p class="m-0">주문수량: ${orderItem.orderQuantity}개  <span class="fst-italic">(사이즈: ${orderItem.size})</span></p>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                          </tbody>
                        </table>
                    </div>
                    <div class="table-responsive p-3 rounded" style="background-color: #FFA500;">
                        <table class="table mt-4 table-borderless text-white">
                          <tbody>
                            <tr>
                              <th class="p-2 text-start">상품금액</th>
                              <td class="p-2 text-end"><fmt:formatNumber type="number" maxFractionDigits="3" value="${orderInfo.paymentInfo.itemAmount}" />원</td>
                            </tr>
                            <tr>
                              <th class="p-2 text-start">배송비</th>
                              <td class="p-2 text-end">+<fmt:formatNumber type="number" maxFractionDigits="3" value="${orderInfo.paymentInfo.deliveryAmount}" />원</td>
                            </tr>
                            <tr>
                              <th class="p-2 text-start">마일리지사용</th>
                              <td class="p-2 text-end"><fmt:formatNumber type="number" maxFractionDigits="3" value="${orderInfo.paymentInfo.mileagePoint}" /></td>
                            </tr>
                            <tr style="border-top: 2px dashed white;">
                              <th class="p-2 text-start">최종결제금액</th>
                              <td class="p-2 text-end"><fmt:formatNumber type="number" maxFractionDigits="3" value="${orderInfo.paymentInfo.totalAmount}" />원</td>
                            </tr>
                            <tr>
                          </tbody>
                        </table>
                    </div>
                </c:when>
             </c:choose>
        </div>
    </div>
    <div class="btnWrap mb-5">
    <button type="button" class="btn btn-warning" id="btnHome">홈으로</button>
   </div>
</div>

