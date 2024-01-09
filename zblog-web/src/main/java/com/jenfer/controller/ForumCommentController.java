package com.jenfer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jenfer.annotation.GloballInterceptor;
import com.jenfer.annotation.VerifyParam;
import com.jenfer.constants.Constants;
import com.jenfer.dto.SessionWebUserDto;
import com.jenfer.enums.*;
import com.jenfer.exception.BusinessException;
import com.jenfer.pojo.ForumComment;
import com.jenfer.pojo.LikeRecord;
import com.jenfer.service.ForumCommentService;
import com.jenfer.service.LikeRecordService;
import com.jenfer.utils.StringTools;
import com.jenfer.utils.SysCacheUtils;
import com.jenfer.vo.ResponseVo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class ForumCommentController extends ABaseController{

    @Autowired
    private ForumCommentService forumCommentService;

    @Autowired
    private LikeRecordService likeRecordService;

    @RequestMapping("/loadComment")
    @GloballInterceptor(checkParams = true)
    public ResponseVo loadComment(HttpSession session,
                                  @VerifyParam(required = true) String articleId,
                                  Integer pageNo,
                                  Integer orderType){
        //热榜：点赞量多的
        final String ORDER_TYPR0 ="good_count desc,comment_id asc";
        //最新：时间最新发布(id自增所以id大的也就是时间最新的)
        final String ORDER_TYPR1 ="comment_id desc";
        //是否查询二级评论
        Boolean queryChildren = true;

        Boolean queryHaveLike = false;

        String orderBy = orderType==null || orderType== Constants.ZERO ? ORDER_TYPR0 : ORDER_TYPR1;

        //未开启评论功能
        if(!SysCacheUtils.getSysSetting().getSysSettingCommenDto().getCommentOpen()){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }


        ForumComment forumComment = new ForumComment();
        forumComment.setArticle_id(articleId);
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        String currentId = null;
        if(userDto!=null){
            queryHaveLike = true;
            currentId = userDto.getUserId();
        }else {
            forumComment.setStatus(ArticleStatusEnum.AUDIT.getStatus());
        }
        Page<ForumComment> forumCommentPage = new Page<>(pageNo==null?1:pageNo, PageSize.SIZE50.getSize());
        forumComment.setP_comment_id(0);
        List<ForumComment> forumCommentList = forumCommentService.queryListByParam(forumCommentPage, forumComment, orderBy, queryChildren, currentId, queryHaveLike);

        return getSuccessResponseVo(forumCommentList);

    }


    @RequestMapping("/doLike")
    @GloballInterceptor(checkLogin = true,checkParams = true)
    public ResponseVo doLike (HttpSession session, @VerifyParam(required = true)Integer commentId){
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        String objectId = String.valueOf(commentId);
        if(userDto==null){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        likeRecordService.doLike(objectId,userDto.getUserId(),userDto.getNickName(), OperRecordOpTypeEnum.COMMENT_LIKE);
        LambdaQueryWrapper<LikeRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LikeRecord::getObject_id,objectId).eq(LikeRecord::getUser_id,userDto.getUserId())
                .eq(LikeRecord::getOp_type,OperRecordOpTypeEnum.COMMENT_LIKE.getType());
        LikeRecord likeRecord = likeRecordService.getOne(queryWrapper);
        ForumComment comment = forumCommentService.getById(commentId);
        comment.setLikeType(likeRecord==null?0:1);
        return getSuccessResponseVo(comment);


    }


    @RequestMapping("/changeTopType")
    @GloballInterceptor(checkLogin = true,checkParams = true)
    public ResponseVo changeTopType (HttpSession session, @VerifyParam(required = true)Integer commentId,
                                     @VerifyParam(required = true) Integer topType){
       forumCommentService.changeTopType(getUserInfoFromSession(session).getUserId(),commentId,topType);

        return getSuccessResponseVo(null);


    }


    @RequestMapping("/postComment")
    @GloballInterceptor(checkLogin = true,checkParams = true)
    public ResponseVo postComment (HttpSession session, @VerifyParam(required = true)String articleId,
                                   @VerifyParam(required = true) Integer pCommentId,
                                   @VerifyParam(min = 5,max = 800) String content,
                                   MultipartFile image,String replyUserId){

        //未开启评论功能
        if(!SysCacheUtils.getSysSetting().getSysSettingCommenDto().getCommentOpen()){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        if(image==null&& StringTools.isEmpty(content)){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        SessionWebUserDto userDto = getUserInfoFromSession(session);

        ForumComment forumComment = new ForumComment();
        content = StringTools.transPageHtml(content);
        forumComment.setUser_id(userDto.getUserId());
        forumComment.setNick_name(userDto.getNickName());
        forumComment.setUser_ip_address(userDto.getProvince());
        forumComment.setP_comment_id(pCommentId);
        forumComment.setArticle_id(articleId);
        forumComment.setContent(content);
        forumComment.setReply_user_id(replyUserId);
        forumComment.setTop_type(CommentTopTypeEnum.NO_TOP.getType());

        forumCommentService.postComment(forumComment,image);
        if(pCommentId!=0){
            ForumComment queryComment = new ForumComment();
            queryComment.setArticle_id(articleId);
            queryComment.setP_comment_id(pCommentId);
            String orderType = "comment_id asc";
            List<ForumComment> commentList = forumCommentService.queryListByParam(null, queryComment, orderType, true, null, false);
            return getSuccessResponseVo(commentList);
        }

        return getSuccessResponseVo(forumComment);


    }

}
