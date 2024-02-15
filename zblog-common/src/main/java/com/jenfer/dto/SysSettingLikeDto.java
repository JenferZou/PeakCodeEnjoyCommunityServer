package com.jenfer.dto;

import com.jenfer.annotation.VerifyParam;

public class SysSettingLikeDto {

    /**
     * 点赞量阈值
     */
    @VerifyParam(required = true)
    private Integer likeDayCountThreshold;

    public Integer getLikeDayCountThreshold() {
        return likeDayCountThreshold;
    }

    public void setLikeDayCountThreshold(Integer likeDayCountThreshold) {
        this.likeDayCountThreshold = likeDayCountThreshold;
    }
}
