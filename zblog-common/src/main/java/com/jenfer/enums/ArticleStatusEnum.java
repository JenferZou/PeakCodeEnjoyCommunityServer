package com.jenfer.enums;

public enum ArticleStatusEnum {

    AUDIT(1,"已审核"),
    DEL(-1,"已删除"),
    NO_AUDIT(0,"未审核");

    private Integer status;

    private String desc;

    ArticleStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
