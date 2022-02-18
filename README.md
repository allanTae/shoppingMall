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
1. 의류 상품 등록 기능.
2. 의류 상품 조회 기능.

의류 상품 등록 기능은 관리자 회원만 가능합니다.

#### 3) 주문
-----------
주문 프로세는 다음과 같은 순서로 이루어집니다.

[![](https://mermaid.ink/img/pako:eNqrVkrOT0lVslJKL0osyFDwCYrJc9R4M6_lTfecN4v3vF6zQ1NBV1fh1aYNbxbMeTOz5fXiHqAMUMhOwUkDogAiqhmT5wRS-aa58e2kDoW3PSveNi15s3yqwusNG960LXwzp-VtcyNMq4sGRNmbJQ2vd7a8WTIRrhti4vYVb9rg9jhrIIsCVbqAVCIsyC-C2qEQAVEPUWGn4KoBtRvJFlewjBtcBiTmBhZzh4lBvKOko5SbWpSbmJkCDJvqmDwFhRilkozU3NQYJSsgMyU1LbE0pyRGKSavFqi0tCAlsSTVNSWzJL9IySotMac4VUcpsbQkP7gyL1nJqqSoNBWmyCUzERjUuVBVtQDxdrJu)](https://mermaid-js.github.io/mermaid-live-editor/edit#pako:eNqrVkrOT0lVslJKL0osyFDwCYrJc9R4M6_lTfecN4v3vF6zQ1NBV1fh1aYNbxbMeTOz5fXiHqAMUMhOwUkDogAiqhmT5wRS-aa58e2kDoW3PSveNi15s3yqwusNG960LXwzp-VtcyNMq4sGRNmbJQ2vd7a8WTIRrhti4vYVb9rg9jhrIIsCVbqAVCIsyC-C2qEQAVEPUWGn4KoBtRvJFlewjBtcBiTmBhZzh4lBvKOko5SbWpSbmJkCDJvqmDwFhRilkozU3NQYJSsgMyU1LbE0pyRGKSavFqi0tCAlsSTVNSWzJL9IySotMac4VUcpsbQkP7gyL1nJqqSoNBWmyCUzERjUuVBVtQDxdrJu)

주문 도메인의 상태 정보에 대한 내용은 [OrderStatus](https://github.com/allanTae/shoppingMall/blob/develop/src/main/java/com/allan/shoppingMall/domains/order/domain/OrderStatus.java) class 를 참조하시길 바랍니다.

주문 삭제 기능의 경우, 사용자가 임의의 주문건을 삭제하기 위해서가 아닌, '임시 주문' 상태의 주문건을 삭제하기 위한 기능입니다.

1. 개별 상품 주문, 장바구니  기능.
2. 상품 주문 취소 기능.
3. 상품 주문 삭제 기능(for 임시주문).
4. 주문시, 주문 유효성 검사 기능.

#### 4) 장바구니
--------------
장바구니의 종류는 '비회원 장바구니', '회원 장바구니' 2가지가 있습니다. 비회원 장바구니는 쿠키를 통해 저장되며, 로그인시 회원 장바구니에 비회원 장바구니 상품이 등록 됩니다.

1. 비회원, 회원 장바구니 생성 및 상품 추가 기능.
2. 비회원 장바구니 상품을 회원 장바구니로 갱신 기능.
3. 장바구니 상품 수정 기능.

#### 5) 결제
-----------
결제는 iamport api를 통해서 실질적인 결제가 이루어집니다. 결제가 이루어진 이후
임시저장 상태의 주문과 결제정보의 유효성 검사 이후 주문이 완료 됩니다.

1. 결제 기능.
2. 결제시, 결제 유효성 검사 기능.
