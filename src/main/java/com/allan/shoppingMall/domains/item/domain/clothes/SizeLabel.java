package com.allan.shoppingMall.domains.item.domain.clothes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SizeLabel {

    S(1, "small size", "S"),
    M(2, "medium size", "M"),
    L(3, "large size", "L");

    private int id;
    private String desc;
    private String key;

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
