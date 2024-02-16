package com.jenfer.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jenfer.annotation.GloballInterceptor;
import com.jenfer.annotation.VerifyParam;
import com.jenfer.controller.base.ABaseController;
import com.jenfer.pojo.UserInfo;
import com.jenfer.service.UserInfoService;
import com.jenfer.vo.PaginationResultVo;
import com.jenfer.vo.ResponseVo;
import com.jenfer.vo.UserInfoVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserInfoController extends ABaseController {

    @Resource
    private UserInfoService userInfoService;

    @RequestMapping("/loadUserList")
    public ResponseVo loadUserList(Integer pageNo,Integer pageSize,String nickNameFuzzy,Integer sex,Integer status){
        IPage<UserInfo> userInfoFuzzy = userInfoService.findUserInfoFuzzy(pageNo, pageSize, nickNameFuzzy, sex, status);
        List<UserInfo> userInfoList = userInfoFuzzy.getRecords();
        PaginationResultVo<UserInfo> userInfoPaginationResultVo = new PaginationResultVo<UserInfo>();
        userInfoPaginationResultVo.setPageNo((int)userInfoFuzzy.getCurrent());
        userInfoPaginationResultVo.setPageSize((int) userInfoFuzzy.getSize());
        userInfoPaginationResultVo.setTotalCount((int)userInfoFuzzy.getTotal());
        userInfoPaginationResultVo.setPageTotal((int)userInfoFuzzy.getPages());
        userInfoPaginationResultVo.setList(userInfoList);
        return getSuccessResponseVo(userInfoPaginationResultVo);
    }


    @RequestMapping("/updateUserStatus")
    @GloballInterceptor(checkParams = true)
    public ResponseVo updateUserStatus(@VerifyParam(required = true) String userId,
                                        @VerifyParam(required = true) Integer status){
        userInfoService.updateUserStatus(userId,status);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("/sendMessage")
    @GloballInterceptor(checkParams = true)
    public ResponseVo sendMessage(@VerifyParam(required = true) String userId,
                                  @VerifyParam(required = true) String message,
                                  @VerifyParam(required = true) Integer integral){
        userInfoService.sendMessage(userId,message,integral);
        return getSuccessResponseVo(null);
    }

}
