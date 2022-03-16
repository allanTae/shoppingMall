package com.allan.shoppingMall.domains.cart.service;

import com.allan.shoppingMall.domains.cart.domain.Cart;
import com.allan.shoppingMall.domains.cart.domain.CartItem;
import com.allan.shoppingMall.domains.cart.domain.CartRepository;
import com.allan.shoppingMall.domains.cart.domain.model.*;
import com.allan.shoppingMall.domains.category.domain.*;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesSize;
import com.allan.shoppingMall.domains.item.domain.item.ItemRepository;
import com.allan.shoppingMall.domains.item.domain.item.ItemSize;
import com.allan.shoppingMall.domains.item.domain.item.ItemImage;
import com.allan.shoppingMall.domains.item.domain.clothes.Clothes;
import com.allan.shoppingMall.domains.item.domain.clothes.ClothesRepository;
import com.allan.shoppingMall.domains.item.domain.clothes.SizeLabel;
import com.allan.shoppingMall.domains.member.domain.Member;
import com.allan.shoppingMall.domains.member.domain.MemberRepository;
import com.allan.shoppingMall.domains.order.domain.model.OrderSummaryRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Rollback(value = true)
public class CartServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    CartRepository cartRepository;

    @Mock
    ClothesRepository clothesRepository;

    @Mock
    CategoryItemRepository categoryItemRepository;

    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    CartService cartService;

    private static final Long TEST_ITEM_ID_1 = 1l;
    private static final Long TEST_ITEM_ID_2 = 2l;
    private static final Long TEST_ITEM_ID_3 = 3l;
    private static final Long TEST_ITEM_ID_4 = 4l;

    @Test
    public void 쿠키정보가_존재하는_경우_비회원_장바구니_추가테스트() throws Exception {
        //given
        Cart TEST_CART = Cart.builder().build();

        given(cartRepository.findByCkId(any()))
                .willReturn(Optional.of(TEST_CART));

        CartRequest TEST_CARTREQUEST = CartRequest.builder()
                .cartCkId("testCkId")
                .cartItems(List.of(
                        new CartLineRequest(1l, 1l, SizeLabel.S, 1l),
                        new CartLineRequest(2l, 2l, SizeLabel.M, 1l)

                ))
                .build();

        Clothes TEST_CLOTHES_1 = Clothes.builder()
                .name("testClothes1")
                .build();

        Clothes TEST_CLOTHES_2 = Clothes.builder()
                .name("testClothes2")
                .build();

        given(itemRepository.findById(1l))
                .willReturn(Optional.of(TEST_CLOTHES_1));
        given(itemRepository.findById(2l))
                .willReturn(Optional.of(TEST_CLOTHES_2));

        //when
        cartService.updatempCart(TEST_CARTREQUEST);

        //then
        verify(cartRepository, atLeastOnce()).findByCkId(any());
        verify(itemRepository, atLeastOnce()).findById(1l);
        verify(itemRepository, atLeastOnce()).findById(2l);
        assertThat(TEST_CART.getCartItems().size(), is(2));
        assertThat(TEST_CART.getCartItems().get(0).getItem().getName(), is(TEST_CLOTHES_1.getName()));
        assertThat(TEST_CART.getCartItems().get(1).getItem().getName(), is(TEST_CLOTHES_2.getName()));
    }

    @Test
    public void 쿠키_정보가_없는경우_비회원_장바구니_추가테스트() throws Exception {
        //given
        CartRequest TEST_CARTREQUEST = CartRequest.builder()
                .cartCkId("testCkId")
                .cartItems(List.of(
                        new CartLineRequest(1l, 1l, SizeLabel.S, 1l),
                        new CartLineRequest(2l, 2l, SizeLabel.M, 1l)

                ))
                .build();

        Clothes TEST_CLOTHES_1 = Clothes.builder()
                .name("testClothes1")
                .build();

        Clothes TEST_CLOTHES_2 = Clothes.builder()
                .name("testClothes2")
                .build();

        given(itemRepository.findById(1l))
                .willReturn(Optional.of(TEST_CLOTHES_1));
        given(itemRepository.findById(2l))
                .willReturn(Optional.of(TEST_CLOTHES_2));

        //when
        cartService.addTempCart(TEST_CARTREQUEST);

        //then
        verify(itemRepository, atLeastOnce()).findById(1l);
        verify(itemRepository, atLeastOnce()).findById(2l);
        verify(cartRepository, atLeastOnce()).save(any());
    }

    @Test
    public void 회원_장바구니_추가테스트() throws Exception {
        //given
        CartRequest TEST_CARTREQUEST = CartRequest.builder()
                .cartCkId("testCkId")
                .cartItems(List.of(
                        new CartLineRequest(1l, 1l, SizeLabel.S, 1l),
                        new CartLineRequest(2l, 2l, SizeLabel.M, 1l)

                ))
                .build();

        Member TEST_MEMBER = Member.builder()
                .authId("testAuthId")
                .build();

        Clothes TEST_CLOTHES_1 = Clothes.builder()
                .name("testClothes1")
                .build();

        Clothes TEST_CLOTHES_2 = Clothes.builder()
                .name("testClothes2")
                .build();

        given(itemRepository.findById(1l))
                .willReturn(Optional.of(TEST_CLOTHES_1));
        given(itemRepository.findById(2l))
                .willReturn(Optional.of(TEST_CLOTHES_2));

        Cart TEST_CART = Cart.builder().build();
        given(cartRepository.findByAuthId(TEST_MEMBER.getAuthId()))
                .willReturn(Optional.of(TEST_CART));

        //when
        cartService.addMemberCart(TEST_CARTREQUEST, TEST_MEMBER.getAuthId());

        //then
        verify(itemRepository, atLeastOnce()).findById(1l);
        verify(itemRepository, atLeastOnce()).findById(2l);
        verify(cartRepository, atLeastOnce()).findByAuthId(TEST_MEMBER.getAuthId());
        assertThat(TEST_CART.getCartItems().size(), is(2));
        assertThat(TEST_CART.getCartItems().get(0).getItem().getName(), is(TEST_CLOTHES_1.getName()));
        assertThat(TEST_CART.getCartItems().get(1).getItem().getName(), is(TEST_CLOTHES_2.getName()));
    }

    /**
     * 비회원 장바구니에 상품들을 회원 장바구니로 추가 기능을 테스트합니다.
     */
    @Test
    public void 비회원장바구니_상품들을_회원장바구니로_추가_테스트() throws Exception {
        //given
        Cart TEST_COOKIE_CART = createCookieCart();
        Cart TEST_MEMBER_CART = createMemberCart();
        given(cartRepository.findByCkId(TEST_COOKIE_CART.getCkId()))
                .willReturn(Optional.of(TEST_COOKIE_CART));
        given(cartRepository.findByAuthId("testAuthId"))
                .willReturn(Optional.of(TEST_MEMBER_CART));

        //when
        cartService.updateMemberCartByTempCart(TEST_COOKIE_CART.getCkId(), "testAuthId");

        //then
        verify(cartRepository, atLeastOnce()).findByCkId(any());
        verify(cartRepository, atLeastOnce()).findByAuthId(any());
        assertThat(TEST_MEMBER_CART.getCartItems().size(), is(10));
        assertThat(TEST_MEMBER_CART.getCartItems().get(5).getCartQuantity(), is(TEST_COOKIE_CART.getCartItems().get(0).getCartQuantity()));
        assertThat(TEST_MEMBER_CART.getCartItems().get(5).getSize(), is(TEST_COOKIE_CART.getCartItems().get(0).getSize()));
        verify(cartRepository, atLeastOnce()).delete(any(Cart.class));
    }

    @Test
    public void 회원_장바구니_조회테스트() throws Exception {
        //given
        Cart TEST_MEMBER_CART = createMemberCart();
        given(cartRepository.findByAuthId(any()))
                .willReturn(Optional.of(TEST_MEMBER_CART));

        Category TEST_CATEGORY = Category.builder().build();
        ReflectionTestUtils.setField(TEST_CATEGORY, "categoryId", 1l);

        CategoryItem TEST_CATEGORY_ITEM = new CategoryItem(TEST_CATEGORY);
        given(categoryItemRepository.getCategoryItem(any(), anyLong()))
                .willReturn(Optional.of(TEST_CATEGORY_ITEM));

        //when
        CartDTO memberCartDTO = cartService.getMemberCart(any());

        //then
        // 회원 장바구니 첫번째 상품.
        assertThat(memberCartDTO.getCartItems().size(), is(2));
        assertThat(memberCartDTO.getCartItems().get(TEST_ITEM_ID_1).getRequiredOptions().size(), is(3));
        assertThat(memberCartDTO.getCartItems().get(TEST_ITEM_ID_1).getRequiredOptions().get(0).getItemQuantity(),
                is(TEST_MEMBER_CART.getCartItems().get(0).getCartQuantity()));
        assertThat(memberCartDTO.getCartItems().get(TEST_ITEM_ID_1).getRequiredOptions().get(1).getItemQuantity(),
                is(TEST_MEMBER_CART.getCartItems().get(1).getCartQuantity()));
        assertThat(memberCartDTO.getCartItems().get(TEST_ITEM_ID_1).getRequiredOptions().get(2).getItemQuantity(),
                is(TEST_MEMBER_CART.getCartItems().get(2).getCartQuantity()));

        // 회원 장바구니 첫번째 상품.
        assertThat(memberCartDTO.getCartItems().get(TEST_ITEM_ID_2).getRequiredOptions().size(), is(2));
        assertThat(memberCartDTO.getCartItems().get(TEST_ITEM_ID_2).getRequiredOptions().get(0).getItemQuantity(),
                is(TEST_MEMBER_CART.getCartItems().get(3).getCartQuantity()));
        assertThat(memberCartDTO.getCartItems().get(TEST_ITEM_ID_2).getRequiredOptions().get(1).getItemQuantity(),
                is(TEST_MEMBER_CART.getCartItems().get(4).getCartQuantity()));
    }

    @Test
    public void 비회원_장바구니_조회테스트() throws Exception {
        //given
        Cart TEST_COOKIE_CART = createCookieCart();
        given(cartRepository.findByCkId(any()))
                .willReturn(Optional.of(TEST_COOKIE_CART));

        Category TEST_CATEGORY = Category.builder().build();
        ReflectionTestUtils.setField(TEST_CATEGORY, "categoryId", 1l);

        CategoryItem TEST_CATEGORY_ITEM = new CategoryItem(TEST_CATEGORY);
        given(categoryItemRepository.getCategoryItem(any(), anyLong()))
                .willReturn(Optional.of(TEST_CATEGORY_ITEM));

        //when
        CartDTO cookieCartDTO = cartService.getCookieCart(any());

        //then
        // 비회원 장바구니 첫번째 상품.
        System.out.println("cartDTO: " + cookieCartDTO.toString());
        assertThat(cookieCartDTO.getCartItems().size(), is(2));
        assertThat(cookieCartDTO.getCartItems().get(TEST_ITEM_ID_3).getRequiredOptions().size(), is(3));
        assertThat(cookieCartDTO.getCartItems().get(TEST_ITEM_ID_3).getRequiredOptions().get(0).getItemQuantity(),
                is(TEST_COOKIE_CART.getCartItems().get(0).getCartQuantity()));
        assertThat(cookieCartDTO.getCartItems().get(TEST_ITEM_ID_3).getRequiredOptions().get(1).getItemQuantity(),
                is(TEST_COOKIE_CART.getCartItems().get(1).getCartQuantity()));
        assertThat(cookieCartDTO.getCartItems().get(TEST_ITEM_ID_3).getRequiredOptions().get(2).getItemQuantity(),
                is(TEST_COOKIE_CART.getCartItems().get(2).getCartQuantity()));

        // 비회원 장바구니 두번째 상품.
        assertThat(cookieCartDTO.getCartItems().get(TEST_ITEM_ID_4).getRequiredOptions().size(), is(2));
        assertThat(cookieCartDTO.getCartItems().get(TEST_ITEM_ID_4).getRequiredOptions().get(0).getItemQuantity(),
                is(TEST_COOKIE_CART.getCartItems().get(3).getCartQuantity()));
        assertThat(cookieCartDTO.getCartItems().get(TEST_ITEM_ID_4).getRequiredOptions().get(1).getItemQuantity(),
                is(TEST_COOKIE_CART.getCartItems().get(4).getCartQuantity()));
    }

    /**
     * 장바구니 상품들 중 단일 상품을 주문 요청 정보 DTO로 변환하는 테스트입니다.
     * (테스트하는 필드 정보들은 OrderSummaryRequest 를 참고하세요.)
     */
    @Test
    public void 장바구니_단일상품_주문정보_생성테스트() throws Exception {
        //given
        Long TEST_CART_ID =1l;
        Long TEST_ITEM_ID =1l;
        Long TEST_ITEM_ID_2 =2l;
        CartDTO TEST_CART_DTO = new CartDTO(1l);

        // 장바구니에 들어간 itemId가 1l 인 상품.
        TEST_CART_DTO.getCartItems().put(TEST_ITEM_ID, new CartItemDTO(TEST_ITEM_ID, 1l, "testItemName", 1000l, 1l, SizeLabel.S, 1l));
        TEST_CART_DTO.getCartItems().get(TEST_ITEM_ID).getRequiredOptions().add(new RequiredOption(2l, SizeLabel.M));
        TEST_CART_DTO.getCartItems().get(TEST_ITEM_ID).getRequiredOptions().add(new RequiredOption(3l, SizeLabel.L));

        // 장바구니에 들어간 itemId가 2l인 상품.
        TEST_CART_DTO.getCartItems().put(TEST_ITEM_ID_2, new CartItemDTO(TEST_ITEM_ID_2, 2l, "testItemName2", 2000l, 4l, SizeLabel.S, 1l));
        TEST_CART_DTO.getCartItems().get(TEST_ITEM_ID_2).getRequiredOptions().add(new RequiredOption(5l, SizeLabel.M));
        TEST_CART_DTO.getCartItems().get(TEST_ITEM_ID_2).getRequiredOptions().add(new RequiredOption(6l, SizeLabel.L));
        //when
        OrderSummaryRequest orderSummaryRequest = cartService.transferOrderSummary(TEST_CART_DTO, TEST_CART_ID);

        //then
        assertThat(orderSummaryRequest.getOrderItems().size(), is(1)); // 장바구니 내 상품의 갯수.
        assertThat(orderSummaryRequest.getOrderItems().get(0).getRequiredOptions().size(), is(3)); // 장바구니 내 상품의 필수정보(사이즈, 수량) 갯수.
        assertThat(orderSummaryRequest.getTotalQuantity(), is(6l)); // 장바구니 내 상품의 총 갯수.
        assertThat(orderSummaryRequest.getTotalAmount(), is(6000l)); // 장바구니 내 상품의 총 금액.
        // 장바구니 내 상품의 사이즈, 수량 정보.
        assertThat(orderSummaryRequest.getOrderItems().get(0).getRequiredOptions().get(0).getItemQuantity(), is(1l));
        assertThat(orderSummaryRequest.getOrderItems().get(0).getRequiredOptions().get(0).getItemSize(), is(SizeLabel.S));
        assertThat(orderSummaryRequest.getOrderItems().get(0).getRequiredOptions().get(1).getItemQuantity(), is(2l));
        assertThat(orderSummaryRequest.getOrderItems().get(0).getRequiredOptions().get(1).getItemSize(), is(SizeLabel.M));
        assertThat(orderSummaryRequest.getOrderItems().get(0).getRequiredOptions().get(2).getItemQuantity(), is(3l));
        assertThat(orderSummaryRequest.getOrderItems().get(0).getRequiredOptions().get(2).getItemSize(), is(SizeLabel.L));
    }

    /**
     * 장바구니 상품들 중 모든 상품을 주문 요청 정보 DTO로 변환하는 테스트입니다.
     */
    @Test
    public void 장바구니_모든상품_주문정보_생성테스트() throws Exception {
        //given
        Long TEST_CART_ID =1l;
        Long TEST_ITEM_ID =1l;
        Long TEST_ITEM_ID_2 =2l;
        CartDTO TEST_CART_DTO = new CartDTO(1l);

        // 장바구니에 들어간 itemId가 1l 인 상품.
        TEST_CART_DTO.getCartItems().put(TEST_ITEM_ID, new CartItemDTO(TEST_ITEM_ID, 1l, "testItemName", 1000l, 1l, SizeLabel.S, 1l));
        TEST_CART_DTO.getCartItems().get(TEST_ITEM_ID).getRequiredOptions().add(new RequiredOption(2l, SizeLabel.M));
        TEST_CART_DTO.getCartItems().get(TEST_ITEM_ID).getRequiredOptions().add(new RequiredOption(3l, SizeLabel.L));

        // 장바구니에 들어간 itemId가 2l인 상품.
        TEST_CART_DTO.getCartItems().put(TEST_ITEM_ID_2, new CartItemDTO(TEST_ITEM_ID_2, 2l, "testItemName2", 2000l, 4l, SizeLabel.S, 1l));
        TEST_CART_DTO.getCartItems().get(TEST_ITEM_ID_2).getRequiredOptions().add(new RequiredOption(5l, SizeLabel.M));
        //when
        OrderSummaryRequest orderSummaryRequest = cartService.transferOrderSummary(TEST_CART_DTO);

        //then
        assertThat(orderSummaryRequest.getOrderItems().size(), is(2)); // 장바구니 내 상품의 갯수.

        // 장바구니 내 상품의 필수정보(사이즈, 수량) 갯수.
        assertThat(orderSummaryRequest.getOrderItems().get(0).getRequiredOptions().size(), is(3));
        assertThat(orderSummaryRequest.getOrderItems().get(1).getRequiredOptions().size(), is(2));
        assertThat(orderSummaryRequest.getTotalQuantity(), is(15l)); // 장바구니 내 상품의 총 갯수.
        assertThat(orderSummaryRequest.getTotalAmount(), is(24000l)); // 장바구니 내 상품의 총 금액.

        // 장바구니 내 상품의 사이즈, 수량 정보.
        assertThat(orderSummaryRequest.getOrderItems().get(0).getRequiredOptions().get(0).getItemQuantity(), is(1l));
        assertThat(orderSummaryRequest.getOrderItems().get(0).getRequiredOptions().get(0).getItemSize(), is(SizeLabel.S));
        assertThat(orderSummaryRequest.getOrderItems().get(0).getRequiredOptions().get(1).getItemQuantity(), is(2l));
        assertThat(orderSummaryRequest.getOrderItems().get(0).getRequiredOptions().get(1).getItemSize(), is(SizeLabel.M));
        assertThat(orderSummaryRequest.getOrderItems().get(0).getRequiredOptions().get(2).getItemQuantity(), is(3l));
        assertThat(orderSummaryRequest.getOrderItems().get(0).getRequiredOptions().get(2).getItemSize(), is(SizeLabel.L));

        assertThat(orderSummaryRequest.getOrderItems().get(1).getRequiredOptions().get(0).getItemQuantity(), is(4l));
        assertThat(orderSummaryRequest.getOrderItems().get(1).getRequiredOptions().get(0).getItemSize(), is(SizeLabel.S));
        assertThat(orderSummaryRequest.getOrderItems().get(1).getRequiredOptions().get(1).getItemQuantity(), is(5l));
        assertThat(orderSummaryRequest.getOrderItems().get(1).getRequiredOptions().get(1).getItemSize(), is(SizeLabel.M));
    }

    private Cart createCookieCart(){
        // 1 상품 도메인.
        Clothes TEST_CLOTHES = Clothes.builder()
                .name("testClothesName")
                .price(1000l)
                .build();
        ReflectionTestUtils.setField(TEST_CLOTHES, "itemId", TEST_ITEM_ID_3);

        // 1 상품 사이즈 도메인.
        ClothesSize TEST_CLOTHES_SIZE_S = ClothesSize.builder()
                .stockQuantity(10l)
                .sizeLabel(SizeLabel.S)
                .build();

        ClothesSize TEST_CLOTHES_SIZE_M = ClothesSize.builder()
                .stockQuantity(10l)
                .sizeLabel(SizeLabel.M)
                .build();

        ClothesSize TEST_CLOTHES_SIZE_L = ClothesSize.builder()
                .stockQuantity(10l)
                .sizeLabel(SizeLabel.L)
                .build();

        // 1 상품 이미지 도메인.
        ItemImage TEST_ITEM_IMAGE = ItemImage.builder().build();
        ReflectionTestUtils.setField(TEST_ITEM_IMAGE, "itemImageId", 1l);

        TEST_CLOTHES.changeClothesSize(List.of(TEST_CLOTHES_SIZE_M, TEST_CLOTHES_SIZE_S, TEST_CLOTHES_SIZE_L));
        TEST_CLOTHES.changeItemImages(List.of(TEST_ITEM_IMAGE));

        // 2 상품 도메인.
        Clothes TEST_CLOTHES_2 = Clothes.builder()
                .name("testClothesName2")
                .price(2000l)
                .build();
        ReflectionTestUtils.setField(TEST_CLOTHES_2, "itemId", TEST_ITEM_ID_4);

        // 2 상품 사이즈 도메인.
        ClothesSize TEST_CLOTHES_2_SIZE_S = ClothesSize.builder()
                .stockQuantity(10l)
                .sizeLabel(SizeLabel.S)
                .build();

        ClothesSize TEST_CLOTHES_2_SIZE_M = ClothesSize.builder()
                .stockQuantity(10l)
                .sizeLabel(SizeLabel.M)
                .build();

        ClothesSize TEST_CLOTHES_2_SIZE_L = ClothesSize.builder()
                .stockQuantity(10l)
                .sizeLabel(SizeLabel.L)
                .build();

        TEST_CLOTHES_2.changeClothesSize(List.of(TEST_CLOTHES_2_SIZE_S, TEST_CLOTHES_2_SIZE_M, TEST_CLOTHES_2_SIZE_L));

        // 2 상품 이미지 도메인.
        ItemImage TEST_ITEM_IMAGE_2 = ItemImage.builder().build();
        ReflectionTestUtils.setField(TEST_ITEM_IMAGE, "itemImageId", 1l);
        TEST_CLOTHES_2.changeItemImages(List.of(TEST_ITEM_IMAGE_2));

        // 비회원(쿠키) 장바구니 도메인.
        Cart TEST_COOKIE_CART = Cart.builder()
                .ckId("testCkId")
                .build();

        // 장바구니 상품 도메인 목록.
        List<CartItem> TEST_CART_ITEM_LIST = List.of(
                new CartItem(TEST_CLOTHES, 1l, TEST_CLOTHES_SIZE_M.getSizeLabel()),
                new CartItem(TEST_CLOTHES, 2l, TEST_CLOTHES_SIZE_S.getSizeLabel()),
                new CartItem(TEST_CLOTHES, 3l, TEST_CLOTHES_SIZE_L.getSizeLabel()),

                new CartItem(TEST_CLOTHES_2, 4l, TEST_CLOTHES_2_SIZE_S.getSizeLabel()),
                new CartItem(TEST_CLOTHES_2, 5l, TEST_CLOTHES_2_SIZE_M.getSizeLabel())
        );

        TEST_COOKIE_CART.addCartItems(TEST_CART_ITEM_LIST);

        return TEST_COOKIE_CART;
    }

    private Cart createMemberCart(){
        // 1 상품 도메인.
        Clothes TEST_CLOTHES = Clothes.builder()
                .name("testClothesName")
                .price(1000l)
                .build();
        ReflectionTestUtils.setField(TEST_CLOTHES, "itemId", TEST_ITEM_ID_1);

        // 1 상품 사이즈 도메인.
        ClothesSize TEST_CLOTHES_SIZE_S = ClothesSize.builder()
                .stockQuantity(10l)
                .sizeLabel(SizeLabel.S)
                .build();

        ClothesSize TEST_CLOTHES_SIZE_M = ClothesSize.builder()
                .stockQuantity(10l)
                .sizeLabel(SizeLabel.M)
                .build();

        ClothesSize TEST_CLOTHES_SIZE_L = ClothesSize.builder()
                .stockQuantity(10l)
                .sizeLabel(SizeLabel.L)
                .build();

        // 1 상품 이미지 도메인.
        ItemImage TEST_ITEM_IMAGE = ItemImage.builder().build();
        ReflectionTestUtils.setField(TEST_ITEM_IMAGE, "itemImageId", 1l);

        TEST_CLOTHES.changeItemSizes(List.of(TEST_CLOTHES_SIZE_M, TEST_CLOTHES_SIZE_S, TEST_CLOTHES_SIZE_L));
        TEST_CLOTHES.changeItemImages(List.of(TEST_ITEM_IMAGE));

        // 2 상품 도메인.
        Clothes TEST_CLOTHES_2 = Clothes.builder()
                .name("testClothesName2")
                .price(2000l)
                .build();
        ReflectionTestUtils.setField(TEST_CLOTHES_2, "itemId", TEST_ITEM_ID_2);

        // 2 상품 사이즈 도메인.
        ClothesSize TEST_CLOTHES_2_SIZE_S = ClothesSize.builder()
                .stockQuantity(10l)
                .sizeLabel(SizeLabel.S)
                .build();

        ClothesSize TEST_CLOTHES_2_SIZE_M = ClothesSize.builder()
                .stockQuantity(10l)
                .sizeLabel(SizeLabel.M)
                .build();

        ClothesSize TEST_CLOTHES_2_SIZE_L = ClothesSize.builder()
                .stockQuantity(10l)
                .sizeLabel(SizeLabel.L)
                .build();

        TEST_CLOTHES_2.changeClothesSize(List.of(TEST_CLOTHES_2_SIZE_S, TEST_CLOTHES_2_SIZE_M, TEST_CLOTHES_2_SIZE_L));

        // 2 상품 이미지 도메인.
        ItemImage TEST_ITEM_IMAGE_2 = ItemImage.builder().build();
        ReflectionTestUtils.setField(TEST_ITEM_IMAGE, "itemImageId", 1l);
        TEST_CLOTHES_2.changeItemImages(List.of(TEST_ITEM_IMAGE_2));

        // 회원 장바구니 도메인.
        Cart TEST_MEMBER_CART = Cart.builder().build();

        // 장바구니 상품 도메인 목록.
        List<CartItem> TEST_CART_ITEM_LIST = List.of(
                new CartItem(TEST_CLOTHES, 1l, TEST_CLOTHES_SIZE_M.getSizeLabel()),
                new CartItem(TEST_CLOTHES, 2l, TEST_CLOTHES_SIZE_S.getSizeLabel()),
                new CartItem(TEST_CLOTHES, 3l, TEST_CLOTHES_SIZE_L.getSizeLabel()),

                new CartItem(TEST_CLOTHES_2, 4l, TEST_CLOTHES_2_SIZE_S.getSizeLabel()),
                new CartItem(TEST_CLOTHES_2, 5l, TEST_CLOTHES_2_SIZE_M.getSizeLabel())
        );

        TEST_MEMBER_CART.addCartItems(TEST_CART_ITEM_LIST);

        return TEST_MEMBER_CART;
    }

    @Test
    public void 회원_장바구니_수정_테스트() throws Exception {
        //given
        Cart TEST_CART = mock(Cart.class);
        doNothing().when(TEST_CART).modifyCartItems(any());
        given(cartRepository.findByAuthId(any()))
                .willReturn(Optional.of(TEST_CART));

        Clothes TEST_CLOTHES = Clothes.builder().build();
        given(itemRepository.findById(any()))
                .willReturn(Optional.of(TEST_CLOTHES));

        CartRequest TEST_CART_REQUEST = new CartRequest();
        TEST_CART_REQUEST.setCartItems(List.of(
                new CartLineRequest(1l, 10l, SizeLabel.S, 1l)
        ));

        //when
        cartService.modifyMemberCart(TEST_CART_REQUEST, any());

        //then
        verify(cartRepository,atLeastOnce()).findByAuthId(any());
        verify(itemRepository, atLeastOnce()).findById(any());
        verify(TEST_CART, atLeastOnce()).modifyCartItems(any());
    }

    @Test
    public void 비회원_장바구니_수정_테스트() throws Exception {
        //given
        Cart TEST_TEMP_CART = mock(Cart.class);
        doNothing().when(TEST_TEMP_CART).modifyCartItems(any());
        given(cartRepository.findByCkId(any()))
                .willReturn(Optional.of(TEST_TEMP_CART));

        Clothes TEST_CLOTHES = Clothes.builder().build();
        given(itemRepository.findById(any()))
                .willReturn(Optional.of(TEST_CLOTHES));

        CartRequest TEST_CART_REQUEST = new CartRequest();
        TEST_CART_REQUEST.setCartItems(List.of(
                new CartLineRequest(1l, 10l, SizeLabel.S, 1l)
        ));

        //when
        cartService.modifyTempCart(TEST_CART_REQUEST, any());

        //then
        verify(cartRepository,atLeastOnce()).findByCkId(any());
        verify(itemRepository, atLeastOnce()).findById(any());
        verify(TEST_TEMP_CART, atLeastOnce()).modifyCartItems(any());
    }

}
