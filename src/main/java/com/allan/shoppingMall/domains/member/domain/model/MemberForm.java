package com.allan.shoppingMall.domains.member.domain.model;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberForm {

    @NotEmpty(message = "이름을 입력 해 주세요.")
    private String name;

    @NotEmpty(message = "아이디를 입력 해 주세요.")
    private String authId;

    @Max(message = "나이를 제대로 입력 해 주세요.", value = 150)
    @Min(message = "나이를 제대로 입력 해 주세요.", value = 0)
    private int age;

    @NotEmpty(message = "비밀번호를 입력 해 주세요.")
    private String pwd;

    @NotEmpty(message = "비밀번호 확인을 입력 해 주세요.")
    private String rePwd;

    @NotEmpty(message = "지번주소를 입력 해 주세요.")
    private String jibunAddress;

    @NotEmpty(message = "도로명주소를 입력 해 주세요.")
    private String roadAddress;

    @NotEmpty(message = "우편번호를 입력 해 주세요.")
    private String postCode;

    @NotEmpty(message = "상세 주소를 입력 해 주세요.")
    private String detailAddress;

    private String extraAddress;

    @NotEmpty(message = "성별을 선택 해 주세요.")
    private String gender;

    @NotEmpty(message = "이메일을 입력 해 주세요.")
    private String email;

    @NotEmpty(message = "전화번호를 입력 해 주세요.")
    private String phone;

    private String year;
    private String month;
    private String day;
    private String dateOfBirth; // server 에서 전달받은 year, month, day 정보로 생성하는 dateOfBirth 정보.

}
