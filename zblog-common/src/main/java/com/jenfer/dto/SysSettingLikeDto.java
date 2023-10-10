package com.jenfer.dto;

public class SysSettingLikeDto {

    /**
     * 点赞量阈值
     */
    private Integer likeDayCountThreshold;

    public Integer getLikeDayCountThreshold() {
        return likeDayCountThreshold;
    }

    public void setLikeDayCountThreshold(Integer likeDayCountThreshold) {
        this.likeDayCountThreshold = likeDayCountThreshold;
    }
}
