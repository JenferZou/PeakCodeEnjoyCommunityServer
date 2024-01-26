package com.jenfer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenfer.constants.Constants;
import com.jenfer.dto.FileUploadDto;
import com.jenfer.enums.*;
import com.jenfer.exception.BusinessException;
import com.jenfer.mappers.ForumArticleMapper;
import com.jenfer.mappers.ForumCommentMapper;
import com.jenfer.mappers.UserInfoMapper;
import com.jenfer.pojo.ForumArticle;
import com.jenfer.pojo.ForumComment;
import com.jenfer.pojo.UserInfo;
import com.jenfer.pojo.UserMessage;
import com.jenfer.service.ForumCommentService;
import com.jenfer.service.UserInfoService;
import com.jenfer.service.UserMessageService;
import com.jenfer.utils.FileUtils;
import com.jenfer.utils.StringTools;
import com.jenfer.utils.SysCacheUtils;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author Jenf
* @description 针对表【forum_comment(评论)】的数据库操作Service实现
* @createDate 2023-09-24 11:25:25
*/
@Service
public class ForumCommentServiceImpl extends ServiceImpl<ForumCommentMapper, ForumComment>
    implements ForumCommentService {


    @Autowired
    private ForumArticleMapper forumArticleMapper;


    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserMessageService userMessageService;



    @Resource
    private FileUtils fileUtils;
    @Override
    public List<ForumComment> queryListByParam(Page<ForumComment> page,
                                               ForumComment forumComment,
                                               String orderBy,
                                               Boolean queryChildren,
                                               String currentId,
                                               Boolean haveLike
                                               ) {
        IPage<ForumComment> forumCommentIPage = this.baseMapper.queryCommentbyParam(page,forumComment,orderBy,currentId,haveLike);
        List<ForumComment> forumCommentList = forumCommentIPage.getRecords();
        if(queryChildren){
            ForumComment queryComment = new ForumComment();

            queryComment.setArticle_id(forumComment.getArticle_id());
            queryComment.setStatus(forumComment.getStatus());

            List<Integer> pcommentIdList = forumCommentList.stream().map(ForumComment::getComment_id).distinct().collect(Collectors.toList());
            IPage<ForumComment> commentIPage = this.baseMapper.querySubCommentbyParam(page, queryComment,orderBy,currentId,haveLike,pcommentIdList);
            List<ForumComment> subCommentList = commentIPage.getRecords();
            Map<Integer, List<ForumComment>> tempMap = subCommentList.stream().collect(Collectors.groupingBy(ForumComment::getP_comment_id));

            forumCommentList.forEach(item->{
                item.setSub_forumComment_list(tempMap.get(item.getComment_id()));
            });
        }


        return forumCommentList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeTopType(String userId, Integer commentId, Integer topType) {
        CommentTopTypeEnum topTypeEnum = CommentTopTypeEnum.getByType(topType);
        if(null==topTypeEnum){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        ForumComment forumComment = this.baseMapper.selectById(commentId);
        if(forumComment==null){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        LambdaQueryWrapper<ForumArticle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ForumArticle::getArticle_id,forumComment.getArticle_id());
        ForumArticle forumArticle = forumArticleMapper.selectOne(queryWrapper);
        if (forumArticle == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        //只有一级评论能置顶二级评论不行，当前文章作者是否是当前登录用户
        if(!forumArticle.getUser_id().equals(userId)||forumComment.getP_comment_id()!=0){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        //判断是否已经是置顶了
        if(forumComment.getTop_type().equals(topType)){
            return;
        }
        //如果要换一个评论置顶那么先将当前文章的置顶评论给取消然后新增新的评论置顶
        if(CommentTopTypeEnum.TOP.getType().equals(topType)){
            LambdaUpdateWrapper<ForumComment> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(ForumComment::getArticle_id,forumComment.getArticle_id())
                    .eq(ForumComment::getTop_type,CommentTopTypeEnum.TOP.getType()).set(ForumComment::getTop_type,CommentTopTypeEnum.NO_TOP.getType());
            this.baseMapper.update(null,updateWrapper);
        }

        forumComment.setTop_type(topType);
        this.baseMapper.updateById(forumComment);


    }

    @Override
    public void postComment(ForumComment forumComment, MultipartFile image) {
        LambdaQueryWrapper<ForumArticle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ForumArticle::getArticle_id,forumComment.getArticle_id());
        ForumArticle forumArticle = forumArticleMapper.selectOne(queryWrapper);
        if(forumArticle==null ||!ArticleStatusEnum.AUDIT.getStatus().equals(forumArticle.getStatus())){
            throw new BusinessException("评论文章不存在");
        }
        ForumComment pComment = null;
        if (forumComment.getP_comment_id()!=0){
            LambdaQueryWrapper<ForumComment> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ForumComment::getComment_id,forumComment.getP_comment_id());
            pComment = this.baseMapper.selectOne(wrapper);
            if (pComment==null){
                throw new BusinessException("回复评论不存在");
            }
        }
        //判断回复的用户是否存在
        if(!StringTools.isEmpty(forumComment.getReply_user_id())){
            LambdaQueryWrapper<UserInfo> userInfoQuery = new LambdaQueryWrapper<>();
            userInfoQuery.eq(UserInfo::getUser_id,forumComment.getReply_user_id());
            UserInfo userInfo = userInfoService.getOne(userInfoQuery);
            if(userInfo==null){
                throw new BusinessException("回复用户不存在");
            }
            forumComment.setReply_nick_name(userInfo.getNick_name());

        }
        forumComment.setPost_time(new Date());
        if(image!=null){
            FileUploadDto fileUploadDto = fileUtils.uploadFile2Local(image, Constants.FILE_FOLDER_IMAGE, FileUploadTypeEnum.COMMEMT_IMAGE);
            forumComment.setImg_path(fileUploadDto.getLocalPath());
        }
        Boolean needAudit = SysCacheUtils.getSysSetting().getSysSettingAuditDto().getCommentAudit();
        forumComment.setStatus(needAudit? CommentStatusEnum.NO_AUDIT.getStatus() : CommentStatusEnum.AUDIT.getStatus());
        this.baseMapper.insert(forumComment);
        if(needAudit){
            return;
        }

        updateCommentInfo(forumComment,forumArticle,pComment);


    }

    @Override
    public Long queryCountByArticleId(String articleId) {
        LambdaQueryWrapper<ForumComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ForumComment::getArticle_id,articleId);
       return this.baseMapper.selectCount(wrapper);
    }

    @Override
    public Long queryCountByArticleIdAndPCommentId(String articleId, Integer pCommentId) {
        LambdaQueryWrapper<ForumComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ForumComment::getArticle_id,articleId).eq(ForumComment::getP_comment_id,pCommentId);
        return this.baseMapper.selectCount(wrapper);
    }


    public void updateCommentInfo(ForumComment forumComment,ForumArticle forumArticle,ForumComment pComment){
        Integer commengIntegral = SysCacheUtils.getSysSetting().getSysSettingCommentDto().getCommentIntegral();
        if(commengIntegral!=null&&commengIntegral>0){
           userInfoService.updateUserIntegral(forumComment.getUser_id(), UserIntegralOperTypeEnum.POST_COMMENT,
                   UserIntegralChangeTypeEnum.ADD.getChangeType(), commengIntegral);
        }
        //评论数只包含一级评论的情况
//        if(forumComment.getP_comment_id()==0){
//            forumArticleMapper.updateArticleCount(UpdateArticleTypeEnum.COMMENT_COUNT.getType(), Constants.ONE,forumComment.getArticle_id());
//        }
        //评论数包含一级评论和二级评论
        forumArticleMapper.updateArticleCount(UpdateArticleTypeEnum.COMMENT_COUNT.getType(), Constants.ONE,forumComment.getArticle_id());


        //记录消息
        UserMessage userMessage = new UserMessage();
        userMessage.setMessage_type(MessageTypeEnum.COMMENT.getType());
        userMessage.setCreate_time(new Date());
        userMessage.setArticle_id(forumComment.getArticle_id());
        userMessage.setArticle_title(forumArticle.getTitle());
        userMessage.setComment_id(forumComment.getComment_id());
        userMessage.setSend_user_id(forumComment.getUser_id());
        userMessage.setSend_nick_name(forumComment.getNick_name());
        userMessage.setStatus(MessageStatusEnum.NO_READ.getStatus());
        //当前评论的是文章的作者
        if(forumComment.getP_comment_id()==0){
            userMessage.setReceived_user_id(forumArticle.getUser_id());
        } else if (forumComment.getP_comment_id()!=0&&StringTools.isEmpty(forumComment.getReply_user_id())) {
            //回复的当前二级评论跟一级评论是同一个人
            userMessage.setReceived_user_id(pComment.getUser_id());
        } else if (forumComment.getP_comment_id()!=0&&StringTools.isEmpty(forumComment.getReply_user_id())){
            //回复的当前二级评论跟一级评论不是同一个人
            userMessage.setReceived_user_id(forumComment.getReply_user_id());
        }
        if(!forumComment.getUser_id().equals(userMessage.getReceived_user_id())){
            userMessageService.save(userMessage);
        }


    }


}




