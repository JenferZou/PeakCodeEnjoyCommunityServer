package com.jenfer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jenfer.dto.UserMessageCountDto;
import com.jenfer.pojo.UserMessage;
import org.springframework.stereotype.Service;

/**
* @author Jenf
* @description 针对表【user_message(用户消息)】的数据库操作Service
* @createDate 2023-09-24 11:25:25
*/
@Service
public interface UserMessageService extends IService<UserMessage> {

    UserMessageCountDto getUserMessageCount(String userId);


}
