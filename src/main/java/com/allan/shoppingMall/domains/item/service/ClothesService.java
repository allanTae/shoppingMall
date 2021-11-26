package com.allan.shoppingMall.domains.item.service;

import com.allan.shoppingMall.domains.item.domain.ImageType;
import com.allan.shoppingMall.domains.item.domain.ItemImage;
import com.allan.shoppingMall.domains.item.domain.ItemRepository;
import com.allan.shoppingMall.domains.item.domain.clothes.*;
import com.allan.shoppingMall.domains.item.domain.model.ClothesDTO;
import com.allan.shoppingMall.domains.item.domain.model.ClothesForm;
import com.allan.shoppingMall.domains.item.infra.ImageFileHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ClothesService {

    private final ItemRepository itemRepository;
    private final ImageFileHandler imageFileHandler;

    @Transactional
    public Long saveClothes(ClothesForm form){
        // 의류 원단 정보.
        List<ClothesFabric> fabrics = form.getClothesFabrics()
                .stream()
                .map(clothesFabricsDTO -> {
                    return ClothesFabric.builder()
                            .materialPart(clothesFabricsDTO.getMaterialPart())
                            .materialDesc(clothesFabricsDTO.getMaterialDesc())
                            .build();
                })
                .collect(Collectors.toList());

        // 의류 디테일 정보.
        List<ClothesDetail> details = form.getClothesDetails()
                .stream()
                .map(clothesDetailsDTO -> {
                    return ClothesDetail.builder()
                            .detailDesc(clothesDetailsDTO.getDetailDesc())
                            .build();
                })
                .collect(Collectors.toList());

        // 의류 사이즈 정보.
        List<ClothesSize> sizes = form.getClothesSizes()
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

        // 모델 사이즈 정보.
        List<ModelSize> modelSizes = form.getModelSizes()
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

        // 이미지 파일 정보.
        List<MultipartFile> profileImageFiles = form.getProfileImageFiles();
        List<ItemImage> profileItemImages = null;
        List<MultipartFile> detailImageFiles = form.getDetailImageFiles();
        List<ItemImage> detailItemImages = null;
        try{
            profileItemImages = imageFileHandler.parseImageInfo(profileImageFiles, ImageType.PREVIEW);
            detailItemImages = imageFileHandler.parseImageInfo(detailImageFiles, ImageType.PRODUCT);
        }catch (IOException e){
            log.error("ClothesService's saveClothes() cause Error!");
            log.error("exception message: " + e.getMessage());
        }

        Clothes clothes = Clothes.builder()
                .name(form.getName())
                .price(form.getPrice())
                .stockQuantity(form.getStockQuantity())
                .build();

        clothes.changeClothesFabrics(fabrics);
        clothes.changeClothesDetails(details);
        clothes.changeClothesSizes(sizes);
        clothes.changeModelSizes(modelSizes);
        clothes.changeItemImages(profileItemImages);
        clothes.changeItemImages(detailItemImages);

        itemRepository.save(clothes);
        return clothes.getItemId();
    }

    /**
     * PREVIEW 이미지를 포함한 의상정보를 반환하는 메소드.
     * @return List<ClothesSummaryDTO>
     */
    public List<ClothesSummaryDTO> getClothes(){
        List<Clothes> clothesList = itemRepository.getClothesList();

        List<ClothesSummaryDTO> clothesSummaryDTOS = clothesList
                .stream()
                .map(clothes -> {
                    ClothesSummaryDTO clothesSummary = ClothesSummaryDTO.builder()
                            .clothesId(clothes.getItemId())
                            .clothesName(clothes.getName())
                            .price(clothes.getPrice())
                            .profileImageIds(ClothesSummaryDTO.toImagePath(clothes.getItemImages()))
                            .build();
                    return clothesSummary;
                }).collect(Collectors.toList());

        return clothesSummaryDTOS;
    }
}
