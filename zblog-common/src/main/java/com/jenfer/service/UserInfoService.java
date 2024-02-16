package com.jenfer.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jenfer.dto.SessionWebUserDto;
import com.jenfer.enums.UserIntegralOperTypeEnum;
import com.jenfer.pojo.UserInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
* @author Jenf
* @description 针对表【user_info(用户信息)】的数据库操作Service
* @createDate 2023-09-24 11:25:25
*/
@Service
public interface UserInfoService extends IService<UserInfo> {
    void register(String email,String emailCode,String nickName,String password);

    void updateUserIntegral(String userId, UserIntegralOperTypeEnum operTypeEnum,Integer changeType,Integer integral);

    SessionWebUserDto login(String email,String password,String ip);


    void resetPwd(String email,String password,String emailCode);

    void updateUserInfo(UserInfo userInfo, MultipartFile avatar);


    IPage<UserInfo> findUserInfoFuzzy(Integer pageNo, Integer pageSize, String nickNameFuzzy, Integer sex, Integer status);

    void updateUserStatus(String userId, Integer status);

    void sendMessage(String userId, String message, Integer integral);
}
