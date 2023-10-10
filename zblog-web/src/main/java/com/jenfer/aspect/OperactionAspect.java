package com.jenfer.aspect;

import com.jenfer.annotation.GloballInterceptor;
import com.jenfer.annotation.VerifyParam;
import com.jenfer.enums.ResponseCodeEnum;
import com.jenfer.exception.BusinessException;
import com.jenfer.utils.StringTools;
import com.jenfer.utils.VerifyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.reflection.ArrayUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Component
public class OperactionAspect {
    private static final Logger logger = LoggerFactory.getLogger(OperactionAspect.class);

    private static final String[] TYPE_BASE = {"java.lang.Integer","java.lang.String","java.lang.Long"};


    @Pointcut("@annotation(com.jenfer.annotation.GloballInterceptor)")
    private void requestInterceptor(){

    }

    @Around("requestInterceptor()")
    public Object interceptorDo(ProceedingJoinPoint point){
        try {
            Object target = point.getTarget();
            Object[] args = point.getArgs();
            String methodName = point.getSignature().getName();
            Class<?>[] parameters = ((MethodSignature)point.getSignature()).getMethod().getParameterTypes();
            Method method = target.getClass().getMethod(methodName,parameters);
            GloballInterceptor interceptor = method.getAnnotation(GloballInterceptor.class);
            if(null==interceptor){
                return null;
            }
            /**
             * 校验登录
             */
            if(interceptor.checkLogin()){

            }
            /**
             * 校验参数
             */
            if(interceptor.checkParams()){
                validateParams(method,args);
            }
            Object proceed = point.proceed();
            return proceed;

        }catch (BusinessException e){
            logger.error("全局拦截器异常",e);
            throw e;
        }catch (Exception e){
            logger.error("全局拦截器异常",e);
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        }catch (Throwable e){
            logger.error("全局拦截器异常",e);
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        }
    }


    private void validateParams(Method m,Object[] argument){
        Parameter[] parameters = m.getParameters();
        for(int i=0;i<parameters.length;i++){
            Parameter parameter = parameters[i];
            Object value = argument[i];
            VerifyParam verifyParam = parameter.getAnnotation(VerifyParam.class);
            if(verifyParam==null){
                continue;
            }
            if(ArrayUtils.contains(TYPE_BASE,parameter.getParameterizedType().getTypeName())){
                checkValue(value,verifyParam);
            }else {

            }
        }

    }


    public void checkObjValue(Parameter parameter,Object value){

    }


    private void checkValue(Object value,VerifyParam verifyParam){
        Boolean isEmpty = value==null|| StringTools.isEmpty(value.toString());
        Integer length = value == null?0:value.toString().length();

        /**
         * 校验空
         */
        if(isEmpty&&verifyParam.required()){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        /**
         * 校验长度
         */
        if(!isEmpty&&(verifyParam.max()!=-1&& verifyParam.max()<length||verifyParam.min()!=-1&&verifyParam.min()>length)){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        /**
         * 校验正则
         */
        if(!isEmpty&&!StringTools.isEmpty(verifyParam.regex().getRegex())&& !VerifyUtils.verify(verifyParam.regex(),String.valueOf(value))){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }




    }


}
