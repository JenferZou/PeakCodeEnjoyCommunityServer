package com.jenfer.aspect;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jenfer.annotation.GloballInterceptor;
import com.jenfer.annotation.VerifyParam;
import com.jenfer.constants.Constants;
import com.jenfer.dto.SessionWebUserDto;
import com.jenfer.dto.SysSettingDto;
import com.jenfer.enums.DateTimePatternEnum;
import com.jenfer.enums.ResponseCodeEnum;
import com.jenfer.enums.UserOperFrequencyTypeEnum;
import com.jenfer.exception.BusinessException;
import com.jenfer.pojo.ForumArticle;
import com.jenfer.pojo.ForumComment;
import com.jenfer.pojo.LikeRecord;
import com.jenfer.service.ForumArticleService;
import com.jenfer.service.ForumCommentService;
import com.jenfer.service.LikeRecordService;
import com.jenfer.utils.DateUtil;
import com.jenfer.utils.StringTools;
import com.jenfer.utils.SysCacheUtils;
import com.jenfer.utils.VerifyUtils;
import com.jenfer.vo.ResponseVo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;

@Aspect
@Component
public class OperactionAspect {
    private static final Logger logger = LoggerFactory.getLogger(OperactionAspect.class);

    private static final String[] TYPE_BASE = {"java.lang.Integer","java.lang.String","java.lang.Long"};


    @Resource
    private ForumArticleService forumArticleService;

    @Resource
    private ForumCommentService forumCommentService;


    @Resource
    private LikeRecordService likeRecordService;

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
                checkLogin();
            }
            /**
             * 校验参数
             */
            if(interceptor.checkParams()){
                validateParams(method,args);
            }
            /**
             * 频次校验
             */
            this.checkOperFrequency(interceptor.frequencyType());

            Object proceed = point.proceed();

            if(proceed instanceof ResponseVo){
                ResponseVo responseVo = (ResponseVo) proceed;
                if(Constants.STATUS_SUCCESS.equals(responseVo.getStatus())){
                    this.addOpcount(interceptor.frequencyType());
                }
            }

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

    void checkOperFrequency(UserOperFrequencyTypeEnum typeEnum){
        if(typeEnum==null||typeEnum==UserOperFrequencyTypeEnum.NO_CHECK){
            return;
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        SessionWebUserDto webUserDto = (SessionWebUserDto) session.getAttribute(Constants.SESSION_KEY);
        String curDate = DateUtil.format(new Date(), DateTimePatternEnum.YYYY_MM_DD.getPattern());
        String sessionKey = Constants.SESSION_KEY_FREQUENCY+curDate+typeEnum.getOperType();
        Integer count = (Integer) session.getAttribute(sessionKey);
        SysSettingDto sysSettingDto = SysCacheUtils.getSysSetting();
        switch (typeEnum){
            case POST_ARTICLE :
                if(count==null){
                    LambdaQueryWrapper<ForumArticle> forumArticleLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    forumArticleLambdaQueryWrapper.eq(ForumArticle::getUser_id,webUserDto.getUserId())
                            .between(ForumArticle::getPost_time,curDate,curDate);
                   count =(int) forumArticleService.count(forumArticleLambdaQueryWrapper);
                }
                if(count>=sysSettingDto.getSysSettingPostDto().getPostDayCountThreshold()){
                    throw new BusinessException(ResponseCodeEnum.CODE_602);
                }
                break;
            case POST_COMMENT:
                if(count==null){
                    LambdaQueryWrapper<ForumComment> forumCommentLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    forumCommentLambdaQueryWrapper.eq(ForumComment::getUser_id,webUserDto.getUserId())
                            .between(ForumComment::getPost_time,curDate,curDate);
                    count =(int) forumCommentService.count(forumCommentLambdaQueryWrapper);
                }
                if(count>=sysSettingDto.getSysSettingCommentDto().getCommentDayCountThreshold()){
                    throw new BusinessException(ResponseCodeEnum.CODE_602);
                }
                break;
            case DO_LIKE:
                if(count==null){
                    LambdaQueryWrapper<LikeRecord> likeRecordLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    likeRecordLambdaQueryWrapper.eq(LikeRecord::getUser_id,webUserDto.getUserId())
                            .between(LikeRecord::getCreate_time,curDate,curDate);
                    count =(int) likeRecordService.count(likeRecordLambdaQueryWrapper);
                }
                if (count>=sysSettingDto.getSysSettingLikeDto().getLikeDayCountThreshold()){
                    throw new BusinessException(ResponseCodeEnum.CODE_602);
                }
                break;
            case IMAGE_UPLAOD:
                if(count==null){
                    count=0;
                }
                if(count>=sysSettingDto.getSysSettingPostDto().getDayImageUploadCount()){
                    throw new BusinessException(ResponseCodeEnum.CODE_602);
                }
                break;

        }

        session.setAttribute(sessionKey,count);
    }


    private void addOpcount(UserOperFrequencyTypeEnum typeEnum){
        if(typeEnum==null||typeEnum==UserOperFrequencyTypeEnum.NO_CHECK){
            return;
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        String curDate = DateUtil.format(new Date(), DateTimePatternEnum.YYYY_MM_DD.getPattern());
        String sessionKey = Constants.SESSION_KEY_FREQUENCY+curDate+typeEnum.getOperType();
        Integer count = (Integer) session.getAttribute(sessionKey);
        session.setAttribute(sessionKey,count+1);
    }

    private void checkLogin(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpSession session = request.getSession();
        Object obj = session.getAttribute(Constants.SESSION_KEY);
        if(obj==null){
            throw new BusinessException(ResponseCodeEnum.CODE_901);
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
