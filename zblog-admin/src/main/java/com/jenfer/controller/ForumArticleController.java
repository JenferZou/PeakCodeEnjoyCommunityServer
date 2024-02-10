package com.jenfer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jenfer.annotation.GloballInterceptor;
import com.jenfer.annotation.VerifyParam;
import com.jenfer.config.AdminConfig;
import com.jenfer.constants.Constants;
import com.jenfer.controller.base.ABaseController;
import com.jenfer.dto.SessionWebUserDto;
import com.jenfer.enums.ArticleStatusEnum;
import com.jenfer.enums.PageSize;
import com.jenfer.enums.ResponseCodeEnum;
import com.jenfer.exception.BusinessException;
import com.jenfer.pojo.ForumArticle;
import com.jenfer.pojo.ForumArticleAttachment;
import com.jenfer.pojo.ForumComment;
import com.jenfer.service.ForumArticleAttachmentService;
import com.jenfer.service.ForumArticleService;
import com.jenfer.service.ForumCommentService;
import com.jenfer.utils.SysCacheUtils;
import com.jenfer.vo.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/forum")
public class ForumArticleController extends ABaseController {

    private static final Logger logger = LoggerFactory.getLogger(ForumArticleController.class);


    @Resource
    private ForumArticleService forumArticleService;

    @Resource
    private ForumArticleAttachmentService forumArticleAttachmentService;


    @Resource
    private AdminConfig adminConfig;


    @Resource
    private ForumCommentService forumCommentService;

    @RequestMapping("loadArticle")
    public ResponseVo loadArticle(ForumArticleRequestVo forumArticleRequestVo){

        IPage<ForumArticleVo> articleByList = forumArticleService.findArticleByList(forumArticleRequestVo);
        PaginationResultVo<ForumArticleVo> forumArticlePaginationResultVo = new PaginationResultVo<>();
        forumArticlePaginationResultVo.setList(articleByList.getRecords());
        forumArticlePaginationResultVo.setPageNo((int)articleByList.getCurrent());
        forumArticlePaginationResultVo.setPageSize((int) articleByList.getSize());
        forumArticlePaginationResultVo.setTotalCount((int)articleByList.getTotal());
        forumArticlePaginationResultVo.setPageTotal((int)articleByList.getPages());
        return getSuccessResponseVo(forumArticlePaginationResultVo);

    }


    @RequestMapping("/delArticle")
    public ResponseVo delArticle(@VerifyParam(required = true) String articleIds){
        forumArticleService.deleteForumArticleById(articleIds);
        return getSuccessResponseVo(null);
    }


    @RequestMapping("/updateBoard")
    public ResponseVo updateBoard(@VerifyParam(required = true) String articleId,
                                  @VerifyParam(required = true) Integer pBoardId,
                                  @VerifyParam(required = true) Integer boardId){

        boardId = boardId == null ? 0 : boardId;
        forumArticleService.updateBoard(articleId,pBoardId,boardId);
        return getSuccessResponseVo(null);
    }



    @RequestMapping("/getAttachment")
    public ResponseVo getAttachment(@VerifyParam(required = true) String articleId){
        LambdaQueryWrapper<ForumArticleAttachment> forumArticleAttachmentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        forumArticleAttachmentLambdaQueryWrapper.eq(ForumArticleAttachment::getArticle_id,articleId);
        List<ForumArticleAttachment> attachmentList = forumArticleAttachmentService.list(forumArticleAttachmentLambdaQueryWrapper);
        if(attachmentList.isEmpty()){
            throw new BusinessException("附件不存在");
        }
        return getSuccessResponseVo(attachmentList.get(0));

    }

    @RequestMapping("/attachmentDownload")
    @GloballInterceptor(checkLogin = true,checkParams = true)
    public void attachmentDownload (HttpSession session, HttpServletRequest request, HttpServletResponse response,
                                    @VerifyParam(required = true)String fileId){
        ForumArticleAttachment attachment = forumArticleAttachmentService.getById(fileId);
        InputStream in = null;
        OutputStream out = null;
        String downloadFileName = attachment.getFile_name();
        String filePath = adminConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE +
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






    @RequestMapping("/topArticle")
    public ResponseVo topArticle(@VerifyParam(required = true) String articleId,
                                 @VerifyParam(required = true) Integer topType){
        ForumArticle forumArticle = forumArticleService.getById(articleId);
        if(forumArticle==null){
            throw new BusinessException("文章不存在");
        }
        LambdaUpdateWrapper<ForumArticle> forumArticleLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        forumArticleLambdaUpdateWrapper.set(ForumArticle::getTop_type,topType);
        forumArticleService.update(forumArticle,forumArticleLambdaUpdateWrapper);
        return getSuccessResponseVo(null);
    }



    @RequestMapping("/auditArticle")
    public ResponseVo auditArticle(@VerifyParam(required = true) String articleIds){
        forumArticleService.auditArticle(articleIds);
        return getSuccessResponseVo(null);
    }



    @RequestMapping("/loadComment4Article")
    @GloballInterceptor(checkParams = true)
    public ResponseVo loadComment4Article(@VerifyParam(required = true) String articleId,
                                  Integer pageNo,
                                  Integer pageSize
                                ){

        Page<ForumComment> forumCommentPage = new Page<>(pageNo==null?1:pageNo,pageSize==null?PageSize.SIZE10.getSize():pageSize);
        List<ForumComment> forumCommentList = forumCommentService.queryListByParamByAdmin(forumCommentPage, articleId, true);
        //一级评论数目
        Long commentCountExcludeSub = forumCommentService.queryCountByArticleIdAndPCommentId(articleId, 0);
        PaginationResultVo<ForumComment> resultVo = new PaginationResultVo<>();
        resultVo.setList(forumCommentList);
        resultVo.setPageNo(pageNo);
        resultVo.setPageSize(pageSize);
        resultVo.setTotalCount(commentCountExcludeSub.intValue());
        return  getSuccessResponseVo(convert2PaginationVo(resultVo,ForumComment.class));

    }




    @RequestMapping("/loadComment")
    public ResponseVo loadComment(Integer pageNo, Integer pageSize,
                                  String contentFuzzy,String nickNameFuzzy,
                                  Integer status){
        Page<ForumComment> forumCommentPage = new Page<>(pageNo==null?1:pageNo,pageSize==null?PageSize.SIZE10.getSize():pageSize);
        List<ForumComment> forumCommentList = forumCommentService.queryListFuzzyByAdmin(forumCommentPage,contentFuzzy,nickNameFuzzy,status,true);
        PaginationResultVo<ForumComment> resultVo = new PaginationResultVo<>();
        resultVo.setList(forumCommentList);
        resultVo.setPageNo(pageNo);
        resultVo.setPageSize(pageSize);
        resultVo.setTotalCount(forumCommentList.size());
        return getSuccessResponseVo(convert2PaginationVo(resultVo,ForumComment.class));
    }



    @RequestMapping("/delComment")
    public ResponseVo delComment(@VerifyParam(required = true) String commentIds){
        forumCommentService.delComment(commentIds);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("/auditComment")
    public ResponseVo auditComment(@VerifyParam(required = true) String commentIds){
        forumCommentService.auditComment(commentIds);
        return getSuccessResponseVo(null);
    }






}
