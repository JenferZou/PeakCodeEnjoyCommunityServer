package com.jenfer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenfer.mappers.UserMessageMapper;
import com.jenfer.pojo.UserMessage;
import com.jenfer.service.UserMessageService;
import org.springframework.stereotype.Service;

/**
* @author Jenf
* @description 针对表【user_message(用户消息)】的数据库操作Service实现
* @createDate 2023-09-24 11:25:25
*/
@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage>
    implements UserMessageService {

}




