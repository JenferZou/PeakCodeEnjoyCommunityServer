package com.jenfer.enums;

public enum ArticleAttachmentTypeEnum {
    NP_ATTACHMENT(0,"没有附件"),
    HAVE_ATTACHMENT(1,"有附件");

    private Integer type;


    private String desc;

    private ArticleAttachmentTypeEnum(Integer type,  String desc){
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static ArticleAttachmentTypeEnum getByType(Integer type){
        for(ArticleAttachmentTypeEnum attachmentFileTypeEnum : ArticleAttachmentTypeEnum.values()){
            if(attachmentFileTypeEnum.getType().equals(type)){
                return attachmentFileTypeEnum;
            }
        }
        return null;
    }
}
