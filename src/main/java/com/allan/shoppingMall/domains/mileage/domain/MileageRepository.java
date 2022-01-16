package com.allan.shoppingMall.domains.mileage.domain;

import com.allan.shoppingMall.domains.mileage.domain.model.MileageContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MileageRepository extends JpaRepository<Mileage, Long> {

    /**
     * 로그인 된 아이디에 마일리지를 반환하는 메소드.
     * @param authId 회원 도메인 아이디.
     * @return 마일리지 도메인 리스트.
     */
    List<Mileage> findAllByAuthId(String authId);

    /**
     * 주문번호에 해당하는 마일리지 리스트를 반환하는 메소드.
     * @param orderNum 주문 도메인 주문번호.
     * @return 마일리지 도메인 리스트.
     */
    List<Mileage> findAllByOrderNum(String orderNum);

    /**
     * 마일리지 아이디 리스트로 마일리지를 삭제 할 때 사용하는 메소드로,
     * 결제가 실패하여, 임시상태의 주문을 삭제 할 때, 주문과 관련 된 마일리지도 삭제 하는데 호출 된다.
     * @param mileageIds 삭제 할 마일리지 도메인 id.
     * @return
     */
    @Modifying
    @Query("delete from Mileage m where m.mileageId in :mileageIds")
    int deleteAllByMileageIds(@Param("mileageIds") List<Long> mileageIds);

    /**
     * 특정 주문번호와 마일리지 내용으로 마일리지를 조회하는 메소드.
     * @param orderNum
     * @param mileageContent
     * @return Optional<Mileage>
     */
    Optional<Mileage> findByOrderNumAndMileageContent(String orderNum, MileageContent mileageContent);
}
