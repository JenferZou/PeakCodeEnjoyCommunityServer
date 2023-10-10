package com.jenfer.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jenfer.pojo.UserIntegralRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Bean;

/**
* @author Jenf
* @description 针对表【user_integral_record(用户积分记录表)】的数据库操作Mapper
* @createDate 2023-09-24 11:25:25
* @Entity generator.domain.UserIntegralRecord
*/
@Mapper
public interface UserIntegralRecordMapper extends BaseMapper<UserIntegralRecord> {

}




