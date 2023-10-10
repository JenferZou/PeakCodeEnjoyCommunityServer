package com.jenfer.utils;

import com.jenfer.enums.ResponseCodeEnum;
import com.jenfer.exception.BusinessException;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OKHttpUtils {

    private static final int TIME_OUT_SECONDES = 5;

    private static Logger logger = LoggerFactory.getLogger(OKHttpUtils.class);

    private static OkHttpClient.Builder getClientBuilder(){
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().followRedirects(false).retryOnConnectionFailure(false);
        clientBuilder.connectTimeout(TIME_OUT_SECONDES, TimeUnit.SECONDS).readTimeout(TIME_OUT_SECONDES,TimeUnit.SECONDS);
        return clientBuilder;
    }

    private static Request.Builder getRequestBuilder(Map<String,String>header){
        Request.Builder requestBuilder = new Request.Builder();
        if(null!=header){
            for (Map.Entry<String,String> map :header.entrySet()){
                String key = map.getKey();
                String value;
                if(map.getValue()==null){
                    value="";
                }else {
                    value = map.getValue();
                }
                requestBuilder.addHeader(key,value);
            }
        }
        return requestBuilder;
    }


    private static FormBody.Builder getBuilder(Map<String,String> params){
        FormBody.Builder builder = new FormBody.Builder();
        if(params ==null){
            return builder;
        }
        for(Map.Entry<String,String> map :params.entrySet()){
            String key = map.getKey();
            String value;
            if(map.getValue()==null){
                value = "";
            }else {
                value = map.getValue();
            }
            builder.add(key,value);
        }
        return builder;
    }

    public static String getRequest(String url) throws BusinessException{
        ResponseBody responseBody = null;
        try {
            OkHttpClient.Builder clientBuilder = getClientBuilder();
            Request.Builder requestBuilder = getRequestBuilder(null);
            OkHttpClient okHttpClient = clientBuilder.build();
            Request request = requestBuilder.url(url).build();
            Response response = okHttpClient.newCall(request).execute();
            responseBody = response.body();
            return responseBody.string();
        }catch (SocketTimeoutException | ConnectException e){
            logger.error("OKhttp Post 请求超时，url:{}",url,e);
            throw new BusinessException(ResponseCodeEnum.CODE_900);
        }catch (Exception e){
            logger.error("Okhttp GET 请求异常",e);
            return null;
        }finally {
            if(responseBody!=null){
                responseBody.close();
            }
        }

    }

//    public static String postRequest(String url,Map<String,String>header,Map<String,String>params)throws BusinessException{
//        ResponseBody responseBody = null;
//        try {
//            OkHttpClient.Builder clientBuilder = getClientBuilder();
//            Request.Builder requestBuilder = getRequestBuilder(null);
//            OkHttpClient okHttpClient = clientBuilder.build();
//            Request request = requestBuilder.url(url).build();
//            Response response = okHttpClient.newCall(request).execute();
//            responseBody = response.body();
//            return responseBody.string();
//        }catch (SocketTimeoutException | ConnectException e){
//            logger.error("OKhttp Post 请求超时，url:{}",url,e);
//            throw new BusinessException(ResponseCodeEnum.CODE_900);
//        }catch (Exception e){
//            logger.error("Okhttp GET 请求异常",e);
//            return null;
//        }finally {
//            if(responseBody!=null){
//                responseBody.close();
//            }
//        }
//    }




}
