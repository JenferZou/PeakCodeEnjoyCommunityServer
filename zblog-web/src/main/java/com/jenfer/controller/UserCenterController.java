package com.jenfer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jenfer.annotation.GloballInterceptor;
import com.jenfer.annotation.VerifyParam;
import com.jenfer.constants.Constants;
import com.jenfer.dto.SessionWebUserDto;
import com.jenfer.enums.*;
import com.jenfer.exception.BusinessException;
import com.jenfer.pojo.*;
import com.jenfer.service.*;
import com.jenfer.utils.CopyTools;
import com.jenfer.vo.ForumArticleVo;
import com.jenfer.vo.PaginationResultVo;
import com.jenfer.vo.ResponseVo;
import com.jenfer.vo.UserInfoVo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController("userCenterController")
@RequestMapping("/ucenter")
public class UserCenterController extends ABaseController{

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private ForumArticleService forumArticleService;


    @Autowired
    private LikeRecordService likeRecordService;

    @Autowired
    private UserIntegralRecordService userIntegralRecordService;


    @Autowired
    private UserMessageService userMessageService;


    @RequestMapping("getUserInfo")
    @GloballInterceptor(checkParams = true)
    public ResponseVo getUserInfo(@VerifyParam(required = true)String userId){
        UserInfo userInfo = userInfoService.getById(userId);
        if(null==userInfo || UserStatusEnum.DISABLE.getStatus().equals(userInfo.getStatus())){
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
        LambdaQueryWrapper<ForumArticle> forumArticleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        forumArticleLambdaQueryWrapper.eq(ForumArticle::getUser_id,userId).eq(ForumArticle::getStatus, ArticleStatusEnum.AUDIT.getStatus());
        long postCount = forumArticleService.count(forumArticleLambdaQueryWrapper);
        UserInfoVo userInfoVo = CopyTools.copy(userInfo,UserInfoVo.class);
        userInfoVo.setPost_count((int)postCount);

        LambdaQueryWrapper<LikeRecord> likeRecordLambdaQueryWrapper = new LambdaQueryWrapper<LikeRecord>();
        likeRecordLambdaQueryWrapper.eq(LikeRecord::getUser_id,userId);
        long likeCount = likeRecordService.count(likeRecordLambdaQueryWrapper);
        userInfoVo.setLike_count((int)likeCount);

        return getSuccessResponseVo(userInfoVo);
    }


    @RequestMapping("/loadUserArticle")
    @GloballInterceptor(checkParams = true)
    public ResponseVo loadUserArticle(HttpSession session,@VerifyParam(required = true)String userId,
                                      @VerifyParam(required = true)Integer type,Integer pageNo){
        UserInfo userInfo = userInfoService.getById(userId);
        if(null==userInfo || UserStatusEnum.DISABLE.getStatus().equals(userInfo.getStatus())){
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
        LambdaQueryWrapper<ForumArticle> forumArticleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        forumArticleLambdaQueryWrapper
                .orderByDesc(ForumArticle::getTop_type)
                .orderByDesc(ForumArticle::getPost_time);
       if(type==0){
           forumArticleLambdaQueryWrapper.eq(ForumArticle::getUser_id,userId);
       }else if(type==1){
           //点击就查询自己发过评论的文章
           forumArticleLambdaQueryWrapper.inSql(ForumArticle::getArticle_id,
                   "select article_id from forum_comment where user_id = '" + userId + "' and status = 1");
       } else if (type==2) {
           //点击就查询自己点过赞的文章
           forumArticleLambdaQueryWrapper.inSql(ForumArticle::getArticle_id,
                   "select object_id from like_record where user_id = '" + userId + "' and op_type = 0 and status = 1");
       }

        SessionWebUserDto userDto = getUserInfoFromSession(session);
        if (userDto == null) {
            forumArticleLambdaQueryWrapper.eq(ForumArticle::getStatus, ArticleStatusEnum.AUDIT.getStatus());
        }
        Page<ForumArticle> forumArticlePage = new Page<>(pageNo==null? Constants.ONE : pageNo, Constants.LENGTH_10);


        IPage<ForumArticle> forumArticleVoIPage = forumArticleService.page(forumArticlePage, forumArticleLambdaQueryWrapper);
        PaginationResultVo<ForumArticleVo> forumArticlePaginationResultVo = new PaginationResultVo<>();
        List<ForumArticle> resultVo = forumArticleVoIPage.getRecords();
        forumArticlePaginationResultVo.setList(CopyTools.copyList(resultVo,ForumArticleVo.class));
        forumArticlePaginationResultVo.setPageNo(pageNo);
        forumArticlePaginationResultVo.setPageSize((int) forumArticleVoIPage.getSize());
        forumArticlePaginationResultVo.setTotalCount((int)forumArticleVoIPage.getTotal());
        forumArticlePaginationResultVo.setPageTotal((int)forumArticleVoIPage.getPages());
        return getSuccessResponseVo(convert2PaginationVo(forumArticlePaginationResultVo, ForumArticleVo.class));
        }



    @RequestMapping("/updateUserInfo")
    @GloballInterceptor(checkParams = true)
    public ResponseVo updateUserInfo(HttpSession session,
                                     Integer sex,
                                     @RequestParam("person_description") @VerifyParam(max = 100)String persionDescription,
                                     MultipartFile avatar){
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        UserInfo userInfo = new UserInfo();
        userInfo.setUser_id(userDto.getUserId());
        userInfo.setSex(sex);
        userInfo.setPerson_description(persionDescription);
        userInfoService.updateUserInfo(userInfo,avatar);
        return getSuccessResponseVo(null);

    }




    @RequestMapping("/loadUserIntegralRecord")
    @GloballInterceptor(checkParams = true)
    public ResponseVo loadUserIntegralRecord(HttpSession session, Integer pageNo,
                                             String createTimeStart,
                                             String createTimeEnd){
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        LambdaQueryWrapper<UserIntegralRecord> userIntegralRecordLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userIntegralRecordLambdaQueryWrapper.eq(UserIntegralRecord::getUser_id,userDto.getUserId())
                .orderByDesc(UserIntegralRecord::getRecord_id);
        if (StringUtils.isNotBlank(createTimeStart) && StringUtils.isNotBlank(createTimeEnd)) {
            userIntegralRecordLambdaQueryWrapper.between(UserIntegralRecord::getCreate_time, createTimeStart, createTimeEnd);
        }
        IPage<UserIntegralRecord> resultVo = userIntegralRecordService.page(new Page<>(pageNo == null ? Constants.ONE : pageNo, Constants.LENGTH_10), userIntegralRecordLambdaQueryWrapper);
        PaginationResultVo<UserIntegralRecord> paginationResultVo = new PaginationResultVo<>();
        paginationResultVo.setList(CopyTools.copyList(resultVo.getRecords(),UserIntegralRecord.class));
        paginationResultVo.setPageNo(pageNo);
        paginationResultVo.setPageSize((int) resultVo.getSize());
        paginationResultVo.setTotalCount((int) resultVo.getTotal());
        paginationResultVo.setPageTotal((int) resultVo.getPages());
        return getSuccessResponseVo(paginationResultVo);

    }



    @RequestMapping("/getMessageCount")
    @GloballInterceptor(checkParams = true)
    public ResponseVo getMessageCount(HttpSession session){
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        if(null==userDto){
            throw new BusinessException("未检测到当前登录信息或登录信息失效,请重试");
        }
        return getSuccessResponseVo(userMessageService.getUserMessageCount(userDto.getUserId()));

    }



    @RequestMapping("/loadMessageList")
    @GloballInterceptor(checkParams = true,checkLogin = true)
    public ResponseVo loadMessageList(HttpSession session,@VerifyParam(required = true) String code,Integer pageNo){
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        MessageTypeEnum typeEnum = MessageTypeEnum.getByCode(code);
        if(null==typeEnum){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        LambdaQueryWrapper<UserMessage> userMessageLambdaQueryWrapper = new LambdaQueryWrapper<UserMessage>();
        userMessageLambdaQueryWrapper.eq(UserMessage::getReceived_user_id,userDto.getUserId())
                .eq(UserMessage::getMessage_type,typeEnum.getType())
                .eq(UserMessage::getReceived_user_id,userDto.getUserId())
                .orderByDesc(UserMessage::getMessage_id);
        IPage<UserMessage> resultVo = userMessageService.page(new Page<>(pageNo == null ? Constants.ONE : pageNo, Constants.LENGTH_10), userMessageLambdaQueryWrapper);
        PaginationResultVo paginationResultVo = new PaginationResultVo<UserMessage>();
        paginationResultVo.setList(CopyTools.copyList(resultVo.getRecords(),UserMessage.class));
        paginationResultVo.setPageNo(pageNo);
        paginationResultVo.setPageSize((int) resultVo.getSize());
        paginationResultVo.setTotalCount((int) resultVo.getTotal());
        paginationResultVo.setPageTotal((int) resultVo.getPages());

        //将读过的消息设置成已读
        if(pageNo==null||pageNo==1){
            LambdaUpdateWrapper<UserMessage> userMessageLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            userMessageLambdaUpdateWrapper.eq(UserMessage::getReceived_user_id,userDto.getUserId())
                    .eq(UserMessage::getMessage_type,typeEnum.getType())
                    .set(UserMessage::getStatus, MessageStatusEnum.READ.getStatus());
            userMessageService.update(userMessageLambdaUpdateWrapper);
        }


        return getSuccessResponseVo(paginationResultVo);

    }


}

