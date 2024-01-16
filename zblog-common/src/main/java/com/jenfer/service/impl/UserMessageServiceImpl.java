package com.jenfer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenfer.dto.UserMessageCountDto;
import com.jenfer.enums.MessageTypeEnum;
import com.jenfer.mappers.UserMessageMapper;
import com.jenfer.pojo.UserMessage;
import com.jenfer.service.UserMessageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
* @author Jenf
* @description 针对表【user_message(用户消息)】的数据库操作Service实现
* @createDate 2023-09-24 11:25:25
*/
@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage>
    implements UserMessageService {

    @Override
    public UserMessageCountDto getUserMessageCount(String userId) {
        List<Map> mapList = this.baseMapper.selectUserMessageCount(userId);
        UserMessageCountDto messageCountDto = new UserMessageCountDto();
        Long totalCount = 0L;
        for(Map item : mapList){
            Integer type = (Integer) item.get("messageType");
            Long count = (Long) item.get("count");
            totalCount = totalCount+count;
            MessageTypeEnum messageType = MessageTypeEnum.getByType(type);
            switch (messageType){
                case SYS :
                    messageCountDto.setSys(count);
                    break;
                case COMMENT :
                     messageCountDto.setReply(count);
                     break;
                case ARTICLE_LIKE:
                    messageCountDto.setLikePost(count);
                    break;
                case COMMENT_LIKE :
                    messageCountDto.setLikeComment(count);
                      break;
                case DOWNLOAD_ATTACHMENT :
                      messageCountDto.setDownloadAttachment(count);
                      break;
            }
        }
        messageCountDto.setTotal(totalCount);
        return messageCountDto;
    }


}




