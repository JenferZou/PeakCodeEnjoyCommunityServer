package com.jenfer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenfer.config.WebConfig;
import com.jenfer.constants.Constants;
import com.jenfer.dto.SessionWebUserDto;
import com.jenfer.enums.*;
import com.jenfer.exception.BusinessException;
import com.jenfer.mappers.UserInfoMapper;
import com.jenfer.mappers.UserIntegralRecordMapper;
import com.jenfer.mappers.UserMessageMapper;
import com.jenfer.pojo.UserInfo;
import com.jenfer.pojo.UserIntegralRecord;
import com.jenfer.pojo.UserMessage;
import com.jenfer.service.EmailCodeService;
import com.jenfer.service.UserInfoService;
import com.jenfer.service.UserIntegralRecordService;
import com.jenfer.utils.JsonUtils;
import com.jenfer.utils.OKHttpUtils;
import com.jenfer.utils.StringTools;
import com.jenfer.utils.SysCacheUtils;
import org.apache.catalina.User;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
* @author Jenf
* @description 针对表【user_info(用户信息)】的数据库操作Service实现
* @createDate 2023-09-24 11:25:25
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService {


    private static final Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    @Autowired
    private EmailCodeService emailCodeService;

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Autowired
    private UserIntegralRecordMapper userIntegralRecordMapper;


    @Autowired
    private WebConfig webConfig;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(String email, String emailCode, String nickName, String password) {
        QueryWrapper<UserInfo> emailQueryWrapper = new QueryWrapper<>();
        emailQueryWrapper.eq("email",email);
        UserInfo userInfo = baseMapper.selectOne(emailQueryWrapper);
        if(userInfo!=null){
            throw new BusinessException("邮箱已存在");
        }
        QueryWrapper<UserInfo> NickNameQueryWrapper = new QueryWrapper<>();
        emailQueryWrapper.eq("nick_name",nickName);
         userInfo = baseMapper.selectOne(NickNameQueryWrapper);
        if(userInfo!=null){
            throw new BusinessException("昵称已存在");
        }
        //检测邮箱和验证码
        emailCodeService.checkCode(email,emailCode);


        //将注册的用户插入数据库
        String userID = StringTools.getRandomNumber(Constants.LENGTH_10);
        UserInfo insertInfo = new UserInfo();
        insertInfo.setUser_id(userID);
        insertInfo.setEmail(email);
        insertInfo.setJoin_time(new Date());
        insertInfo.setNick_name(nickName);
        insertInfo.setStatus(UserStatusEnum.ENABLE.getStatus());
        insertInfo.setTotal_integral(Constants.ZERO);
        insertInfo.setCurrent_integral(Constants.ZERO);
        baseMapper.insert(insertInfo);

        //更新用户积分
        updateUserIntegral(userID,UserIntegralOperTypeEnum.REGISTER,UserIntegralChangeTypeEnum.ADD.getChangeType(), Constants.INTEGRAL_5);


        //记录消息
        UserMessage userMessage = new UserMessage();
        userMessage.setReceived_user_id(userID);
        userMessage.setMessage_type(MessageTypeEnum.SYS.getType());
        userMessage.setCreate_time(new Date());
        userMessage.setStatus(MessageStatusEnum.NO_READ.getStatus());
        userMessage.setMessage_content(SysCacheUtils.getSysSetting().getSysSettingRegisterDto().getRegisterWelcomInfo());
        userMessageMapper.insert(userMessage);



    }

    /**
     * 更新用户积分
     * @param userId
     * @param operTypeEnum
     * @param changeType
     * @param integral
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserIntegral(String userId, UserIntegralOperTypeEnum operTypeEnum, Integer changeType, Integer integral) {
        integral = changeType*integral;
        if(integral==0){
            return;
        }
        UserInfo userInfo = baseMapper.selectById(userId);
        if(UserIntegralChangeTypeEnum.REDUCE.getChangeType().equals(changeType)&&userInfo.getCurrent_integral()+integral<0){
            integral = changeType * userInfo.getCurrent_integral();
        }

        UserIntegralRecord userIntegralRecord = new UserIntegralRecord();
        userIntegralRecord.setUser_id(userId);
        userIntegralRecord.setCreate_time(new Date());
        userIntegralRecord.setOper_type(operTypeEnum.getOperType());
        userIntegralRecord.setIntegral(integral);
        userIntegralRecordMapper.insert(userIntegralRecord);


        int count = baseMapper.updateIntegral(integral, userId);
        if(count==0){
            throw new BusinessException("更新用户积分失败");
        }

    }

    @Override
    public SessionWebUserDto login(String email,String password, String ip) {
        UserInfo userInfo = baseMapper.selectOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getEmail, email));
        if(null==userInfo || !userInfo.getPassword().equals(password)){
            throw new BusinessException("账号或密码错误");
        }
        if(!UserStatusEnum.ENABLE.getStatus().equals(userInfo.getStatus())){
            throw new BusinessException("账号已禁用");
        }
        String ipAddress = getIpAddress(ip);
        UserInfo updateUserInfo = new UserInfo();
        updateUserInfo.setLast_login_ip(ip);
        updateUserInfo.setLast_login_time(new Date());
        updateUserInfo.setLast_login_ip_address(ipAddress);
        baseMapper.update(updateUserInfo,new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getUser_id,userInfo.getUser_id()));


        SessionWebUserDto sessionWebUserDto = new SessionWebUserDto();
        sessionWebUserDto.setUserId(userInfo.getUser_id());
        sessionWebUserDto.setNickName(userInfo.getNick_name());
        sessionWebUserDto.setProvince(ipAddress);
        if(!StringTools.isEmpty(webConfig.getAdminEmails())&& ArrayUtils.contains(webConfig.getAdminEmails().split(","),userInfo.getEmail())){
            sessionWebUserDto.setAdmin(true);
        }else {
            sessionWebUserDto.setAdmin(false);
        }

        return sessionWebUserDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPwd(String email, String password, String emailCode) {
        UserInfo userInfo = baseMapper.selectOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getEmail, email));
        if(null==userInfo){
            throw new BusinessException("邮箱不存在");
        }
        emailCodeService.checkCode(email,emailCode);
        UserInfo updateUserInfo = new UserInfo();
        updateUserInfo.setPassword(StringTools.encodeMd5(password));
        baseMapper.update(updateUserInfo,new LambdaUpdateWrapper<UserInfo>().eq(UserInfo::getEmail,email));

    }


    public String getIpAddress(String ip){
        Map<String, String> addressInfo = new HashMap<>();
        try {
            String url = "http://whois.pconline.com.cn/ipJson.jsp?json=true&ip="+ip;
            String responseJson = OKHttpUtils.getRequest(url);
            if(null==responseJson){
                return Constants.NO_ADDRESS;
            }
            addressInfo = JsonUtils.convertJson2Obj(responseJson,Map.class);
        }catch (Exception e){
            logger.error("获取Ip地址失败",e);
        }
        return Constants.NO_ADDRESS;
    }

}




