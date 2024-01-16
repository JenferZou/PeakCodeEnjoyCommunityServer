package com.jenfer.annotation;

import com.jenfer.enums.UserOperFrequencyTypeEnum;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GloballInterceptor {

    /**
     * 判断该操作是否需要登录
     */

    boolean checkLogin() default false;

    /**
     * 是否需要校验参数
     */
    boolean checkParams() default false;


    /**
     * 频次校验
     */
    UserOperFrequencyTypeEnum frequencyType() default UserOperFrequencyTypeEnum.NO_CHECK;

}
