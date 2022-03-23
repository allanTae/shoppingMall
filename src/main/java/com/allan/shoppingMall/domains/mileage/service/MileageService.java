package com.allan.shoppingMall.domains.mileage.service;

import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.mileage.MileageDeductFailException;
import com.allan.shoppingMall.common.exception.mileage.MileageNotFoundException;
import com.allan.shoppingMall.domains.mileage.domain.Mileage;
import com.allan.shoppingMall.domains.mileage.domain.MileageRepository;
import com.allan.shoppingMall.domains.mileage.domain.model.MileageContent;
import com.allan.shoppingMall.domains.mileage.domain.model.MileageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MileageService {

    private final MileageRepository mileageRepository;

    /**
     * 마일리지를 적립하기 위한 메소드.
     * @param orderNum 주문 도메인의 주문번호.
     * @param authId 회원 도메인의 아이디.
     * @param accumulatePoint 적립 할 포인트.
     * @param mileageContent 마일리지 내용.
     * @return 마일리지 도메인의 id.
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public Long accumulateMileage(String orderNum, String authId, Long accumulatePoint, MileageContent mileageContent){
        Mileage accumulateMileage = Mileage.builder()
                .orderNum(orderNum)
                .authId(authId)
                .point(accumulatePoint)
                .mileageContent(mileageContent)
                .build();

        mileageRepository.save(accumulateMileage);

        return accumulateMileage.getMileageId();
    }

    /**
     * 마일리지를 차감하기 위한 메소드.
     * @param orderNum 주문 도메인의 주문번호.
     * @param authId 회원 도메인의 아이디.
     * @param deductPoint 차감 할 포인트.
     * @param mileageContent 마일리지 내용.
     * @return 마일리지 도메인의 id.
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public Long deductMileage(String orderNum, String authId, Long deductPoint, MileageContent mileageContent){
        List<Mileage> mileageList = mileageRepository.findAllByAuthId(authId);

        if(mileageList.size() > 0){
            long pointSum = mileageList.stream()
                    .mapToLong(mileage -> {
                        return mileage.getPoint();
                    })
                    .sum();
            if(pointSum < deductPoint)
                throw new MileageDeductFailException(ErrorCode.MILEAGE_POINT_NOT_ENOUGH_FOR_DEDUCT);
        }else{
            throw new MileageDeductFailException(ErrorCode.MILEAGE_POINT_NOT_ENOUGH_FOR_DEDUCT);
        }

        Mileage deductMileage = Mileage.builder()
                .orderNum(orderNum)
                .authId(authId)
                .point(deductPoint)
                .mileageContent(mileageContent)
                .build();

        mileageRepository.save(deductMileage);

        return deductMileage.getMileageId();
    }

    /**
     * 주문번호에 해당하는 모든 마일리지를 조회하여, 삭제하는 메소드.
     * @param orderNum 주문 도메인의 주문번호.
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public void deleteMileage(String orderNum){
        List<Long> milegeIds = mileageRepository.findAllByOrderNum(orderNum)
                .stream()
                .map(mileage -> {
                    return mileage.getMileageId();
                }).collect(Collectors.toList());

        mileageRepository.deleteAllByMileageIds(milegeIds);
    }

    /**
     * 주문번호로 마일리지를 조회하는 메소드.
     * @param orderNum 주문 도메인의 주문번호.
     * @param mileageContent 마일리지 내용.
     * @return
     */
    // 조회 안되는 경우, 빈값이 들어가는지 확인 필요.
    public MileageDTO getMileageByOrderNum(String orderNum, MileageContent mileageContent){
        boolean present = mileageRepository.findByOrderNumAndMileageContent(orderNum, mileageContent).isPresent();
        if(present){
            Mileage mileage = mileageRepository.findByOrderNumAndMileageContent(orderNum, mileageContent).get();
            return new MileageDTO(mileage.getPoint(), mileageContent.getDesc());
        }else
            return new MileageDTO(0l, "");
    }

    /**
     * @param authId 회원 도메인의 아이디.
     * @return 사용가능한 총 마일리지 포인트.
     */
    public long getAvailableMileagePoint(String authId){
        List<Mileage> mileageList = mileageRepository.findAllByAuthId(authId);
        if(mileageList.size() > 0){
            long pointSum = mileageList.stream()
                    .mapToLong(mileage -> mileage.getPoint())
                    .sum();
            return pointSum;
        }else
            return 0l;
    }
}
