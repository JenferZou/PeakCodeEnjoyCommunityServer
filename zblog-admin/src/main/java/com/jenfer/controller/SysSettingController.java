package com.jenfer.controller;

import com.jenfer.annotation.GloballInterceptor;
import com.jenfer.annotation.VerifyParam;
import com.jenfer.config.AdminConfig;
import com.jenfer.controller.base.ABaseController;
import com.jenfer.dto.*;
import com.jenfer.exception.BusinessException;
import com.jenfer.service.SysSettingService;
import com.jenfer.utils.JsonUtils;
import com.jenfer.utils.OKHttpUtils;
import com.jenfer.utils.StringTools;
import com.jenfer.vo.ResponseVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/setting")
public class SysSettingController extends ABaseController {

    @Resource
    private SysSettingService sysSettingService;


    @Resource
    private AdminConfig adminConfig;


    @RequestMapping("/getSetting")
    public ResponseVo getSetting(){

        return getSuccessResponseVo(sysSettingService.refresCache());
    }



    @RequestMapping("/saveSetting")
    public ResponseVo saveSetting(@VerifyParam SysSettingAuditDto sysSettingAuditDto,
                                  @VerifyParam SysSettingCommentDto sysSettingCommentDto,
                                  @VerifyParam SysSettingPostDto sysSettingPostDto,
                                  @VerifyParam SysSettingLikeDto sysSettingLikeDto,
                                  @VerifyParam SysSettingEmailDto sysSettingEmailDto
                                  ){
        SysSettingDto sysSettingDto = new SysSettingDto();
        sysSettingDto.setSysSettingAuditDto(sysSettingAuditDto);
        sysSettingDto.setSysSettingCommentDto(sysSettingCommentDto);
        sysSettingDto.setSysSettingPostDto(sysSettingPostDto);
        sysSettingDto.setSysSettingLikeDto(sysSettingLikeDto);
        sysSettingDto.setSysSettingEmailDto(sysSettingEmailDto);
        sysSettingService.saveSetting(sysSettingDto);
        sentWebRequest();
        return getSuccessResponseVo(null);
    }


    private void sentWebRequest(){
        String appKey = adminConfig.getInnerApiKey();
        String innerApiSecret = adminConfig.getInnerApiSecret();
        long timeStamp = System.currentTimeMillis();
        String sign = StringTools.encodeMd5(appKey + timeStamp + innerApiSecret);
        String url = adminConfig.getWebApiUrl() + "?appKey" + appKey + "&timeStamp" + timeStamp + "&sign" + sign;
        String requestJson = OKHttpUtils.getRequest(url);
        ResponseVo responseVo = JsonUtils.convertJson2Obj(requestJson, ResponseVo.class);
        if(!STATIC_SUCCESS.equals(responseVo.getStatus())){
            throw new BusinessException("刷新访客端缓存失败");
        }
    }



}
