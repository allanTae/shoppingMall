package com.allan.shoppingMall.domains.item.service;

import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.item.AccessorySaveFailException;
import com.allan.shoppingMall.common.exception.item.ItemNotFoundException;
import com.allan.shoppingMall.domains.category.domain.Category;
import com.allan.shoppingMall.domains.category.domain.CategoryCode;
import com.allan.shoppingMall.domains.category.domain.CategoryItem;
import com.allan.shoppingMall.domains.category.domain.CategoryRepository;
import com.allan.shoppingMall.domains.item.domain.accessory.Accessory;
import com.allan.shoppingMall.domains.item.domain.accessory.AccessoryRepository;
import com.allan.shoppingMall.domains.item.domain.accessory.AccessorySize;
import com.allan.shoppingMall.domains.item.domain.item.ItemDetail;
import com.allan.shoppingMall.domains.item.domain.item.ItemFabric;
import com.allan.shoppingMall.domains.item.domain.item.ItemSize;
import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import com.allan.shoppingMall.domains.item.domain.item.Color;
import com.allan.shoppingMall.domains.item.domain.item.ImageType;
import com.allan.shoppingMall.domains.item.domain.item.ItemImage;
import com.allan.shoppingMall.domains.item.domain.model.*;
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
@Slf4j
public class AccessoryService {

    private final AccessoryRepository accessoryRepository;
    private final ImageFileHandler imageFileHandler;
    private final CategoryRepository categoryRepository;

    /**
     * form 으로 전달 된 데이터로 accessory 저장하는 메소드.
     * @param form
     * @return accessoryId
     */
    @Transactional
    public Long saveAccessory(AccessoryForm form){
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

        // 악세서리 사이즈 정보.
        List<AccessorySize> sizes = form.getAccessorySizes()
                .stream()
                .map(accessorySizeDTO -> {
                    return AccessorySize.builder()
                            .widthLength(accessorySizeDTO.getWidthLength())
                            .heightLength(accessorySizeDTO.getHeightLength())
                            .sizeLabel(SizeLabel.valueOf(Integer.valueOf(accessorySizeDTO.getSizeLabel())))
                            .stockQuantity(accessorySizeDTO.getStockQuantity())
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
        if(findCategory.getCategoryCode().getCode() != CategoryCode.ACCESSORY.getCode())
            throw new AccessorySaveFailException(ErrorCode.ITEM_CATEGORY_CODE_INVALID);

        Accessory accessory = Accessory.builder()
                .name(form.getName())
                .engName(form.getEngName())
                .price(form.getPrice())
                .color(Color.valueOf(form.getClothesColor()))
                .build();

        accessory.changeItemFabrics(fabrics);
        accessory.changeItemDetails(details);
        accessory.changeAccessorySize(sizes);
        accessory.changeItemImages(profileItemImages);
        accessory.changeItemImages(detailItemImages);
        accessory.changeCategoryItems(List.of(new CategoryItem(findCategory)));

        accessoryRepository.save(accessory);
        return accessory.getItemId();
    }

    /**
     * 단일 Accessory 도메인에 대한 AccessoryDTO 를 반환하는 메소드.(상품 상세보기 위한 기능).
     * @param accessoryId 악세서리 도메인 id.
     * @return AccessoryDTO
     */
    public AccessoryDTO getAccessory(Long accessoryId){
        Accessory findAccessory = accessoryRepository.findById(accessoryId).orElseThrow(() ->
                new ItemNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        List<ItemFabricDTO> fabricDTOS = findAccessory.getItemFabrics()
                .stream()
                .map(itemFabric -> {
                    return ItemFabricDTO.builder()
                            .materialPart(itemFabric.getMaterialPart())
                            .materialDesc(itemFabric.getMaterialDesc())
                            .build();
                }).collect(Collectors.toList());

        List<ItemDetailDTO> detailDTOS = findAccessory.getItemDetails()
                .stream()
                .map(itemDetail -> {
                    return ItemDetailDTO.builder()
                            .detailDesc(itemDetail.getDetailDesc())
                            .build();
                }).collect(Collectors.toList());

        List<AccessorySizeDTO> sizeDTOS = findAccessory.getAccessorySizes()
                .stream()
                .map(accessorySize -> {
                    return AccessorySizeDTO.builder()
                            .sizeLabel(accessorySize.getSizeLabel().getKey())
                            .widthLength(accessorySize.getWidthLength())
                            .heightLength(accessorySize.getHeightLength())
                            .build();
                }).collect(Collectors.toList());

        AccessoryDTO accessoryDTO = AccessoryDTO.builder()
                .accessoryName(findAccessory.getName())
                .engName(findAccessory.getEngName())
                .price(findAccessory.getPrice())
                .accessoryId(findAccessory.getItemId())
                .itemFabrics(fabricDTOS)
                .itemDetails(detailDTOS)
                .accessorySizes(sizeDTOS)
                .itemImages(findAccessory.getItemImages())
                .color(findAccessory.getColor().getDesc())
                .build();

        return accessoryDTO;
    }
}
