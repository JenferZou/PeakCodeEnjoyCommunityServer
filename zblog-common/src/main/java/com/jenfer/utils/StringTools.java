package com.jenfer.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

public class StringTools {

    public static Boolean isEmpty(String str){
        if(null==str||"".equals(str.trim())||"null".equals(str)){
            return true;
        }else {
            return false;
        }
    }

    public static final String getRandomString(Integer count){
        return RandomStringUtils.random(count,true,true);
    }


    public static final String getRandomNumber(Integer count){
        return RandomStringUtils.random(count,false,true);
    }

    public static String encodeMd5(String sourceStr){
        return StringTools.isEmpty(sourceStr)?null: DigestUtils.md5Hex(sourceStr);
    }


    public static String getFileSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public static String getFileName(String fileName){
        return fileName.substring(0,fileName.lastIndexOf("."));
    }


    public static String transPageHtml(String content){
        if(StringTools.isEmpty(content)){
            return content;
        }
        content.replace("<", "&lt;");
        content.replace(" ", "&nbsp;");
        content.replace("\n", "<br/>");
        return content;
    }
}
