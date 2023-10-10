package com.jenfer.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtil {

    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<>();
    private static Object lockOj = new Object();

    private static SimpleDateFormat getSdf(final String pattern){
        ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);
        if (tl == null){
            synchronized (lockOj){
                tl = sdfMap.get(pattern);
                if (tl == null){
                    tl = ThreadLocal.withInitial(() -> new SimpleDateFormat(pattern));
                    sdfMap.put(pattern, tl);
                }
            }
        }
        return tl.get();
    }

    public static String format(Date date, String pattern){
        return getSdf(pattern).format(date);
    }

    public static Date parse(String dateStr,String pattern){
        try {
            return getSdf(pattern).parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
