package com.jenfer.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenfer.constants.Constants;
import com.jenfer.enums.MessageStatusEnum;
import com.jenfer.enums.MessageTypeEnum;
import com.jenfer.enums.OperRecordOpTypeEnum;
import com.jenfer.enums.UpdateArticleTypeEnum;
import com.jenfer.exception.BusinessException;
import com.jenfer.mappers.ForumArticleMapper;
import com.jenfer.mappers.ForumCommentMapper;
import com.jenfer.mappers.LikeRecordMapper;
import com.jenfer.mappers.UserMessageMapper;
import com.jenfer.pojo.ForumArticle;
import com.jenfer.pojo.ForumComment;
import com.jenfer.pojo.LikeRecord;
import com.jenfer.pojo.UserMessage;
import com.jenfer.service.LikeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
* @author Jenf
* @description 针对表【like_record(点赞记录)】的数据库操作Service实现
* @createDate 2023-09-24 11:25:25
*/
@Service
public class LikeRecordServiceImpl extends ServiceImpl<LikeRecordMapper, LikeRecord>
    implements LikeRecordService {

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Autowired
    private ForumArticleMapper forumArticleMapper;

    @Autowired
    private ForumCommentMapper forumCommentMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doLike(String objectId, String userId, String nickName, OperRecordOpTypeEnum opType) {
        UserMessage userMessage = new UserMessage();
        userMessage.setCreate_time(new Date());
        LikeRecord likeRecord = null;
        switch (opType){
            case ARTICLE_LIKE:
                ForumArticle forumArticle = forumArticleMapper.selectOne(new LambdaUpdateWrapper<ForumArticle>().eq(ForumArticle::getArticle_id, objectId));
                if (forumArticle == null) {
                    throw new BusinessException("文章不存在");
                }
                articleLike(objectId, forumArticle,userId, opType);
                userMessage.setArticle_id(objectId);
                userMessage.setArticle_title(forumArticle.getTitle());
                userMessage.setMessage_type(MessageTypeEnum.ARTICLE_LIKE.getType());
                userMessage.setComment_id(Constants.ZERO);
                userMessage.setReceived_user_id(forumArticle.getUser_id());
                break;
            case COMMENT_LIKE:
                ForumComment forumComment = forumCommentMapper.selectById(Integer.parseInt(objectId));
                forumArticle = forumArticleMapper.selectOne(new LambdaUpdateWrapper<ForumArticle>().eq(ForumArticle::getArticle_id,forumComment.getArticle_id()));
                if(null==forumComment){
                    throw new BusinessException("评论不存在");
                }
                commentLike(objectId,forumComment,userId, opType);
                userMessage.setArticle_id(objectId);
                userMessage.setArticle_title(forumArticle.getTitle());
                userMessage.setMessage_type(MessageTypeEnum.COMMENT_LIKE.getType());
                userMessage.setComment_id(forumComment.getComment_id());
                userMessage.setReceived_user_id(forumArticle.getUser_id());
                userMessage.setMessage_content(forumComment.getContent());
                break;
        }
        userMessage.setSend_user_id(userId);
        userMessage.setSend_nick_name(nickName);
        userMessage.setStatus(MessageStatusEnum.NO_READ.getStatus());
        if(!userId.equals(userMessage.getReceived_user_id())){
            LambdaQueryWrapper<UserMessage> queryUserMes = new LambdaQueryWrapper<UserMessage>().eq(UserMessage::getSend_user_id, userMessage.getSend_user_id())
                    .eq(UserMessage::getArticle_id, userMessage.getArticle_id())
                    .eq(UserMessage::getComment_id, userMessage.getComment_id())
                    .eq(UserMessage::getMessage_type, userMessage.getMessage_type());
            UserMessage dbInfo = userMessageMapper.selectOne(queryUserMes);
            if(dbInfo==null){
                userMessageMapper.insert(userMessage);
            }
        }
    }



    public void articleLike(String articleId,ForumArticle forumArticle,String userId,OperRecordOpTypeEnum opTypeEnum){
        LambdaQueryWrapper<LikeRecord> likeRecordLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //查询是否有相关的点赞记录
         likeRecordLambdaQueryWrapper
                .eq(LikeRecord::getObject_id, articleId)
                .eq(LikeRecord::getUser_id, userId)
                .eq(LikeRecord::getOp_type, opTypeEnum.getType());
        LikeRecord likeRecord = this.getOne(likeRecordLambdaQueryWrapper);
        if(likeRecord!=null){
            //如果有对应的点赞记录，那就删除点赞记录也就是取消点赞
            LambdaQueryWrapper<LikeRecord> likeRecordLambdaDeleteWrapper = new LambdaQueryWrapper<>();
            likeRecordLambdaDeleteWrapper.eq(LikeRecord::getObject_id, articleId)
                    .eq(LikeRecord::getUser_id, userId)
                    .eq(LikeRecord::getOp_type, opTypeEnum.getType());
            this.remove(likeRecordLambdaDeleteWrapper);
            forumArticleMapper.updateArticleCount(UpdateArticleTypeEnum.GOOD_COUNT.getType(),-1,articleId);
        }else {

            //如果有文章的话那就添加点赞记录
            LikeRecord newlikeRecord = new LikeRecord();
            newlikeRecord.setObject_id(articleId);
            newlikeRecord.setUser_id(userId);
            newlikeRecord.setOp_type(opTypeEnum.getType());
            newlikeRecord.setAuthor_user_id(forumArticle.getUser_id());
            newlikeRecord.setCreate_time(new Date());
            this.save(newlikeRecord);
            forumArticleMapper.updateArticleCount(UpdateArticleTypeEnum.GOOD_COUNT.getType(), Constants.ONE,articleId);

        }

    }


    public void commentLike(String objectId,ForumComment forumComment,String userId,OperRecordOpTypeEnum opTypeEnum){
        LambdaQueryWrapper<LikeRecord> likeRecordLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //查询是否有相关的点赞记录
         likeRecordLambdaQueryWrapper
               .eq(LikeRecord::getObject_id, objectId)
               .eq(LikeRecord::getUser_id, userId)
               .eq(LikeRecord::getOp_type, opTypeEnum.getType());

        LikeRecord likeRecord = this.getOne(likeRecordLambdaQueryWrapper);
        if(likeRecord!=null){
            //如果有对应的点赞记录，那就删除点赞记录也就是取消点赞
            LambdaQueryWrapper<LikeRecord> likeRecordLambdaDeleteWrapper = new LambdaQueryWrapper<>();
            likeRecordLambdaDeleteWrapper.eq(LikeRecord::getObject_id, objectId)
                    .eq(LikeRecord::getUser_id, userId)
                    .eq(LikeRecord::getOp_type, opTypeEnum.getType());
            this.remove(likeRecordLambdaDeleteWrapper);
            forumCommentMapper.updateCommentGoodCount(-1,Integer.parseInt(objectId));
        }else {
            //如果有文章的话那就添加点赞记录
            LikeRecord newlikeRecord = new LikeRecord();
            newlikeRecord.setObject_id(objectId);
            newlikeRecord.setUser_id(userId);
            newlikeRecord.setOp_type(opTypeEnum.getType());
            newlikeRecord.setAuthor_user_id(forumComment.getUser_id());
            newlikeRecord.setCreate_time(new Date());
            this.save(newlikeRecord);
            forumCommentMapper.updateCommentGoodCount(1,Integer.parseInt(objectId));

        }

           }








}




