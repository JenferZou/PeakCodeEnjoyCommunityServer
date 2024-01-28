package com.jenfer.mappers;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jenfer.pojo.UserMessage;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author Jenf
* @description 针对表【user_message(用户消息)】的数据库操作Mapper
* @createDate 2023-09-24 11:25:25
* @Entity generator.domain.UserMessage
*/
@Mapper
public interface UserMessageMapper extends BaseMapper<UserMessage> {

    @MapKey("messageType")
    List<Map> selectUserMessageCount(@Param("userId") String userId);

}




