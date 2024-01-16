package com.jenfer.controller.api;

import com.jenfer.annotation.GloballInterceptor;
import com.jenfer.annotation.VerifyParam;
import com.jenfer.config.WebConfig;
import com.jenfer.controller.ABaseController;
import com.jenfer.enums.ResponseCodeEnum;
import com.jenfer.exception.BusinessException;
import com.jenfer.service.SysSettingService;
import com.jenfer.utils.StringTools;
import com.jenfer.vo.ResponseVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("innerApiController")
@RequestMapping("/innerApi")
public class InnerApiController extends ABaseController {

    @Resource
    private WebConfig webConfig;

    @Resource
    private SysSettingService sysSettingService;


    @RequestMapping("/refresSysSetting")
    @GloballInterceptor(checkParams = true)
    public ResponseVo refresSysSetting(@VerifyParam(required = true) String appKey,
                                       @VerifyParam(required = true)Long timeStamp,
                                       @VerifyParam(required = true)String sign) {

        if(!webConfig.getInnerApiKey().equals(appKey)){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        if(System.currentTimeMillis() - timeStamp > 1000 *10){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        String mySign = StringTools.encodeMd5(appKey+timeStamp+webConfig.getInnerApiSecret());

        if(!mySign.equals(sign)){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        sysSettingService.refresCache();
        return getSuccessResponseVo(null);
    }


}
