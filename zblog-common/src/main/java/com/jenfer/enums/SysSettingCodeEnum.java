package com.jenfer.enums;

public enum SysSettingCodeEnum {
    AUDIT("audit","com.jenfer.dto.SysSettingAuditDto","sysSettingAuditDto","审核设置"),
    COMMENT("comment","com.jenfer.dto.SysSettingCommentDto","sysSettingCommentDto","评论设置"),
    POST("post","com.jenfer.dto.SysSettingPostDto","sysSettingPostDto","帖子设置"),
    LIKE("like","com.jenfer.dto.SysSettingLikeDto","sysSettingLikeDto","点赞设置"),
    EMAIL("email","com.jenfer.dto.SysSettingEmailDto","sysSettingEmailDto","邮件设置"),
    REGISTER("register","com.jenfer.dto.SysSettingRegisterDto","sysSettingRegisterDto","注册设置")
    ;
    private String code;
    private String clazz;
    private String propName;
    private String desc;

    public static SysSettingCodeEnum getByCode(String code){
        for(SysSettingCodeEnum item:SysSettingCodeEnum.values()){
            if(item.getCode().equals(code)){
                return item;
            }
        }
        return null;
    }

    SysSettingCodeEnum(String code,String clazz,String propName,String desc){
        this.code = code;
        this.clazz=clazz;
        this.propName = propName;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
