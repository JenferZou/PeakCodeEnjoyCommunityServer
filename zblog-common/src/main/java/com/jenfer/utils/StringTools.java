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

}
