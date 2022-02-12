package com.allan.shoppingMall.common.util;

/**
 * data 형식을 변경 할 때 사용하는 클래스.
 */
public class FormatUtil {

    /**
     * 전화번호 양식에 맞추어 문자열을 반환하는 메소드.
     * @param phoneNumber 전화번호.
     * @return 전화번호 종류에 따라 양식을 맞춘 전화번호.
     */
    public static String phoneFormat(String phoneNumber){
        if (phoneNumber == null) {
            return "";
        }
        if (phoneNumber.length() == 8) {
            return phoneNumber.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2");
        } else if (phoneNumber.length() == 12) {
            return phoneNumber.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$", "$1-$2-$3");
        }
        return phoneNumber.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
    }

}
