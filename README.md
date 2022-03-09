# shoppingMall

## 0. Index
1. Overview
2. Environment
3. Getting Started
4. Function

## 1. Overview
Spring Boot + JPA를 활용한 개인 의류 쇼핑몰 프로젝트입니다. 이 프로젝트는 의류 디자이너 동료와 함께 포트폴리오를 목적으로 만들어졌으며, 현재도 개선중에 있습니다. 앞으로도 성능향상을 위한 리팩토링과 동료의 요청으로 인한 기능 추가시 계속해서 갱신 할 예정입니다.

## 2. Environment
프로젝트의 구성환경은 다음과 같습니다.
+ Mac OS Catalina(10.15.2)
+ Java 11
+ Spring boot(2.5.6)
+ IntelliJ 2020.3 Community
+ h2 database(1.4.199)
+ gradle(7.2)
+ JPA
+ bootstrap(5.1.3)

## 3. Getting Started
결제 기능은 Iamport 외부 api를 통해 동작합니다. 현재 프로젝트에는 보안을 위해
Iamport 설정파일은 포함되어 있지 않습니다.
프로젝트에 application-iamport을 추가 해 주셔야 합니다.

```
// application-iamport 참고.
payment:
  iamport:
    apiKey: "testApiKey"
    apiSecret: "testApiSecret"
```

iamport 에 대한 자세한 내용은 [공식문서](https://docs.iamport.kr/)를 참고하시길 바랍니다.

## 4. Function
쇼핑몰의 주요 기능으로는 다음과 같습니다.

### 4-1 벡엔드
벡엔드 코드는 spring boot를 기반으로 코딩되어 있습니다. 대표적인 기능들의 내용은 아래의 표를 참고 하시길 바랍니다.

###  🍎 벡엔드 주요 기능.

| 순번 | 그룹 |주요기능 |
| ------ | -- |----------- |
| 1.| 회원 | 회원가입 |
| 2. | 상품| 상품등록 및 상품조회 |
| 3. | 주문| 상품주문 |
| 4. | 결제| 주문결제 |
| 5. | 장바구니|장바구니 등록 및 수정 |
| 6. | 카테고리|카테고리 등록, 수정, 삭제, 변경 |

#### 1) 회원
----------
회원의 종류에는 크게 관리자, 사용자가 있습니다. 사용자는 쇼핑몰을 이용하는 일반회원을 의미하며, 관리자는 쇼핑몰을 관리하는 관리자를 의미합니다.

일반회원이 사용 할 수 있는 기능은 다음과 같습니다.
1. 회원 가입 기능.
2. 회원 가입시, 중복 회원 아이디 조회 기능.
3. 회원 가입시, 정보 유효성 검사 기능.

관리자회원의 경우 추가적으로 '상품등록' 기능을 이용 할 수 있습니다.

#### 2) 상품
-----------
상품 도메인의 경우, 확장 가능하도록 설계가 되어 있습니다. 공통의 요소를 [상품 도메인](https://github.com/allanTae/shoppingMall/blob/develop/src/main/java/com/allan/shoppingMall/domains/item/domain/item/Item.java)를 캡슐화 한 후, [의류상품](https://github.com/allanTae/shoppingMall/blob/develop/src/main/java/com/allan/shoppingMall/domains/item/domain/clothes/Clothes.java), [악세서리상품 도메인](https://github.com/allanTae/shoppingMall/blob/develop/src/main/java/com/allan/shoppingMall/domains/item/domain/accessory/Accessory.java) 같이 구체적인 상품 도메인들이 상속받도록 구성되어 있습니다.

1. 의류 상품 등록 기능.
2. 의류 상품 조회 기능.

의류 상품 등록 기능은 관리자 회원만 가능합니다.

#### 3) 주문
-----------
주문 프로세는 다음과 같은 순서로 이루어집니다.

[![](https://mermaid.ink/img/pako:eNqrVkrOT0lVslJKL0osyFDwCYrJc9R4M6_lTfecN4v3vF6zQ1NBV1fh1aYNbxbMeTOz5fXiHqAMUMhOwUkDogAiqhmT5wRS-aa58e2kDoW3PSveNi15s3yqwusNG960LXwzp-VtcyNMq4sGRNmbJQ2vd7a8WTIRrhti4vYVb9rg9jhrIIsCVbqAVCIsyC-C2qEQAVEPUWGn4KoBtRvJFlewjBtcBiTmBhZzh4lBvKOko5SbWpSbmJkCDJvqmDwFhRilkozU3NQYJSsgMyU1LbE0pyRGKSavFqi0tCAlsSTVNSWzJL9IySotMac4VUcpsbQkP7gyL1nJqqSoNBWmyCUzERjUuVBVtQDxdrJu)](https://mermaid-js.github.io/mermaid-live-editor/edit#pako:eNqrVkrOT0lVslJKL0osyFDwCYrJc9R4M6_lTfecN4v3vF6zQ1NBV1fh1aYNbxbMeTOz5fXiHqAMUMhOwUkDogAiqhmT5wRS-aa58e2kDoW3PSveNi15s3yqwusNG960LXwzp-VtcyNMq4sGRNmbJQ2vd7a8WTIRrhti4vYVb9rg9jhrIIsCVbqAVCIsyC-C2qEQAVEPUWGn4KoBtRvJFlewjBtcBiTmBhZzh4lBvKOko5SbWpSbmJkCDJvqmDwFhRilkozU3NQYJSsgMyU1LbE0pyRGKSavFqi0tCAlsSTVNSWzJL9IySotMac4VUcpsbQkP7gyL1nJqqSoNBWmyCUzERjUuVBVtQDxdrJu)

주문 도메인의 상태 정보에 대한 내용은 [OrderStatus](https://github.com/allanTae/shoppingMall/blob/develop/src/main/java/com/allan/shoppingMall/domains/order/domain/OrderStatus.java) class 를 참조하시길 바랍니다.

주문 프로세스를 위해 [주문 도메인](https://github.com/allanTae/shoppingMall/blob/develop/src/main/java/com/allan/shoppingMall/domains/order/domain/Order.java), [주문 상품 도메인](https://github.com/allanTae/shoppingMall/blob/develop/src/main/java/com/allan/shoppingMall/domains/order/domain/OrderItem.java)이 존재합니다. 주문 상품 도메인은 주문 도메인과 상품 도메인의 다대다 연관관계를 위한 중간 테이블 역할을 수행합니다. 자세한 내용은 링크를 참고하시길 바랍니다.

주문 삭제 기능의 경우, 사용자가 임의의 주문건을 삭제하기 위해서가 아닌, '임시 주문' 상태의 주문건을 삭제하기 위한 기능입니다.

1. 개별 상품 주문, 장바구니 상품 주문 기능.
2. 상품 주문 취소 기능.
3. 상품 주문 삭제 기능(for 임시주문).
4. 주문시, 주문 유효성 검사 기능.
5. my 주문 조회 기능.

#### 4) 장바구니
--------------
장바구니의 종류는 '비회원 장바구니', '회원 장바구니' 2가지가 있습니다. 비회원 장바구니는 쿠키를 통해 저장되며, 로그인시 회원 장바구니에 비회원 장바구니 상품이 등록 됩니다.

1. 비회원, 회원 장바구니 생성 및 상품 추가 기능.
2. 비회원 장바구니 상품을 회원 장바구니로 갱신 기능.
3. 장바구니 상품 수정 기능.

#### 5) 결제
-----------
결제는 iamport api(결제 모듈)을 통해서 실질적인 결제가 이루어집니다. 결제가 이루어진 이후 임시저장 상태의 주문과 결제정보의 유효성 검사 이후 주문이 완료 됩니다.

1. 결제 기능.
2. 결제시, 결제 유효성 검사 기능.
3. 결제 정보 저장 기능.

#### 6) 카테고리
--------------
카테고리는 상품 도메인을 분류하기 위한 도메인입니다.
카테고리 프로세스를 위해 [카테고리 도메인](https://github.com/allanTae/shoppingMall/blob/develop/src/main/java/com/allan/shoppingMall/domains/category/domain/Category.java)과 [카테고리 상품 도메인](https://github.com/allanTae/shoppingMall/blob/develop/src/main/java/com/allan/shoppingMall/domains/category/domain/CategoryItem.java)이 존재합니다.

카테고리 상품 도메인은 상품 도메인과 카테고리 도메인의 다대다 연관관계를 위한 중간 테이블 역할을 수행합니다.

상위 카테고리와 하위 카테고리는 self reference table 로 설계되어 있습니다. 조회시, 부모 카테고리를 조회한 후, 재귀처리를 통해 하위 카테고리를 가져오도록 설계하였습니다.(자세한 내용은 [참고](https://github.com/allanTae/shoppingMall/blob/develop/src/main/java/com/allan/shoppingMall/domains/category/domain/model/CategoryDTO.java) 링크를 확인 해 주시기 바랍니다.)

[CategoryCode](https://github.com/allanTae/shoppingMall/blob/develop/src/main/java/com/allan/shoppingMall/domains/category/domain/CategoryCode.java) 는 카테고리에서 상품 도메인의 종류를 구분하는 역할을 수행합니다.

등록되는 상품의 종류를 제한하는 역할과 더불어 상품 상세페이지에 필요한 상품 도메인의 종류를 구분하는 역할을 수행 합니다.


카테고리의 경우, 아래의 내용과 같은 CRUD 과정을 rest api 로 제공합니다.
1. 카테고리 추가 기능.
2. 카테고리 수정 기능.
3. 카테고리 삭제 기능.
4. 카테고리 조회 기능.

### 4-2 프론트 엔드
프론트단은 부트 스트랩을 기반으로 하여 제작되었습니다. 제공하는 기능은 다음과 같습니다.

###  🍎 프론트단 주요 기능.

| 순번 | 그룹 |주요페이지 | 주요기능 |
| ------ | -- |----------- | ---|
| 0.| index | index 페이지 | |
| 1.| 회원 | 회원가입 페이지 | 회원가입 유효성 검사 기능 |
| 2. | 상품| 상품 상세 페이지| 장바구니 추가 기능, 주문 기능 |
| | 상품| 상품 등록페이지 | 의류 상품 등록 기능, 악세서리 상품 등록 기능 |
| 3. | 주문| 주문 페이지 | 주문 유효성 검사 기능 |
|  | 주문| my 주문 목록 및 상세 페이지 | |
| 4. | 결제| 결제 완료 페이지 | |
| 5. | 장바구니|장바구니 목록 페이지 |장바구니 수정 기능 by modal. |
| 6. | 카테고리|카테고리별 상품 목록 페이지 | |
