package com.jenfer.enums;

public enum EditTypeEnum {
    RICH(0,"富文本"),
    MARKDOWN(1,"Markdown");

    private Integer type;

    private String desc;

    EditTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }


    public static EditTypeEnum getByType(Integer type){
        for (EditTypeEnum editTypeEnum : EditTypeEnum.values()) {
            if (editTypeEnum.type.equals(type)) {
                return editTypeEnum;
            }
        }
        return null;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
