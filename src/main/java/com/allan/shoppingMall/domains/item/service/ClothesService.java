package com.allan.shoppingMall.domains.item.service;

import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.ItemNotFoundException;
import com.allan.shoppingMall.domains.cart.domain.model.RequiredOption;
import com.allan.shoppingMall.domains.category.domain.Category;
import com.allan.shoppingMall.domains.category.domain.CategoryRepository;
import com.allan.shoppingMall.domains.item.domain.*;
import com.allan.shoppingMall.domains.item.domain.clothes.*;
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

    /**
     * form 으로 전달 된 데이터로 clothes 저장하는 메소드.
     * @param form
     * @return clothesId
     */
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

        // 색상 정보.
        Clothes clothes = Clothes.builder()
                .name(form.getName())
                .price(form.getPrice())
                .engName(form.getEngName())
                .color(Color.valueOf(form.getClothesColor()))
                .category(findCategory)
                .build();

        clothes.changeClothesFabrics(fabrics);
        clothes.changeClothesDetails(details);
        clothes.changeClothesSizes(sizes);
        clothes.changeModelSizes(modelSizes);
        clothes.changeItemImages(profileItemImages);
        clothes.changeItemImages(detailItemImages);

        clothesRepository.save(clothes);
        return clothes.getItemId();
    }

    /**
     * 단일 clothesDTO 를 반환하는 메소드.
     * @param clothesId
     * @return ClothesDTO
     */
    public ClothesDTO getClothes(Long clothesId){

        Clothes findClothes = clothesRepository.getClothes(clothesId)
                .orElseThrow(() ->
                        new ItemNotFoundException(ErrorCode.ENTITY_NOT_FOUND.getMessage(), ErrorCode.ENTITY_NOT_FOUND));

        List<ClothesFabricDTO> fabricDTOS = findClothes.getClothesFabrics()
                .stream()
                .map(clothesFabric -> {
                    return ClothesFabricDTO.builder()
                            .materialPart(clothesFabric.getMaterialPart())
                            .materialDesc(clothesFabric.getMaterialDesc())
                            .build();
                }).collect(Collectors.toList());

        List<ClothesDetailDTO> detailDTOS = findClothes.getClothesDetails()
                .stream()
                .map(clothesDetail -> {
                    return ClothesDetailDTO.builder()
                            .detailDesc(clothesDetail.getDetailDesc())
                            .build();
                }).collect(Collectors.toList());

        List<ClothesSizeDTO> sizeDTOS = findClothes.getClothesSizes()
                .stream()
                .map(clothesSize -> {
                    return ClothesSizeDTO.builder()
                            .backLength(clothesSize.getBackLength())
                            .sizeLabel(clothesSize.getSizeLabel().getKey())
                            .bottomWidth(clothesSize.getBottomWidth())
                            .chestWidth(clothesSize.getChestWidth())
                            .shoulderWidth(clothesSize.getShoulderWidth())
                            .heapWidth(clothesSize.getHeapWidth())
                            .sleeveLength(clothesSize.getSleeveLength())
                            .waistWidth(clothesSize.getWaistWidth())
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
                                    .clothesFabrics(fabricDTOS)
                                    .clothesDetails(detailDTOS)
                                    .clothesSizes(sizeDTOS)
                                    .modelSizes(modelSizeDTOS)
                                    .itemImages(findClothes.getItemImages())
                                    .color(findClothes.getColor().getDesc())
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
                            .build();
                    return clothesSummary;
                }).collect(Collectors.toList());

        return clothesSummaryDTOS;
    }

    /**
     * 의류 상품 요약정보를 조회하는 메소드.
     * @param clothesId 의류상품 도메인 id.
     * @return ClothesSummaryDTO
     */
    public ClothesSummeryDTO getClothesSummaryDTO(Long clothesId){
        Clothes findClothes = clothesRepository.findById(clothesId).orElseThrow(() ->
                new ItemNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        List<SizeLabel> clothesSizes = findClothes.getClothesSizes().stream()
                .map(clothesSize -> {
                    return clothesSize.getSizeLabel();
                }).collect(Collectors.toList());

        return ClothesSummeryDTO.builder()
                .clothesId(findClothes.getItemId())
                .clothesName(findClothes.getName())
                .clothesPrice(findClothes.getPrice())
                .sizes(clothesSizes)
                .profileImgId(findClothes.getItemImages().get(0).getItemImageId())
                .build();
    }
}
