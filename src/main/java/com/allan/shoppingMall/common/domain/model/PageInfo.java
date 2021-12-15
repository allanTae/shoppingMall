package com.allan.shoppingMall.common.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageInfo {

    private int rangeSize = 10; //하나의 그룹 크기(= 1개의 그룹이 가지는 페이지의 갯수) Ex) 1,2,3,4,5,6,7,8,9,10 => 1그룹.
    private int page; // 현재 페이지 번호.
    private int totalPageCnt; // 총 페이지 수.
    private int startPage; // 현재 그룹번호의 시작 페이지 번호.
    private int endPage; // 현재 그룹번호의 마지막 페이지 번호.
    private boolean isPrev; // 이전 페이지 여부.
    private boolean isNext; // 다음 페이지 여부.

    public PageInfo(int page, int totalPageCnt, boolean isFirst, boolean isLast){
        this.page = page;
        this.totalPageCnt = totalPageCnt;
        this.setPageInfo(isFirst, isLast);
    }

    public boolean getIsPrev(){
        return this.isPrev;
    }

    public boolean getIsNext(){
        return this.isNext;
    }

    private void setPageInfo(boolean isFirst, boolean isLast){

        // 시작 페이지.
        this.startPage = (int) (Math.floor(this.page/this.rangeSize) * this.rangeSize + 1);

        // 끝 페이지.
        this.endPage = (this.startPage + (this.rangeSize -1)) < this.totalPageCnt ? this.startPage + (this.rangeSize - 1) : this.totalPageCnt;

        // 이전 페이지 여부.
        this.isPrev = isFirst == false ? true : false;

        // 다음 페이지 여부.
        this.isNext = isLast == false ? true : false;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "rangeSize=" + rangeSize +
                ", page=" + page +
                ", totalPageCnt=" + totalPageCnt +
                ", startPage=" + startPage +
                ", endPage=" + endPage +
                ", isPrev=" + isPrev +
                ", isNext=" + isNext +
                '}';
    }
}
