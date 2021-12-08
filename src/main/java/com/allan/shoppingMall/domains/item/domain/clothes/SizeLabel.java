package com.allan.shoppingMall.domains.item.domain.clothes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SizeLabel {

    S(1, "S", "small size"),
    M(2, "M", "medium size"),
    L(3, "L", "large size");

    private int id;
    private String key;
    private String desc;

    public static SizeLabel valueOf(int id){
        switch(id){
            case 1:
                return S;
            case 2:
                return M;
            case 3:
                return L;
            default:
                throw new AssertionError("존재하지 않는 사이즈 정보입니다: " + id);
        }
    }
}
