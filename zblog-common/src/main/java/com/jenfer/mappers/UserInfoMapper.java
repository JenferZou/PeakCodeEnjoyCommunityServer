package com.jenfer.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jenfer.pojo.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author Jenf
* @description 针对表【user_info(用户信息)】的数据库操作Mapper
* @createDate 2023-09-24 11:25:25
* @Entity generator.domain.UserInfo
*/
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    int updateIntegral(@Param("integral") Integer integral,@Param("userId")String userId);

}




