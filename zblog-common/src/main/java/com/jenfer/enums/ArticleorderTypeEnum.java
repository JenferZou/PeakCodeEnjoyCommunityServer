package com.jenfer.enums;

public enum ArticleorderTypeEnum {
    HOT(0,"top_type desc,comment_count desc,good_count desc,read_count desc","热榜"),
    SEND(1,"post_time asc","发布"),
    NEW(2,"post_time desc","最新");

    private Integer type;
    private String orderSql;
    private String desc;

    ArticleorderTypeEnum(Integer type, String orderSql, String desc) {
        this.type = type;
        this.orderSql = orderSql;
        this.desc = desc;
    }

    ArticleorderTypeEnum() {
    }

    public Integer getType() {
        return type;
    }

    public String getOrderSql() {
        return orderSql;
    }

    public String getDesc() {
        return desc;
    }

    public static ArticleorderTypeEnum getByType(Integer type){
        for(ArticleorderTypeEnum item : ArticleorderTypeEnum.values()){
            if(item.getType().equals(type)){
                return item;
            }
        }
        return null;
    }

}
