package com.allan.shoppingMall.domains.item.service;

import com.allan.shoppingMall.domains.item.domain.ItemRepository;
import com.allan.shoppingMall.domains.item.domain.clothes.*;
import com.allan.shoppingMall.domains.item.domain.model.ClothesForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClothesService {

    private final ItemRepository itemRepository;

    @Transactional
    public Long saveClothes(ClothesForm request){
        // 의류 원단 정보.
        List<ClothesFabric> fabrics = request.getClothesFabrics()
                .stream()
                .map(clothesFabricsDTO -> {
                    return ClothesFabric.builder()
                            .materialPart(clothesFabricsDTO.getMaterialPart())
                            .materialDesc(clothesFabricsDTO.getMaterialDesc())
                            .build();
                })
                .collect(Collectors.toList());

        // 의류 디테일 정보.
        List<ClothesDetail> details = request.getClothesDetails()
                .stream()
                .map(clothesDetailsDTO -> {
                    return ClothesDetail.builder()
                            .detailDesc(clothesDetailsDTO.getDetailDesc())
                            .build();
                })
                .collect(Collectors.toList());

        // 의류 사이즈 정보.
        List<ClothesSize> sizes = request.getClothesSizes()
                .stream()
                .map(clothesSizesDTO -> {
                    return ClothesSize.builder()
                            .shoulderWidth(clothesSizesDTO.getShoulderWidth())
                            .backLength(clothesSizesDTO.getBackLength())
                            .bottomWidth(clothesSizesDTO.getBottomWidth())
                            .chestWidth(clothesSizesDTO.getChestWidth())
                            .heapWidth(clothesSizesDTO.getHeapWidth())
                            .sleeveLength(clothesSizesDTO.getSleeveLength())
                            .sizeLabel(SizeLabel.valueOf(Integer.valueOf(clothesSizesDTO.getSizeLabel())))
                            .waistWidth(clothesSizesDTO.getWaistWidth())
                            .build();
                })
                .collect(Collectors.toList());

        List<ModelSize> modelSizes = request.getModelSizes()
                .stream()
                .map(modelSizeDTO -> {
                    return ModelSize.builder()
                            .modelShoulderSize(modelSizeDTO.getModelShoulderSize())
                            .modelHeight(modelSizeDTO.getModelHeight())
                            .modelWeight(modelSizeDTO.getModelWeight())
                            .modelWaist(modelSizeDTO.getModelWaist())
                            .modelHeap(modelSizeDTO.getModelHeap())
                            .build();
                })
                .collect(Collectors.toList());

        Clothes clothes = Clothes.builder()
                .name(request.getName())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .build();

        clothes.changeClothesFabrics(fabrics);
        clothes.changeClothesDetails(details);
        clothes.changeClothesSizes(sizes);
        clothes.changeModelSizes(modelSizes);

        itemRepository.save(clothes);
        return clothes.getItemId();
    }


}
