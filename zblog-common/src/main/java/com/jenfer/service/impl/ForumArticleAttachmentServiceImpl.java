package com.jenfer.service.impl;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenfer.constants.Constants;
import com.jenfer.dto.SessionWebUserDto;
import com.jenfer.enums.MessageStatusEnum;
import com.jenfer.enums.MessageTypeEnum;
import com.jenfer.enums.UserIntegralChangeTypeEnum;
import com.jenfer.enums.UserIntegralOperTypeEnum;
import com.jenfer.exception.BusinessException;
import com.jenfer.mappers.ForumArticleAttachmentMapper;
import com.jenfer.pojo.*;
import com.jenfer.service.ForumArticleAttachmentDownloadService;
import com.jenfer.service.ForumArticleAttachmentService;
import com.jenfer.service.ForumArticleService;
import com.jenfer.service.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
* @author Jenf
* @description 针对表【forum_article_attachment(文件信息)】的数据库操作Service实现
* @createDate 2023-09-24 11:25:25
*/
@Service
public class ForumArticleAttachmentServiceImpl extends ServiceImpl
        <ForumArticleAttachmentMapper, ForumArticleAttachment>
    implements ForumArticleAttachmentService {

    @Autowired
    private ForumArticleAttachmentDownloadService forumArticleAttachmentDownloadService;

    @Autowired
    private UserInfoServiceImpl userInfoService;

    @Autowired
    private ForumArticleService forumArticleService;

    @Autowired
    private UserMessageService userMessageService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ForumArticleAttachment downloadAttachment(String fileId, SessionWebUserDto userInfoFromSession) {
        ForumArticleAttachment forumArticleAttachment = getOne(new LambdaUpdateWrapper<ForumArticleAttachment>().eq(ForumArticleAttachment::getFile_id, fileId));
        if(null==forumArticleAttachment){
            throw new BusinessException("附件不存在");
        }
        ForumArticleAttachmentDownload download =null;
        if(forumArticleAttachment.getIntegral()>0&&!userInfoFromSession.getUserId().equals(forumArticleAttachment.getUser_id()))
        {
            LambdaQueryWrapper<ForumArticleAttachmentDownload> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ForumArticleAttachmentDownload::getFile_id, fileId)
                    .eq(ForumArticleAttachmentDownload::getUser_id, userInfoFromSession.getUserId());
            download = forumArticleAttachmentDownloadService.getOne(wrapper);
            if(download==null){
                UserInfo userInfo = userInfoService.
                        getOne(new LambdaUpdateWrapper<UserInfo>().eq(UserInfo::getUser_id, userInfoFromSession.getUserId()));
                if(userInfo.getCurrent_integral()<forumArticleAttachment.getIntegral()){
                    throw new BusinessException("积分不足");
                }
            }
        }
        ForumArticleAttachmentDownload attachmentDownload = new ForumArticleAttachmentDownload();
        attachmentDownload.setArticle_id(forumArticleAttachment.getArticle_id());
        attachmentDownload.setFile_id(fileId);
        attachmentDownload.setDownload_count(forumArticleAttachment.getDownload_count()+1);
        attachmentDownload.setUser_id(userInfoFromSession.getUserId());
        attachmentDownload.setDownload_count(Constants.ONE);
        //更新下载记录，如果查询出来没有数据那就直接插入下载次数为1的记录，如果查询出来有之前这个人的下载记录，就在下载次数上加1；
        LambdaUpdateWrapper<ForumArticleAttachmentDownload> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(ForumArticleAttachmentDownload::getDownload_count,attachmentDownload.getDownload_count()+1);
        forumArticleAttachmentDownloadService.saveOrUpdate(attachmentDownload, updateWrapper);
        //更新文章文件表中附件下载次数
        LambdaUpdateWrapper<ForumArticleAttachment> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(ForumArticleAttachment::getDownload_count,forumArticleAttachment.getDownload_count()+1).
                eq(ForumArticleAttachment::getFile_id, fileId);
        this.update(lambdaUpdateWrapper);
        //自己上传或者自己下载过了就直接返回
        if(userInfoFromSession.getUserId().equals(forumArticleAttachment.getUser_id())||download!=null||forumArticleAttachment.getIntegral().equals(0)){
            return forumArticleAttachment;
        }
        //不是特殊的情况的话就正常扣除积分
        //扣除下载人的积分
        userInfoService.updateUserIntegral(userInfoFromSession.getUserId(), UserIntegralOperTypeEnum.DOWNLOAD_ATTACHMENT
                , UserIntegralChangeTypeEnum.REDUCE.getChangeType(), forumArticleAttachment.getIntegral());

        //增加附件拥有者的积分
        userInfoService.updateUserIntegral(forumArticleAttachment.getUser_id(), UserIntegralOperTypeEnum.DOWNLOAD_ATTACHMENT,
                UserIntegralChangeTypeEnum.ADD.getChangeType(), forumArticleAttachment.getIntegral());

        //记录消息
        LambdaQueryWrapper<ForumArticle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ForumArticle::getArticle_id,forumArticleAttachment.getArticle_id());
        ForumArticle article = forumArticleService.getOne(queryWrapper);
        UserMessage userMessage = new UserMessage();
         userMessage.setArticle_id(article.getArticle_id());
         userMessage.setArticle_title(article.getTitle());
         userMessage.setMessage_type(MessageTypeEnum.DOWNLOAD_ATTACHMENT.getType());
         userMessage.setCreate_time(new Date());
         userMessage.setSend_nick_name(userInfoFromSession.getNickName());
         userMessage.setSend_user_id(userInfoFromSession.getUserId());
         userMessage.setReceived_user_id(forumArticleAttachment.getUser_id());
         userMessage.setStatus(MessageStatusEnum.NO_READ.getStatus());
         userMessage.setComment_id(0);

         userMessageService.save(userMessage);

        return forumArticleAttachment;
    }
}




