package com.jenfer.enums;

public enum UpdateArticleTypeEnum {
    READ_COUNT(0,"阅读数"),
    GOOD_COUNT(1,"点赞数"),
    COMMENT(2,"评论数");

    private Integer type;
    private String desc;

    UpdateArticleTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    UpdateArticleTypeEnum() {
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
