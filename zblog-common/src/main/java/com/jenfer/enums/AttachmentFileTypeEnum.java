package com.jenfer.enums;

public enum AttachmentFileTypeEnum {
    ZIP(0,new String[]{".zip",".rar"},"压缩包");

    private Integer type;
    private String[] suffixs;

    private String desc;

    private AttachmentFileTypeEnum(Integer type,String[] suffixs,String desc){
        this.type = type;
        this.suffixs = suffixs;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String[] getSuffixs() {
        return suffixs;
    }

    public String getDesc() {
        return desc;
    }

    public static AttachmentFileTypeEnum getAttachmentFileTypeEnum(Integer type){
        for(AttachmentFileTypeEnum attachmentFileTypeEnum : AttachmentFileTypeEnum.values()){
            if(attachmentFileTypeEnum.getType().equals(type)){
                return attachmentFileTypeEnum;
            }
        }
        return null;
    }
}
