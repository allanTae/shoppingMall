package com.allan.shoppingMall.domains.item.service;

import com.allan.shoppingMall.domains.category.service.CategoryService;
import com.allan.shoppingMall.domains.item.domain.item.Item;
import com.allan.shoppingMall.domains.item.domain.item.ItemRepository;
import com.allan.shoppingMall.domains.item.domain.model.ItemSummaryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 상품 도메인 서비스단.
 */
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryService categoryService;

    /**
     * 카테고리에 해당하는 상품 도메인 리스트를 페이징하여 반환하기 위한 메소드.
     * (기본 페이징 사이즈는 10 입니다.)
     * @return List<Item>
     */
    public Page<Item> getItems(Long categoryId, Pageable pageable){

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        List<Long> categoryIds = categoryService.getCategoryIds(categoryId);

        pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Item> items = itemRepository.getItemsByCategoryIds(categoryIds, pageable);

        return items;
    }

    /**
     * 컨트롤러단에서 전달받은 Page<Item> 를 프론트단으로 전송 할 List<ItemSummaryDTO> 반환하기 위한 메소드.
     * @param items 페이징 한 Item 정보.
     * @return List<ItemSummaryDTO>
     */
    public List<ItemSummaryDTO> getItemDTOS(List<Item> items, Long categoryId){
        List<ItemSummaryDTO> itemSummaryDTOList = items.stream()
                .map(item -> {
                    return ItemSummaryDTO.builder()
                            .itemId(item.getItemId())
                            .name(item.getName())
                            .price(item.getPrice())
                            .itemColor(item.getColor().getDesc())
                            .profileImageIds(ItemSummaryDTO.toImagePath(item.getItemImages()))
                            .categoryId(categoryId)
                            .build();
                }).collect(Collectors.toList());

        return itemSummaryDTOList;
    }

}
