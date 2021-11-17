package com.allan.shoppingMall.domains.member.presentation;

import com.allan.shoppingMall.common.api.ApiResponse;
import com.allan.shoppingMall.common.api.status.MemberResponse;
import com.allan.shoppingMall.domains.member.domain.MemberRepository;
import com.allan.shoppingMall.domains.member.domain.model.MemberDTO;
import com.allan.shoppingMall.domains.member.domain.model.MemberRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RestMemberController {

    @Autowired
    private final MemberRepository memberRepository;

    @PostMapping("/member/checkId")
    public ResponseEntity<ApiResponse> checkId(@RequestBody MemberRequest memberRequest) {
        boolean present = memberRepository.findByAuthIdLike(memberRequest.getAuthId()).isPresent();
        if (present) {
            ApiResponse response = new ApiResponse(MemberResponse.IN_USE.getStatus(), MemberResponse.IN_USE.getMessage());
            return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
        }else{
            ApiResponse response = new ApiResponse(MemberResponse.IN_NOT_USE.getStatus(), MemberResponse.IN_NOT_USE.getMessage());
            return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);
        }
    }

}
