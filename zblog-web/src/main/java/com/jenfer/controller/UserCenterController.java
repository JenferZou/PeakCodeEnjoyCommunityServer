package com.jenfer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jenfer.annotation.GloballInterceptor;
import com.jenfer.annotation.VerifyParam;
import com.jenfer.constants.Constants;
import com.jenfer.dto.SessionWebUserDto;
import com.jenfer.enums.ArticleStatusEnum;
import com.jenfer.enums.ResponseCodeEnum;
import com.jenfer.enums.UserStatusEnum;
import com.jenfer.exception.BusinessException;
import com.jenfer.pojo.ForumArticle;
import com.jenfer.pojo.LikeRecord;
import com.jenfer.pojo.UserInfo;
import com.jenfer.service.ForumArticleService;
import com.jenfer.service.LikeRecordService;
import com.jenfer.service.UserInfoService;
import com.jenfer.utils.CopyTools;
import com.jenfer.vo.ForumArticleVo;
import com.jenfer.vo.PaginationResultVo;
import com.jenfer.vo.ResponseVo;
import com.jenfer.vo.UserInfoVo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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
        forumArticleLambdaQueryWrapper.eq(ForumArticle::getUser_id,userId).eq(ForumArticle::getStatus, ArticleStatusEnum.AUDIT.getStatus());
       forumArticleLambdaQueryWrapper.orderByDesc(ForumArticle::getPost_time);
       if(type==0){
           forumArticleLambdaQueryWrapper.eq(ForumArticle::getUser_id,userId);
       }else if(type==1){
           forumArticleLambdaQueryWrapper.eq(ForumArticle::getComment_user_id,userId);
       } else if (type==2) {
           forumArticleLambdaQueryWrapper.eq(ForumArticle::getLike_user_id,userId);
       }

        SessionWebUserDto userDto = getUserInfoFromSession(session);
        if (userDto != null) {
            forumArticleLambdaQueryWrapper.eq(ForumArticle::getUser_id, userDto.getUserId());
        }else {
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
    public ResponseVo updateUserInfo(HttpSession session, Integer sex, @VerifyParam(max = 100)String persionDescription,
                                     MultipartFile avatar){
        SessionWebUserDto userDto = getUserInfoFromSession(session);
        UserInfo userInfo = new UserInfo();
        userInfo.setSex(sex);
        userInfo.setPerson_description(persionDescription);
        userInfoService.updateUserInfo(userInfo,avatar);
        return getSuccessResponseVo(null);

    }



    }

