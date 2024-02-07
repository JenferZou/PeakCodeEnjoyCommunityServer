package com.jenfer.interceptor;

import com.jenfer.config.AdminConfig;
import com.jenfer.constants.Constants;
import com.jenfer.dto.SessionAdminUserDto;
import com.jenfer.dto.SessionWebUserDto;
import com.jenfer.enums.ResponseCodeEnum;
import com.jenfer.exception.BusinessException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
@Component("appInterceptor")
public class AppInterceptor implements HandlerInterceptor {

    @Resource
    private AdminConfig adminConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (null==handler){
            return false;
        }
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        if(request.getRequestURL().indexOf("checkCode")!=-1||request.getRequestURL().indexOf("login")!=-1){
            return true;
        }
        checkLogin();
        return true;

    }

    private void checkLogin(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        SessionAdminUserDto webUserDto = (SessionAdminUserDto) session.getAttribute(Constants.SESSION_KEY);
        if(webUserDto==null&&adminConfig.getIsDev()){
            webUserDto =  new SessionAdminUserDto();
            webUserDto.setAccount("管理员");
            session.setAttribute(Constants.SESSION_KEY, webUserDto);
        }
        if(null==webUserDto){
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
