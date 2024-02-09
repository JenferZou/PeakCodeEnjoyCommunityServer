package com.jenfer.controller;

import com.jenfer.annotation.GloballInterceptor;
import com.jenfer.annotation.VerifyParam;
import com.jenfer.config.AdminConfig;
import com.jenfer.constants.Constants;
import com.jenfer.controller.base.ABaseController;
import com.jenfer.dto.SessionAdminUserDto;
import com.jenfer.dto.SessionWebUserDto;
import com.jenfer.dto.SysSettingCommentDto;
import com.jenfer.dto.SysSettingDto;
import com.jenfer.enums.VerifyRegexEnum;
import com.jenfer.exception.BusinessException;
import com.jenfer.service.EmailCodeService;
import com.jenfer.service.UserInfoService;
import com.jenfer.utils.CreateImageCode;
import com.jenfer.utils.StringTools;
import com.jenfer.utils.SysCacheUtils;
import com.jenfer.vo.ResponseVo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AccountController extends ABaseController {

    @Autowired
    private EmailCodeService emailCodeService;


    @Autowired
    private UserInfoService userInfoService;


    @Resource
    private AdminConfig adminConfig;



    @RequestMapping("/checkCode")
    public void checkCode(HttpServletResponse response, HttpSession session) throws IOException {
        CreateImageCode vCode = new CreateImageCode(130, 30, 5, 10);
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expires",0);
        response.setContentType("image/jpeg");
        String code = vCode.getCode();
        session.setAttribute(Constants.CHECK_CODE_KEY,code);
        vCode.write(response.getOutputStream());
    }



    @RequestMapping("/login")
    public ResponseVo login(HttpSession session,
                            @VerifyParam(required = true) String account,
                            @VerifyParam(required = true) String password,
                            @VerifyParam(required = true) String checkCode){
        try {
            if(!checkCode.equalsIgnoreCase((String)session.getAttribute(Constants.CHECK_CODE_KEY))){
                throw new BusinessException("图片验证码错误");
            }
            if(!adminConfig.getAdminAccount().equals(account)||!StringTools.encodeMd5(adminConfig.getAdminPassword()).equals(password)){
                throw new BusinessException("账号或密码错误");
            }
            SessionAdminUserDto sessionAdminUserDto = new SessionAdminUserDto();
            session.setAttribute(Constants.SESSION_KEY,sessionAdminUserDto);

            return getSuccessResponseVo(sessionAdminUserDto);
        } finally {
            session.removeAttribute(Constants.CHECK_CODE_KEY);
        }
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



}
