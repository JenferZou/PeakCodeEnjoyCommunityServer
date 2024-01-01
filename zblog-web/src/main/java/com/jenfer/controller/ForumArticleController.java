package com.jenfer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jenfer.annotation.GloballInterceptor;
import com.jenfer.annotation.VerifyParam;
import com.jenfer.config.WebConfig;
import com.jenfer.constants.Constants;
import com.jenfer.dto.SessionWebUserDto;
import com.jenfer.enums.*;
import com.jenfer.exception.BusinessException;
import com.jenfer.pojo.*;
import com.jenfer.service.*;
import com.jenfer.utils.CopyTools;
import com.jenfer.vo.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.assertj.core.api.IntArrayAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/forum")
public class ForumArticleController extends ABaseController {

    @Autowired
    private ForumArticleService forumArticleService;

    @Autowired
    private ForumArticleAttachmentService forumArticleAttachmentService;

    @Autowired
    private LikeRecordService likeRecordService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private ForumArticleAttachmentDownloadService forumArticleAttachmentDownloadService;

    @Resource
    private WebConfig webConfig;

    private static final Logger logger = LoggerFactory.getLogger(ForumArticleController.class);

    @RequestMapping("/loadArticle")
    public ResponseVo loadArticle(HttpSession session,Integer boardId,Integer pBoardId,Integer orderType,
                                  Integer pageNo){
        //分页需要传递的参数Page
        Page<ForumArticle> articlePage = new Page<>(pageNo==null?1:pageNo, PageSize.SIZE10.getSize());
        //分页需要传递的参数board_id
        Integer board_id =boardId==null||boardId==0?null:boardId;
        //分页需要传递的参数通过orderType来查找到对应的sql语句也就是对应的排序规则
        ArticleorderTypeEnum articleorderTypeEnum = ArticleorderTypeEnum.getByType(orderType);
        articleorderTypeEnum=articleorderTypeEnum==null?ArticleorderTypeEnum.HOT:articleorderTypeEnum;

        IPage<ForumArticleVo> forumArticleVoIPage = forumArticleService.queryArticlesWithPagination(articlePage, board_id, pBoardId, articleorderTypeEnum.getOrderSql());
        List<ForumArticleVo> records = forumArticleVoIPage.getRecords();


        PaginationResultVo<ForumArticleVo> forumArticlePaginationResultVo = new PaginationResultVo<>();
        forumArticlePaginationResultVo.setList(records);
        forumArticlePaginationResultVo.setPageNo(pageNo);
        forumArticlePaginationResultVo.setPageSize((int) forumArticleVoIPage.getSize());
        forumArticlePaginationResultVo.setTotalCount((int)forumArticleVoIPage.getTotal());
        forumArticlePaginationResultVo.setPageTotal((int)forumArticleVoIPage.getPages());
        return getSuccessResponseVo(convert2PaginationVo(forumArticlePaginationResultVo, ForumArticleVo.class));


    }

    @RequestMapping("/getArticleDetail")
    @GloballInterceptor(checkParams = true)
    public ResponseVo getArticleDetail (HttpSession session, @VerifyParam(required = true)String articleId){
        ForumArticle forumArticle = forumArticleService.readArticle(articleId);
        SessionWebUserDto userInfoFromSession = getUserInfoFromSession(session);
        if((ArticleStatusEnum.NO_AUDIT.getStatus().equals(forumArticle.getStatus())&&(userInfoFromSession==null||!userInfoFromSession.getUserId().equals(forumArticle.getUser_id())||!userInfoFromSession.getAdmin()))
        ||ArticleStatusEnum.DEL.getStatus().equals(forumArticle.getStatus())){
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
        ForumArticleDetailVo forumArticleDetailVo = new ForumArticleDetailVo();
        forumArticleDetailVo.setForumArticleVo(CopyTools.copy(forumArticle,ForumArticleVo.class));
        //判断是否有附件
        if(forumArticle.getAttachment_type()== Constants.ONE){
            LambdaQueryWrapper<ForumArticleAttachment> forumArticleAttachmentLambdaQueryWrapper = new LambdaQueryWrapper<>();
            forumArticleAttachmentLambdaQueryWrapper.eq(ForumArticleAttachment::getArticle_id, articleId);
            List<ForumArticleAttachment> forumArticleAttachmentlist = forumArticleAttachmentService.list(forumArticleAttachmentLambdaQueryWrapper);
            if(!forumArticleAttachmentlist.isEmpty()){
                forumArticleDetailVo.setForumArticleAttachmentVo(CopyTools.copy(forumArticleAttachmentlist.get(0), ForumArticleAttachmentVo.class));
            }
        }
        //判断是否点赞
        if(userInfoFromSession!=null){
            LambdaQueryWrapper<LikeRecord> likeRecordLambdaQueryWrapper = new LambdaQueryWrapper<>();
            likeRecordLambdaQueryWrapper.eq(LikeRecord::getObject_id,articleId)
                    .eq(LikeRecord::getOp_type, OperRecordOpTypeEnum.ARTICLE_LIKE.getType())
                    .eq(LikeRecord::getUser_id,userInfoFromSession.getUserId());
            LikeRecord likeRecord = likeRecordService.getOne(likeRecordLambdaQueryWrapper);
            if(likeRecord!=null){
                forumArticleDetailVo.setHaveLike(true);
            }
        }

        return getSuccessResponseVo(forumArticleDetailVo);
    }



    @RequestMapping("/doLike")
    @GloballInterceptor(checkLogin = true,checkParams = true)
    public ResponseVo doLike (HttpSession session, @VerifyParam(required = true)String articleId){
        SessionWebUserDto sessionWebUserDto = getUserInfoFromSession(session);
        if(sessionWebUserDto==null){
            throw new BusinessException(ResponseCodeEnum.CODE_901);
        }
        likeRecordService.doLike(articleId,sessionWebUserDto.getUserId(),sessionWebUserDto.getNickName(),OperRecordOpTypeEnum.ARTICLE_LIKE);

        return getSuccessResponseVo(null);

    }


    @RequestMapping("/getUserDownloadInfo")
    @GloballInterceptor(checkLogin = true,checkParams = true)
    public ResponseVo getUserDownloadInfo (HttpSession session, @VerifyParam(required = true)String fileId) {
        SessionWebUserDto webUserInfo = getUserInfoFromSession(session);

        LambdaQueryWrapper<UserInfo> userInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<UserInfo> getbyId = userInfoLambdaQueryWrapper.eq(UserInfo::getUser_id, webUserInfo.getUserId());
        UserInfo userInfo = userInfoService.getOne(getbyId);

        UserDownloadInfoVo userDownloadInfoVo = new UserDownloadInfoVo();
        userDownloadInfoVo.setUser_integral(userInfo.getCurrent_integral());

        LambdaQueryWrapper<ForumArticleAttachmentDownload> downLoadQuery = new LambdaQueryWrapper<>();
        downLoadQuery.eq(ForumArticleAttachmentDownload::getFile_id, fileId)
                .eq(ForumArticleAttachmentDownload::getUser_id, webUserInfo.getUserId());

        ForumArticleAttachmentDownload attachmentDownload = forumArticleAttachmentDownloadService.getOne(downLoadQuery);

        if(attachmentDownload!=null){
            userDownloadInfoVo.setHave_download(true);
        }
        return getSuccessResponseVo(userDownloadInfoVo);
    }


    @RequestMapping("/attachmentDownload")
    @GloballInterceptor(checkLogin = true,checkParams = true)
    public void attachmentDownload (HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                          @VerifyParam(required = true)String fileId){
        ForumArticleAttachment attachment = forumArticleAttachmentService.downloadAttachment(fileId,getUserInfoFromSession(session));
        InputStream in = null;
        OutputStream out = null;
        String downloadFileName = attachment.getFile_name();
        String filePath = webConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE +
                Constants.FILE_FOLDER_ATTACHMENT + attachment.getFile_path();
        File file = new File(filePath);
        try {
            in = new FileInputStream(file);
            out = response.getOutputStream();
            response.setContentType("application/x-msdownload;charset=UTF-8");
            //解决中文乱码
            if(request.getHeader("User-Agent").toLowerCase().indexOf("msie") > 0){
                downloadFileName = URLEncoder.encode(downloadFileName, "UTF-8");
            }else {
                downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "ISO8859-1");
            }
            response.setHeader("Content-Disposition", "attachment;filename=\""+downloadFileName+"\"");
            byte[] byteData = new byte[1024];
            int len = 0;
            while ((len = in.read(byteData))!= -1) {
                out.write(byteData, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            throw new BusinessException("下载失败");
        }finally {
            try {
                if(in!=null){
                    in.close();
                }
            }catch (IOException e){
                logger.error("IO异常");
            }
            try {
                if(out!=null){
                    out.close();
                }
            }catch (IOException e){
                logger.error("IO异常");
            }

        }

    }



}
