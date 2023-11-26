package com.jenfer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenfer.config.WebConfig;
import com.jenfer.constants.Constants;
import com.jenfer.enums.ResponseCodeEnum;
import com.jenfer.exception.BusinessException;
import com.jenfer.mappers.EmailCodeMapper;
import com.jenfer.mappers.UserInfoMapper;
import com.jenfer.pojo.EmailCode;
import com.jenfer.pojo.UserInfo;
import com.jenfer.service.EmailCodeService;
import com.jenfer.utils.StringTools;
import jakarta.annotation.Resource;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
* @author Jenf
* @description 针对表【email_code(邮箱验证码)】的数据库操作Service实现
* @createDate 2023-09-24 11:25:25
*/
@Service
public class EmailCodeServiceImpl extends ServiceImpl<EmailCodeMapper, EmailCode>
    implements EmailCodeService {

    private static final Logger logger = LoggerFactory.getLogger(EmailCodeServiceImpl.class);
    @Resource
    private JavaMailSender javaMailSender;

    @Autowired
    private WebConfig webConfig;

    @Autowired
    private UserInfoMapper userInfoMapper;



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendEmailCode(String email, Integer type) {
        if(type==Constants.ZERO){
            QueryWrapper<UserInfo> emailQueryWrapper = new QueryWrapper<>();
            emailQueryWrapper.eq("email",email);
            UserInfo userInfo = userInfoMapper.selectOne(emailQueryWrapper);
            if(userInfo!=null){
                throw new BusinessException(ResponseCodeEnum.CODE_601);
            }
            String randomCode = StringTools.getRandomString(Constants.LENGTH_5);
            sendEmailDetail(email,randomCode);

            //当后一次的验证码来了以后将之前的验证码全部设置为失效
            LambdaUpdateWrapper<EmailCode> emailCodeUpdateWrapper = new LambdaUpdateWrapper<>();
            emailCodeUpdateWrapper.set(EmailCode::getStatus,1).eq(EmailCode::getEmail,email).eq(EmailCode::getStatus,0);
            int update = baseMapper.update(null,emailCodeUpdateWrapper);


            //将数据插入数据库
            EmailCode emailCode = new EmailCode(email, randomCode, new Date(), Constants.ZERO);
            baseMapper.insert(emailCode);


        }
    }

    @Override
    public void checkCode(String email, String emailCode) {
        QueryWrapper<EmailCode> emailQueryWrapper = new QueryWrapper<>();
        emailQueryWrapper.eq("email",email).eq("code",emailCode).eq("status",0);
        EmailCode dbEmailCode = baseMapper.selectOne(emailQueryWrapper);
        if(null==dbEmailCode){
            throw new BusinessException("邮箱验证码不正确");
        }
        if(dbEmailCode.getStatus()!=Constants.ZERO||System.currentTimeMillis()-dbEmailCode.getCreate_time().getTime()>1000*60*15) {
            throw new BusinessException("验证码已失效");
        }

        }


    private void sendEmailDetail(String email,String code){
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            //邮件发送人
            helper.setFrom(webConfig.getSendUserName());
            //邮件收件人
            helper.setTo(email);
            helper.setSubject("邮箱验证码");
            helper.setText("邮箱验证码为："+code);
            helper.setSentDate(new Date());
            javaMailSender.send(message);
        }catch (Exception e){
            logger.error("邮件发送失败",e);
            new BusinessException(ResponseCodeEnum.CODE_601);
        }

    }




}




