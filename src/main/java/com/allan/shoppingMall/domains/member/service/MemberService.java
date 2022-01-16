package com.allan.shoppingMall.domains.member.service;

import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.value.Address;
import com.allan.shoppingMall.domains.member.domain.Gender;
import com.allan.shoppingMall.domains.member.domain.Member;
import com.allan.shoppingMall.domains.member.domain.MemberRepository;
import com.allan.shoppingMall.domains.member.domain.MemberRole;
import com.allan.shoppingMall.domains.member.domain.model.MemberForm;
import com.allan.shoppingMall.domains.mileage.domain.model.MileageContent;
import com.allan.shoppingMall.domains.mileage.service.MileageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final MileageService mileageService;

    @Transactional
    public Long join(MemberForm form){

        Member member = Member.builder()
                .role(MemberRole.ACTIVATED_USER)
                .name(form.getName())
                .age(form.getAge())
                .authId(form.getAuthId())
                .pwd(form.getPwd())
                .address(Address.builder()
                        .postCode(form.getPostCode())
                        .detailAddress(form.getDetailAddress())
                        .jibunAddress(form.getJibunAddress())
                        .roadAddress(form.getRoadAddress())
                        .extraAddress(form.getExtraAddress())
                        .build())
                .email(form.getEmail())
                .gender(Gender.valueOf(Integer.parseInt(form.getGender())))
                .dateOfBirth(form.getDateOfBirth())
                .phone(form.getPhone())
                .build();

        if(validateAuthId(member.getAuthId())){
            memberRepository.save(member);
        }

        // 회원가입시, 3000 마일리지 적립.
        mileageService.accumulateMileage("", form.getAuthId(), 3000l, MileageContent.JOIN_MILEAGE_ACCUMULATE);

        return member.getMemberId();
    }

    private boolean validateAuthId(String authId){
        boolean present = memberRepository.findByAuthIdLike(authId).isPresent();
        if(!present){
            return true;
        }else{
            throw new SameIdUseException(ErrorCode.MEMBER_AUTH_ID_DUPLICATION.getMessage(), ErrorCode.MEMBER_AUTH_ID_DUPLICATION);
        }
    }


}
