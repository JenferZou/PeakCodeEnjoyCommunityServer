package com.jenfer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jenfer.pojo.EmailCode;
import org.springframework.stereotype.Service;

/**
* @author Jenf
* @description 针对表【email_code(邮箱验证码)】的数据库操作Service
* @createDate 2023-09-24 11:25:25
*/
@Service
public interface EmailCodeService extends IService<EmailCode> {
    void sendEmailCode(String email,Integer type);

    void checkCode(String email,String emailCode);


}
