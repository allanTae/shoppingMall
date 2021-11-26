package com.allan.shoppingMall.domains.member.domain;

import com.allan.shoppingMall.common.domain.BaseTimeEntity;
import com.allan.shoppingMall.common.value.Address;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long memberId;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false, unique = true, length = 20)
    private String authId;

    @Column(nullable = false, length = 20)
    private String pwd;

    @Embedded
    private Address address;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MemberRole role;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, name = "phone")
    private String phone;

    // 생년월일
    @Column(nullable = false, name="date_of_birth")
    private String dateOfBirth;

    @Column(nullable = false)
    private Long milege;

    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    /**
     * member 엔티티의 경우 초기 가입시 로그인 된 아이디를 사용하여 생성자와 수정자를 설정하는
     * JpaAuditing LoginIdAuditorAware 를 사용하는 불가능하기 때문에
     * 엔티티 최초 persist 시점에 생성하여 입력하도록 한다.
     */
    @PrePersist
    public void setUp(){
        this.milege = 100l;
        this.createdBy = "system";
        this.updatedBy = "system";
        // TaeTae 애플리케이션 서비스에서 초기 가입시, 닉네임 설정은 아이디로 설정.
        if(role.getJoinType().getType().equals(JoinType.NORMAL.getType())){
            this.nickName = this.authId;
        }else{
            // 서비스 가입아 아닌 Oauth2 가입시 닉네임 설정.
        }
    }

    /**
     * Member 엔티티의 정보 수정 프로세스는 크게 2가지이다.
     * 1. 회원 자신의 정보 수정.
     * 2. '비밀번호 찾기' 로 인한 system 에 의한 수정.
     */
    @PreUpdate
    public void preUpdate(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 비밀번호 찾기 기능.
        if(null == authentication || !authentication.isAuthenticated()){
            updatedBy = "system";
        }
        // 회원 정보 수정 기능.
        else if(authentication.isAuthenticated()){
            UserDetails user = (UserDetails) authentication.getPrincipal();
            if(this.authId.equals(user.getUsername())){
                updatedBy = user.getUsername();
            }else{
                throw new AccessDeniedException("본인 정보는 자신만 수정이 가능합니다.");
            }
        }
    }

    @Builder
    public Member(Long memberId, int age, String authId, String pwd, Address address, Gender gender, String email, String phoneNumber, String dateOfBirth, Long milege, String name, String nickName, MemberRole role) {
        this.memberId = memberId;
        this.age = age;
        this.authId = authId;
        this.pwd = pwd;
        this.address = address;
        this.gender = gender;
        this.email = email;
        this.phone = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.milege = milege;
        this.name = name;
        this.nickName = nickName;
        this.role = role;
    }
}
