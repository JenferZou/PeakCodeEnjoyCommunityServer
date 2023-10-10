package com.jenfer.enums;

public enum VerifyRegexEnum {

    NO("","不校验"),

    IP("^(((\\d)|([1-9]\\d)|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))\\.){3}((\\d)|([1-9]\\d)|(1\\d{2})|(2[0-4]\\d)|(25[0-5]))$","IP地址"),

    POSITIVE_INTEGRE("^[0-9]*[1-9][0-9]*$","正整数"),

    NUMBER_LETTER_UDDER_LINE("^\\w+$","由数字、26个英文字母或者下划线组成的字符串"),

    EMAIL("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$","邮箱"),

    PHONE("^[1][3,4,5,6,7,8,9][0-9]{9}$","手机号码"),

    COMMENT("^[\\u4E00-\\u9FA5A-Za-z0-9_]+$","中文、英文、数字包括下划线"),

    PASSWORD("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$","密码"),

    ACCOUNT("^[0-9a-zA-Z_]{1,}&","由字母开头、数字、英文字母或者下划线组成"),

    MONEY("^[0-9]+(.[0-9]{1,2})?$","金额");


    private String regex;



    private String desc;


    VerifyRegexEnum(String regex,String desc){
        this.desc=desc;
        this.regex=regex;
    }

    public String getRegex() {
        return regex;
    }

    public String getDesc() {
        return desc;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
