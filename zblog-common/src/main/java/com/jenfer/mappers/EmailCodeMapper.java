package com.jenfer.mappers;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jenfer.pojo.EmailCode;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Jenf
* @description 针对表【email_code(邮箱验证码)】的数据库操作Mapper
* @createDate 2023-09-24 11:25:25
* @Entity generator.domain.EmailCode
*/
@Mapper
public interface EmailCodeMapper extends BaseMapper<EmailCode> {


}




