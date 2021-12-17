package com.allan.shoppingMall.domains.order.service;

import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.ItemNotFoundException;
import com.allan.shoppingMall.common.exception.order.OrderNotFoundException;
import com.allan.shoppingMall.common.value.Address;
import com.allan.shoppingMall.domains.delivery.domain.Delivery;
import com.allan.shoppingMall.domains.delivery.domain.DeliveryStatus;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesRepository;
import com.allan.shoppingMall.domains.item.domain.ItemRepository;
import com.allan.shoppingMall.domains.item.domain.clothes.Clothes;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesSize;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesSizeRepository;
import com.allan.shoppingMall.domains.member.domain.Member;
import com.allan.shoppingMall.domains.order.domain.*;
import com.allan.shoppingMall.domains.order.domain.model.OrderDetailDTO;
import com.allan.shoppingMall.domains.order.domain.model.OrderItemDTO;
import com.allan.shoppingMall.domains.order.domain.model.OrderRequest;
import com.allan.shoppingMall.domains.order.domain.model.OrderSummaryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClothesRepository clothesRepository;
    private final ClothesSizeRepository clothesSizeRepository;

    /**
     * 상품 상세페이지를 통해서 바로 주문하는 경우 사용하는 주문 메소드.
     * (주문 리스트에 상품들은 모두 동일한 종류의 상품들이다. 예) 의상 페이지에서 주문시, 주문 리스트 상품은 모두 의상 상품들.)
     */
    @Transactional
    public Long order(OrderRequest request, Member member){
        // 카테고리에 따른 분기 처리가 필요. 카테고리 추가시 코드 수정이 필요하다.

        Order order = Order.builder()
                .orderer(member)
                .delivery(Delivery.builder()
                        .address(Address.builder()
                                .roadAddress(request.getAddress())
                                .detailAddress(request.getDetailAddress())
                                .postCode(request.getPostcode())
                                .build())
                        .deliveryStatus(DeliveryStatus.DELIVERY_READY)
                        .deliveryMemo(request.getDeliveryMemo())
                        .recipient(request.getRecipient())
                        .recipientPhone(request.getRecipientPhone())
                        .build())
                .orderStatus(OrderStatus.ORDER_ITEM_READY)
                .ordererInfo(OrdererInfo.builder()
                                .ordererName(request.getOrdererName())
                                .ordererPhone(request.getOrdererPhone())
                                .ordererEmail(request.getOrdererEmail())
                                .build())
                .build();

        List<OrderItem> orderItems = request.getOrderItems()
                .stream()
                .map(orderLineRequest -> {
                    // findItem 의 카테고리를 확인 해서 분기 처리가 필요하다.(카테고리 추가 후 로직 변경 필요.)
                    // clothes 상품인 경우.
                    Clothes clothes = clothesRepository.findById(orderLineRequest.getItemId()).orElseThrow(()
                            -> new ItemNotFoundException(ErrorCode.ENTITY_NOT_FOUND.getMessage(), ErrorCode.ENTITY_NOT_FOUND));

                    ClothesSize clothesSize = clothesSizeRepository.getClothesSizebySizelabel(clothes, orderLineRequest.getSize());

                    return new OrderClothes(orderLineRequest.getOrderQuantity(), clothes, clothesSize);
                    // 그외의 상품인 경우.
                }).collect(Collectors.toList());

        order.changeOrderItems(orderItems);
        orderRepository.save(order);

        return order.getOrderId();
    }

    @Transactional
    public Long cancelOrder(Long orderId){
        Order findOrder = orderRepository.findById(orderId).orElseThrow(()
                -> new OrderNotFoundException(ErrorCode.ENTITY_NOT_FOUND.getMessage(), ErrorCode.ENTITY_NOT_FOUND));

        findOrder.cancelOrder();

        return findOrder.getOrderId();
    }

    /**
     * 자신의 주문 리스트를 페이징하여 반환하기 위한 메소드.
     * (기본 페이징 사이즈는 10 입니다.)
     * @param authId 사용자 아이디.
     * @param pageable 페이지 정보.
     */
    public Page<Order> getMyOrderSummaryList(String authId, Pageable pageable){

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Order> orderPage = orderRepository.getOrderListByAuthId(authId, pageable);

        return orderPage;
    }

    /**
     * 컨트롤러단에서 전달받은 Page<Order> 를 프론트단으로 전송 할 List<OrderSummaryDTO> 반환하기 위한 메소드.
     * @param orders 페이징 한 Order 정보.
     * @return List<OrderSummaryDTO>
     */
    public List<OrderSummaryDTO> getOrderSummaryDTO(List<Order> orders){
        return
                orders.stream()
                        .map(order -> {
                            String orderItemsName = "";
                            if(order.getOrderItems().size() > 1)
                                orderItemsName = order.getOrderItems().get(0).getItem().getName() + "외 " + (order.getOrderItems().size()-1) + "건";
                            else
                                orderItemsName = order.getOrderItems().get(0).getItem().getName();

                            return OrderSummaryDTO.builder()
                                    .orderId(order.getOrderId())
                                    .orderStatus(order.getOrderStatus().getDesc())
                                    .orderName(orderItemsName)
                                    .profileImgId(order.getOrderItems().get(0).getItem().getItemImages().get(0).getItemImageId())
                                    .createdDate(order.getCreatedDate())
                                    .build();
                        })
                        .collect(Collectors.toList());
    }

    /**
     * 주문 1건에 대한 상세 주문정보를 반환하는 메소드.
     * @param orderId
     * @return OrderDetailDTO
     */
    public OrderDetailDTO getOrderDetailDTO(Long orderId){
        Order findOrder = orderRepository.findById(orderId).orElseThrow(()
                -> new OrderNotFoundException(ErrorCode.ENTITY_NOT_FOUND.getMessage(), ErrorCode.ENTITY_NOT_FOUND));

        List<OrderItemDTO> orderItemDTOS = findOrder.getOrderItems()
                .stream()
                .map(orderItem -> {
                    OrderItemDTO orderItemDTO = OrderItemDTO.builder()
                            .name(orderItem.getItem().getName())
                            .price(orderItem.getItem().getPrice())
                            .color(orderItem.getItem().getColor().getDesc())
                            .profileImg(orderItem.getItem().getItemImages().get(0).getItemImageId())
                            .orderQuantity(orderItem.getOrderQuantity())
                            .discountName("없음")
                            .discountPrice(0l)
                            .build();

                    // Clothes 상품만 사이즈 정보를 가지고 있기에(ClothesSize 로 관리)
                    // OrderClothes 인 경우를 체크하고 size 정보를 추가한다.
                    if(orderItem instanceof OrderClothes){
                        OrderClothes orderClothes = (OrderClothes) orderItem;
                        orderItemDTO.setSize(orderClothes.getClothesSize().getSizeLabel());
                    }

                    return orderItemDTO;
                })
                .collect(Collectors.toList());

        OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder()
                .orderId(findOrder.getOrderId())
                .orderDate(LocalDateTime.now())
                .orderItems(orderItemDTOS)
                .recipient(findOrder.getDelivery().getRecipient())
                .recipientPhone(findOrder.getDelivery().getRecipientPhone())
                .address(findOrder.getDelivery().getAddress().getRoadAddress() + findOrder.getDelivery().getAddress().getDetailAddress())
                .orderStatus(findOrder.getOrderStatus().getDesc())
                .ordererInfo(OrdererInfo.builder()
                        .ordererName(findOrder.getOrdererInfo().getOrdererName())
                        .ordererPhone(findOrder.getOrdererInfo().getOrdererPhone())
                        .ordererEmail(findOrder.getOrdererInfo().getOrdererEmail())
                        .build())
                .deliveryMemo(findOrder.getDelivery().getDeliveryMemo())
                .build();

        return orderDetailDTO;
    }
}
