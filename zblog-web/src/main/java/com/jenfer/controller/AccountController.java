package com.jenfer.controller;

import com.jenfer.annotation.GloballInterceptor;
import com.jenfer.annotation.VerifyParam;
import com.jenfer.constants.Constants;
import com.jenfer.dto.SessionWebUserDto;
import com.jenfer.dto.SysSettingCommentDto;
import com.jenfer.dto.SysSettingDto;
import com.jenfer.enums.ResponseCodeEnum;
import com.jenfer.enums.VerifyRegexEnum;
import com.jenfer.exception.BusinessException;
import com.jenfer.service.EmailCodeService;
import com.jenfer.service.UserInfoService;
import com.jenfer.utils.CreateImageCode;
import com.jenfer.utils.StringTools;
import com.jenfer.utils.SysCacheUtils;
import com.jenfer.vo.ResponseVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AccountController extends ABaseController{

    @Autowired
    private EmailCodeService emailCodeService;


    @Autowired
    private UserInfoService userInfoService;



    @RequestMapping("/checkCode")
    public void checkCode(HttpServletResponse response, HttpSession session,Integer type) throws IOException {
        CreateImageCode vCode = new CreateImageCode(130, 30, 5, 10);
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expires",0);
        response.setContentType("image/jpeg");
        String code = vCode.getCode();
        if(type==null||type==0){
            session.setAttribute(Constants.CHECK_CODE_KEY,code);
        }else {
            session.setAttribute(Constants.CHECK_CODE_KEY_EMAIL,code);
        }
        vCode.write(response.getOutputStream());
    }


    @RequestMapping("/sendEmailCode")
    @GloballInterceptor(checkParams = true)
    public ResponseVo sendEmailCode(HttpSession session,
                                    @VerifyParam(required = true) String email,
                                    @VerifyParam(required = true) String checkCode,
                                    @VerifyParam(required = true) Integer type){
        try {
//            if(StringTools.isEmpty(email)||StringTools.isEmpty(checkCode)||type==null){
//                throw new BusinessException(ResponseCodeEnum.CODE_600);
//            }
            if(!checkCode.equalsIgnoreCase((String)session.getAttribute(Constants.CHECK_CODE_KEY))){
                throw new BusinessException("图片验证码错误");
            }
            emailCodeService.sendEmailCode(email,type);
            return getSuccessResponseVo("验证成功");
        }finally {
            session.removeAttribute(Constants.CHECK_CODE_KEY);
        }


    }

    @RequestMapping("/register")
    @GloballInterceptor(checkParams = true)
    public ResponseVo register(HttpSession session, @VerifyParam(required = true,regex = VerifyRegexEnum.EMAIL,max = 150) String email,
                               @VerifyParam(required = true) String emailCode,
                               @VerifyParam(required = true,max = 20) String nickName,
                               @VerifyParam(required = true,min = 8,max = 18,regex = VerifyRegexEnum.PASSWORD) String password,
                               @VerifyParam(required = true) String checkCode){
        try {
//            if(StringTools.isEmpty(email)||StringTools.isEmpty(emailCode)||StringTools.isEmpty(nickName)||StringTools.isEmpty(password)
//            ||StringTools.isEmpty(checkCode)){
//                throw new BusinessException(ResponseCodeEnum.CODE_600);
//            }
            if(!checkCode.equalsIgnoreCase((String)session.getAttribute(Constants.CHECK_CODE_KEY))){
                throw new BusinessException("图片验证码错误");
            }
            userInfoService.register(email,emailCode,nickName,password);
            return getSuccessResponseVo(null);
        } finally {
            session.removeAttribute(Constants.CHECK_CODE_KEY);
        }

    }




    @RequestMapping("/login")
    @GloballInterceptor(checkParams = true)
    public ResponseVo login(HttpSession session, HttpServletRequest request,
                            @VerifyParam(required = true,regex = VerifyRegexEnum.EMAIL,max = 150) String email,
                            @VerifyParam(required = true) String password,
                            @VerifyParam(required = true) String checkCode){
        try {
//
            if(!checkCode.equalsIgnoreCase((String)session.getAttribute(Constants.CHECK_CODE_KEY))){
                throw new BusinessException("图片验证码错误");
            }
            SessionWebUserDto sessionWebUserDto = userInfoService.login(email,password,getIpAddress(request));
            session.setAttribute(Constants.SESSION_KEY,sessionWebUserDto);

            return getSuccessResponseVo(sessionWebUserDto);
        } finally {
            session.removeAttribute(Constants.CHECK_CODE_KEY);
        }
    }



    @RequestMapping("/getUserInfo")
    @GloballInterceptor()
    public ResponseVo getUserInfo(HttpSession session){
        return getSuccessResponseVo(getUserInfoFromSession(session));
    }


    @RequestMapping("/logout")
    @GloballInterceptor()
    public ResponseVo logout(HttpSession session){
        session.invalidate();
        return getSuccessResponseVo(null);
    }


    @RequestMapping("/getSysSetting")
    @GloballInterceptor()
    public ResponseVo getSysSetting(){
        SysSettingDto sysSetting = SysCacheUtils.getSysSetting();
        SysSettingCommentDto commentDto = sysSetting.getSysSettingCommenDto();
        Map<String,Object> result = new HashMap<>();
        result.put("commentOpen",commentDto.getCommentOpen());
        return getSuccessResponseVo(result);
    }


    @RequestMapping("/resetPwd")
    @GloballInterceptor(checkParams = true)
    public ResponseVo resetPwd(HttpSession session,@VerifyParam(required = true) String email,
                               @VerifyParam(required = true,min = 8,max = 18,regex = VerifyRegexEnum.PASSWORD) String password,
                               @VerifyParam(required = true) String checkCode,
                               @VerifyParam(required = true) String emailCode) {
        try {
            if (!checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY_EMAIL))) {
                throw new BusinessException("图片验证码错误");
            }
            userInfoService.resetPwd(email, password, emailCode);
            return getSuccessResponseVo(null);
        } finally {
            session.removeAttribute(Constants.CHECK_CODE_KEY);
        }

    }

}
