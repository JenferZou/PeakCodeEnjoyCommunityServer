package com.jenfer.enums;


public enum CommentStatusEnum {
    DEL(-1, "删除"),
    NO_AUDIT(0,"待审核"),
    AUDIT(1,"已审核");

    private Integer status;

    private String desc;

    CommentStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }



    public String getDesc() {
        return desc;
    }

    public static CommentStatusEnum getByStatus(Integer status){
        for(CommentStatusEnum statusEnum : CommentStatusEnum.values()){
            if(statusEnum.getStatus().equals(status)){
                return statusEnum;
            }
        }
        return null;
    }


}
