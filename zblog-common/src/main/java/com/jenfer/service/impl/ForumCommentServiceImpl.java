package com.jenfer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenfer.enums.CommentTopTypeEnum;
import com.jenfer.enums.ResponseCodeEnum;
import com.jenfer.exception.BusinessException;
import com.jenfer.mappers.ForumArticleMapper;
import com.jenfer.mappers.ForumCommentMapper;
import com.jenfer.pojo.ForumArticle;
import com.jenfer.pojo.ForumComment;
import com.jenfer.service.ForumCommentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
                item.setSub_forumComment_list(tempMap.get(item.getP_comment_id()));
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
            updateWrapper.eq(ForumComment::getArticle_id,forumComment.getArticle_id()).set(ForumComment::getTop_type,0);
            this.baseMapper.update(null,updateWrapper);
        }
        ForumComment updateForumComment = new ForumComment();
        updateForumComment.setTop_type(topType);
        this.baseMapper.updateById(updateForumComment);


    }
}




