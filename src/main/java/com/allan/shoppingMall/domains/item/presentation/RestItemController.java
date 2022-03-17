package com.allan.shoppingMall.domains.item.presentation;

import com.allan.shoppingMall.common.exception.item.ItemNotFoundException;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesSummeryDTO;
import com.allan.shoppingMall.domains.item.domain.item.ItemSummaryDTOForCart;
import com.allan.shoppingMall.domains.item.presentation.clothes.ClothesErrorResponse;
import com.allan.shoppingMall.domains.item.presentation.clothes.ClothesResponse;
import com.allan.shoppingMall.domains.item.presentation.clothes.ClothesResult;
import com.allan.shoppingMall.domains.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestItemController {

    private final ItemService itemService;

    /**
     * 장바구니 목록 프론트단에서 사용 할 상품 정보를 반환하는 핸들러.
     * (cartList.jsp 참고.)
     */
    @ResponseBody
    @GetMapping("/item/{itemId}")
    public ResponseEntity<ItemResponse> getItemSummayDTO(@PathVariable("itemId") Long itemId){
        try{
            ItemSummaryDTOForCart itemInfo = itemService.getItemSummaryDTO(itemId);
            return new ResponseEntity<ItemResponse>(new ItemResponse(ItemResult.GET_ITEM_INFO_SUCCESS, itemInfo), HttpStatus.OK);
        }catch (ItemNotFoundException exception){
            return new ResponseEntity<ItemResponse>(new ItemResponse(ItemResult.GET_ITEM_INFO_FAIL, ItemErrorResponse.of(exception.getErrorCode()))
                    , HttpStatus.OK);
        }
    }
}
