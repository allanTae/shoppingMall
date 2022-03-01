package com.allan.shoppingMall.domains.item.presentation.clothes;

import com.allan.shoppingMall.common.exception.ItemNotFoundException;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesSummeryDTO;
import com.allan.shoppingMall.domains.item.presentation.clothes.ClothesErrorResponse;
import com.allan.shoppingMall.domains.item.presentation.clothes.ClothesResponse;
import com.allan.shoppingMall.domains.item.presentation.clothes.ClothesResult;
import com.allan.shoppingMall.domains.item.service.ClothesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RestClothesController {
    private final ClothesService clothesService;

    /**
     * 장바구니 목록 프론트단에서 사용 할 상품 정보를 반환하는 핸들러.
     * (cartList.jsp 참고.)
     */
    @ResponseBody
    @GetMapping("/clothes/{clothesId}")
    public ResponseEntity<ClothesResponse> getClothesSummayDTO(@PathVariable("clothesId") Long clothesId){
        try{
            ClothesSummeryDTO clothesSummaryDTO = clothesService.getClothesSummaryDTO(clothesId);
            return new ResponseEntity<ClothesResponse>(new ClothesResponse(ClothesResult.GET_CLOTHES_INFO_SUCCESS, clothesSummaryDTO), HttpStatus.OK);
        }catch (ItemNotFoundException exception){
            return new ResponseEntity<ClothesResponse>(new ClothesResponse(ClothesResult.GET_CLOTHES_INFO_FAIL, ClothesErrorResponse.of(exception.getErrorCode()))
                    , HttpStatus.OK);
        }
    }
}
