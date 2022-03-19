package com.allan.shoppingMall.domains.item.service;

import com.allan.shoppingMall.common.exception.BusinessException;
import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.category.CategoryNotFoundException;
import com.allan.shoppingMall.common.exception.item.ClothesSaveFailException;
import com.allan.shoppingMall.common.exception.item.ItemNotFoundException;
import com.allan.shoppingMall.common.exception.item.ItemSizeNotFoundException;
import com.allan.shoppingMall.domains.category.domain.*;
import com.allan.shoppingMall.domains.item.domain.clothes.*;
import com.allan.shoppingMall.domains.item.domain.item.*;
import com.allan.shoppingMall.domains.item.domain.model.*;
import com.allan.shoppingMall.domains.item.infra.ImageFileHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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

    private final ClothesRepository clothesRepository;
    private final ImageFileHandler imageFileHandler;
    private final CategoryRepository categoryRepository;
    private final CategoryItemRepository categoryItemRepository;

    /**
     * form 으로 전달 된 데이터로 clothes 저장하는 메소드.
     * @param form
     * @return clothesId
     */
    @Transactional
    public Long saveClothes(ClothesForm form){
        // 의류 원단 정보.
        List<ItemFabric> fabrics = form.getItemFabrics()
                .stream()
                .map(clothesFabricsDTO -> {
                    return ItemFabric.builder()
                            .materialPart(clothesFabricsDTO.getMaterialPart())
                            .materialDesc(clothesFabricsDTO.getMaterialDesc())
                            .build();
                })
                .collect(Collectors.toList());

        // 의류 디테일 정보.
        List<ItemDetail> details = form.getItemDetails()
                .stream()
                .map(clothesDetailsDTO -> {
                    return ItemDetail.builder()
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
                            .stockQuantity(clothesSizesDTO.getStockQuantity())
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

        // 이미지 파일 정보. (프로필 사진 파일, 디테일 사진 파일)
        List<MultipartFile> profileImageFiles = form.getProfileImageFiles();
        List<MultipartFile> detailImageFiles = form.getDetailImageFiles();
        List<ItemImage> profileItemImages = null;
        List<ItemImage> detailItemImages = null;
        try{
            profileItemImages = imageFileHandler.parseImageInfo(profileImageFiles, ImageType.PREVIEW);
            detailItemImages = imageFileHandler.parseImageInfo(detailImageFiles, ImageType.PRODUCT);
        }catch (IOException e){
            log.error("ClothesService's saveClothes() cause Error!");
            log.error("exception message: " + e.getMessage());
        }

        Category findCategory = categoryRepository.findById(form.getCategoryId()).orElseThrow(() ->
                new ItemNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        // 예외 처리 필요.
        if(findCategory.getCategoryCode().getCode() != CategoryCode.CLOTHES.getCode())
            throw new ClothesSaveFailException(ErrorCode.ITEM_CATEGORY_CODE_INVALID);

        // 의류 상품 도메인 생성.
        Clothes clothes = Clothes.builder()
                .name(form.getName())
                .price(form.getPrice())
                .engName(form.getEngName())
                .color(Color.valueOf(form.getClothesColor()))
                .build();

        clothes.changeItemFabrics(fabrics);
        clothes.changeItemDetails(details);
        clothes.changeClothesSize(sizes);
        clothes.changeModelSizes(modelSizes);
        clothes.changeItemImages(profileItemImages);
        clothes.changeItemImages(detailItemImages);
        clothes.changeCategoryItems(List.of(new CategoryItem(findCategory)));

        clothesRepository.save(clothes);
        return clothes.getItemId();
    }

    /**
     * 단일 Clothes 도메인에 대한 clothesDTO 를 반환하는 메소드.(상품 상세보기 위한 기능).
     * @param clothesId
     * @return ClothesDTO
     */
    public ClothesDTO getClothes(Long clothesId){

        Clothes findClothes = clothesRepository.getClothes(clothesId)
                .orElseThrow(() ->
                        new ItemNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        CategoryItem clothesCategoryItem = categoryItemRepository.getCategoryItem(List.of(CategoryCode.CLOTHES), findClothes.getItemId()).orElseThrow(() ->
                new CategoryNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        List<ItemFabricDTO> fabricDTOS = findClothes.getItemFabrics()
                .stream()
                .map(itemFabric -> {
                    return ItemFabricDTO.builder()
                            .materialPart(itemFabric.getMaterialPart())
                            .materialDesc(itemFabric.getMaterialDesc())
                            .build();
                }).collect(Collectors.toList());

        List<ItemDetailDTO> detailDTOS = findClothes.getItemDetails()
                .stream()
                .map(itemDetail -> {
                    return ItemDetailDTO.builder()
                            .detailDesc(itemDetail.getDetailDesc())
                            .build();
                }).collect(Collectors.toList());

        List<ClothesSizeDTO> sizeDTOS = findClothes.getItemSizes()
                .stream()
                .map(itemSize -> {
                    ClothesSize clothesSize = null;
                    if(itemSize instanceof ClothesSize)
                        clothesSize = (ClothesSize) itemSize;
                    else
                        throw new ItemSizeNotFoundException("의류 상품 사이즈 정보를 찾을 수가 없습니다.", ErrorCode.INTERNAL_SERVER_ERROR);

                    return ClothesSizeDTO.builder()
                            .backLength(clothesSize.getBackLength())
                            .sizeLabel(itemSize.getSizeLabel().getKey())
                            .bottomWidth(clothesSize.getBottomWidth())
                            .chestWidth(clothesSize.getChestWidth())
                            .shoulderWidth(clothesSize.getShoulderWidth())
                            .heapWidth(clothesSize.getHeapWidth())
                            .sleeveLength(clothesSize.getSleeveLength())
                            .waistWidth(clothesSize.getWaistWidth())
                            .labelInfo(clothesSize.getSizeLabel())
                            .stockQuantity(itemSize.getStockQuantity())
                            .build();
                }).collect(Collectors.toList());

        List<ModelSizeDTO> modelSizeDTOS = findClothes.getModelSizes()
                .stream()
                .map(modelSize -> {
                    return ModelSizeDTO.builder()
                            .modelHeap(modelSize.getModelHeap())
                            .modelShoulderSize(modelSize.getModelShoulderSize())
                            .modelHeight(modelSize.getModelHeight())
                            .modelWaist(modelSize.getModelWaist())
                            .modelWeight(modelSize.getModelWeight())
                            .build();
                }).collect(Collectors.toList());

        ClothesDTO clothesDTO = ClothesDTO.builder()
                                    .clothesName(findClothes.getName())
                                    .engName(findClothes.getEngName())
                                    .price(findClothes.getPrice())
                                    .clothesId(findClothes.getItemId())
                                    .itemFabrics(fabricDTOS)
                                    .itemDetails(detailDTOS)
                                    .clothesSizes(sizeDTOS)
                                    .modelSizes(modelSizeDTOS)
                                    .itemImages(findClothes.getItemImages())
                                    .color(findClothes.getColor().getDesc())
                                    .categoryId(clothesCategoryItem.getCategory().getCategoryId())
                                    .categoryName(clothesCategoryItem.getCategory().getName())
                                    .build();

        return clothesDTO;
    }

    /**
     * index.jsp 에서 arraival 에 표시할 상품에 대한 PREVIEW 이미지, 상품이름, 가격 등의 정보를 반환하는 메소드.
     * @return List<ClothesSummaryDTO>
     */
    public List<ClothesSummaryDTOForIndex> getClothesSummary(Pageable pageable){
        List<Clothes> clothesList = clothesRepository.findAll(pageable).getContent();

        List<ClothesSummaryDTOForIndex> clothesSummaryDTOS = clothesList
                .stream()
                .map(clothes -> {
                    ClothesSummaryDTOForIndex clothesSummary = ClothesSummaryDTOForIndex.builder()
                            .clothesId(clothes.getItemId())
                            .clothesName(clothes.getName())
                            .price(clothes.getPrice())
                            .profileImageIds(ClothesSummaryDTOForIndex.toImagePath(clothes.getItemImages()))
                            .clothesColor(clothes.getColor().getDesc())
                            .categoryId(clothes.getCategoryItems().get(0).getCategory().getCategoryId())
                            .build();
                    return clothesSummary;
                }).collect(Collectors.toList());

        return clothesSummaryDTOS;
    }


    /**
     * 프론트단으로 전달 받은 폼정보로, clothes 정보를 수정하는 메소드 입니다.
     * @param form
     * @return clothes domain id.
     */
    @Transactional
    public Long updateClothes(ClothesForm form){
        Clothes findClothes = clothesRepository.findById(form.getClothesId()).orElseThrow(() ->
                new ItemNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        // 기존 원단, 상세내용, 모델사이즈, 의류사이즈, 카테고리 정보 삭제.
        findClothes.getItemFabrics().clear();
        findClothes.getItemDetails().clear();
        findClothes.getModelSizes().clear();
        findClothes.getCategoryItems().clear();

        // 의류 원단 정보.
        List<ItemFabric> fabrics = form.getItemFabrics()
                .stream()
                .map(clothesFabricsDTO -> {
                    return ItemFabric.builder()
                            .materialPart(clothesFabricsDTO.getMaterialPart())
                            .materialDesc(clothesFabricsDTO.getMaterialDesc())
                            .build();
                })
                .collect(Collectors.toList());

        // 의류 디테일 정보.
        List<ItemDetail> details = form.getItemDetails()
                .stream()
                .map(clothesDetailsDTO -> {
                    return ItemDetail.builder()
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
                            .stockQuantity(clothesSizesDTO.getStockQuantity())
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

        Category findCategory = categoryRepository.findById(form.getCategoryId()).orElseThrow(() ->
                new ItemNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        // 예외 처리 필요.
        if(findCategory.getCategoryCode().getCode() != CategoryCode.CLOTHES.getCode())
            throw new ClothesSaveFailException(ErrorCode.ITEM_CATEGORY_CODE_INVALID);

        findClothes.changeName(form.getName());
        findClothes.changePrice(form.getPrice());
        findClothes.changeEngName(form.getEngName());
        findClothes.changeColor(Color.valueOf(form.getClothesColor()));

        findClothes.changeItemFabrics(fabrics);
        findClothes.changeItemDetails(details);
        findClothes.changeModelSizes(modelSizes);
        findClothes.changeCategoryItems(List.of(new CategoryItem(findCategory)));

        // 사이즈 도메인 수정.
        findClothes.updateClothesSize(sizes);

        return findClothes.getItemId();
    }

}
