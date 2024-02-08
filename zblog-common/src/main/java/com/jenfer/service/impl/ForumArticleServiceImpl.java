package com.jenfer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenfer.config.AppConfig;
import com.jenfer.constants.Constants;
import com.jenfer.dto.FileUploadDto;
import com.jenfer.dto.SysSettingAuditDto;
import com.jenfer.enums.*;
import com.jenfer.exception.BusinessException;
import com.jenfer.mappers.ForumArticleAttachmentMapper;
import com.jenfer.mappers.ForumArticleMapper;
import com.jenfer.pojo.ForumArticle;
import com.jenfer.pojo.ForumArticleAttachment;
import com.jenfer.pojo.ForumBoard;
import com.jenfer.pojo.UserMessage;
import com.jenfer.service.*;
import com.jenfer.utils.*;
import com.jenfer.vo.ForumArticleRequestVo;
import com.jenfer.vo.ForumArticleVo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
* @author Jenf
* @description 针对表【forum_article(文章信息)】的数据库操作Service实现
* @createDate 2023-09-24 11:25:25
*/
@Service
public class ForumArticleServiceImpl extends ServiceImpl<ForumArticleMapper, ForumArticle>
    implements ForumArticleService {


    @Autowired
    private ForumBoardService forumBoardService;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private ForumArticleAttachmentMapper forumArticleAttachmentmapper;

    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private ImageUtils imageUtils;

    @Autowired
    private AppConfig appConfig;

    @Override
    public IPage<ForumArticleVo> queryArticlesWithPagination(Page<ForumArticle> page, Integer boardId, Integer pBoardId, String orderSql) {
        return baseMapper.queryArticlesWithPagination(page, boardId, pBoardId, orderSql);

    }

    @Override
    public ForumArticle readArticle(String articleId) {
        ForumArticle forumArticle = baseMapper.selectById(articleId);

        if(forumArticle==null){
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
        if(ArticleStatusEnum.AUDIT.getStatus().equals(forumArticle.getStatus())){
            baseMapper.updateArticleCount(UpdateArticleTypeEnum.READ_COUNT.getType(), Constants.ONE,articleId);
        }

        return forumArticle;
    }

    @Override
    public void postArticle(Boolean isAdmin, ForumArticle forumArticle, ForumArticleAttachment forumArticleAttachment, MultipartFile cover, MultipartFile attachment) {
        resetBoardInfo(isAdmin,forumArticle);
        Date curDate = new Date();
        String articleId = StringTools.getRandomString(Constants.LENGTH_15);
        forumArticle.setArticle_id(articleId);
        forumArticle.setPost_time(curDate);
        forumArticle.setLast_update_time(curDate);
        if (cover != null) {
            FileUploadDto fileUploadDto = fileUtils.uploadFile2Local(cover, Constants.FILE_FOLDER_IMAGE, FileUploadTypeEnum.ARTICLE_COVER);
            forumArticle.setCover(fileUploadDto.getLocalPath());
        }
        if (attachment != null) {
           uploadAttachment(forumArticle,forumArticleAttachment,attachment,false);
           forumArticle.setAttachment_type(ArticleAttachmentTypeEnum.HAVE_ATTACHMENT.getType());
        }else {
            forumArticle.setAttachment_type(ArticleAttachmentTypeEnum.NP_ATTACHMENT.getType());
        }

        //文章审核信息
        if(isAdmin){
            forumArticle.setStatus(ArticleStatusEnum.AUDIT.getStatus());
        }else {
            SysSettingAuditDto sysSettingAuditDto = SysCacheUtils.getSysSetting().getSysSettingAuditDto();
            forumArticle.setStatus(sysSettingAuditDto.getPostAudit()?ArticleStatusEnum.NO_AUDIT.getStatus():ArticleStatusEnum.AUDIT.getStatus());
        }
        //替换图片
        String content = forumArticle.getContent();
        if(!StringTools.isEmpty(content)){
            String month = imageUtils.restImageHtml(content);
            String replaceMonth = "/"+month+"/";
            content = content.replace(Constants.FILE_FOLDER_TEMP,replaceMonth);
            forumArticle.setContent(content);
            String markdownContent = forumArticle.getMarkdown_content();
            if(!StringTools.isEmpty(markdownContent)){
                markdownContent = markdownContent.replace(Constants.FILE_FOLDER_TEMP,replaceMonth);
                forumArticle.setMarkdown_content(markdownContent);
            }
        }


        baseMapper.insert(forumArticle);
        //增加积分
        Integer postIntegral = SysCacheUtils.getSysSetting().getSysSettingPostDto().getPostIntegral();
        if(postIntegral>0&&ArticleStatusEnum.AUDIT.equals(forumArticle.getStatus())){
            userInfoService.updateUserIntegral(forumArticle.getUser_id(),UserIntegralOperTypeEnum.POST_ARTICLE,UserIntegralChangeTypeEnum.ADD.getChangeType(), postIntegral);
        }
    }

    @Override
    public void updateArticle(Boolean isAdmin, ForumArticle forumArticle, ForumArticleAttachment forumArticleAttachment, MultipartFile cover, MultipartFile attachment) {
        ForumArticle dbInfo = this.baseMapper.selectById(forumArticle.getArticle_id());
        if(!isAdmin&&!dbInfo.getUser_id().equals(forumArticle.getUser_id())){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        forumArticle.setLast_update_time(new Date());
        resetBoardInfo(isAdmin,forumArticle);
        if (cover != null) {
            FileUploadDto fileUploadDto = fileUtils.uploadFile2Local(cover, Constants.FILE_FOLDER_IMAGE, FileUploadTypeEnum.ARTICLE_COVER);
            forumArticle.setCover(fileUploadDto.getLocalPath());
        }
        if (attachment != null) {
            uploadAttachment(forumArticle,forumArticleAttachment,attachment,true);
            forumArticle.setAttachment_type(ArticleAttachmentTypeEnum.HAVE_ATTACHMENT.getType());
        }
        ForumArticleAttachment dbAttachment = null;
        LambdaQueryWrapper<ForumArticleAttachment> attachmentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        attachmentLambdaQueryWrapper.eq(ForumArticleAttachment::getArticle_id,forumArticle.getArticle_id());
        List<ForumArticleAttachment> attachmentList = forumArticleAttachmentmapper.selectList(attachmentLambdaQueryWrapper);
        if(!attachmentList.isEmpty()){
            dbAttachment = attachmentList.get(0);
            new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_ATTACHMENT
                    + dbAttachment.getFile_path()).delete();
        }

        if(dbAttachment!=null){
         if(forumArticle.getAttachment_type()==Constants.ZERO){
             new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_ATTACHMENT
                    + dbAttachment.getFile_path()).delete();
             forumArticleAttachmentmapper.deleteById(dbAttachment.getFile_id());
         }else {
             //更新积分
             if(!dbAttachment.getIntegral().equals(forumArticleAttachment.getIntegral())){
                 ForumArticleAttachment integralUpdate = new ForumArticleAttachment();
                 integralUpdate.setIntegral(forumArticleAttachment.getIntegral());
                 LambdaUpdateWrapper<ForumArticleAttachment> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                 lambdaUpdateWrapper.eq(ForumArticleAttachment::getFile_id,dbAttachment.getFile_id());
                 forumArticleAttachmentmapper.update(integralUpdate,lambdaUpdateWrapper);
             }
         }
        }

        //文章是否需要审核
        if (isAdmin) {
            forumArticle.setStatus(ArticleStatusEnum.AUDIT.getStatus());
        }else {
            SysSettingAuditDto sysSettingAuditDto = SysCacheUtils.getSysSetting().getSysSettingAuditDto();
            forumArticle.setStatus(sysSettingAuditDto.getPostAudit()?ArticleStatusEnum.NO_AUDIT.getStatus():ArticleStatusEnum.AUDIT.getStatus());
        }

        //替换图片
        String content = forumArticle.getContent();
        if(!StringTools.isEmpty(content)){
            String month = imageUtils.restImageHtml(content);
            String replaceMonth = "/"+month+"/";
            content = content.replace(Constants.FILE_FOLDER_TEMP,replaceMonth);
            forumArticle.setContent(content);
            String markdownContent = forumArticle.getMarkdown_content();
            if(!StringTools.isEmpty(markdownContent)){
                markdownContent = markdownContent.replace(Constants.FILE_FOLDER_TEMP,replaceMonth);
                forumArticle.setMarkdown_content(markdownContent);
            }
        }
        LambdaUpdateWrapper<ForumArticle> forumArticleLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        forumArticleLambdaUpdateWrapper.eq(ForumArticle::getArticle_id,forumArticle.getArticle_id());
        this.baseMapper.update(forumArticle,forumArticleLambdaUpdateWrapper);

    }

    @Override
    public IPage<ForumArticleVo> findArticleByList(ForumArticleRequestVo forumArticleRequestVo) {
        ForumArticleVo forumArticleVo = CopyTools.copy(forumArticleRequestVo, ForumArticleVo.class);
       Page<ForumArticle> page =  new Page(forumArticleRequestVo.getPageNo()==null?Constants.ONE:forumArticleRequestVo.getPageNo(),
                forumArticleRequestVo.getPageSize()==null?Constants.LENGTH_10:forumArticleRequestVo.getPageSize());
        return this.baseMapper.selectArticleList(page,forumArticleVo);
    }

    @Override
    public void deleteForumArticleById(String articleIds) {
        String[] articleIdArray = articleIds.split(",");
        for (String articleId : articleIdArray) {
            this.deleteArticleSingle(articleId);
        }
    }

    @Override
    public void updateBoard(String articleId, Integer pBoardId, Integer boardId) {
        ForumArticle forumArticle = getById(articleId);
        if(forumArticle==null){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        forumArticle.setP_board_id(pBoardId);
        forumArticle.setBoard_id(boardId);
        resetBoardInfo(true,forumArticle);
        this.baseMapper.updateById(forumArticle);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteArticleSingle(String articleId){
        LambdaQueryWrapper<ForumArticle> forumArticleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        forumArticleLambdaQueryWrapper.eq(ForumArticle::getArticle_id,articleId);
        ForumArticle forumArticleInfo = this.baseMapper.selectOne(forumArticleLambdaQueryWrapper);
        if(forumArticleInfo==null||ArticleStatusEnum.DEL.getStatus().equals(forumArticleInfo.getStatus())){
            return;
        }
        LambdaUpdateWrapper<ForumArticle> forumArticleLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        forumArticleLambdaUpdateWrapper.set(ForumArticle::getStatus,ArticleStatusEnum.DEL.getStatus());
        update(forumArticleInfo,forumArticleLambdaUpdateWrapper);

        Integer integral = SysCacheUtils.getSysSetting().getSysSettingPostDto().getPostIntegral();
        if(integral>0&&ArticleStatusEnum.AUDIT.getStatus().equals(forumArticleInfo.getStatus())){
            userInfoService.updateUserIntegral(forumArticleInfo.getUser_id(),UserIntegralOperTypeEnum.DEL_ARTICLE, UserIntegralChangeTypeEnum.REDUCE.getChangeType(), integral);
        }

        UserMessage userMessage = new UserMessage();
        userMessage.setReceived_user_id(forumArticleInfo.getUser_id());
        userMessage.setMessage_type(MessageTypeEnum.SYS.getType());
        userMessage.setCreate_time(new Date());
        userMessage.setStatus(MessageStatusEnum.NO_READ.getStatus());
        userMessage.setMessage_content("您的文章【"+forumArticleInfo.getTitle()+"】已被管理员删除");
        userMessageService.save(userMessage);


    }


    public void resetBoardInfo(Boolean isAdmin,ForumArticle forumArticle){
        LambdaQueryWrapper<ForumBoard> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ForumBoard::getBoard_id,forumArticle.getP_board_id());
        ForumBoard pBoard = forumBoardService.getOne(queryWrapper);
        if (pBoard==null||pBoard.getPost_type()==Constants.ZERO&&!isAdmin){
            throw new BusinessException("一级板块不存在");
        }
        forumArticle.setP_board_name(pBoard.getBoard_name());
        if(forumArticle.getBoard_id()!=null&&forumArticle.getBoard_id()!=0){
            LambdaQueryWrapper<ForumBoard> queryboardWrapper = new LambdaQueryWrapper<>();
            queryboardWrapper.eq(ForumBoard::getBoard_id,forumArticle.getBoard_id());
            ForumBoard board = forumBoardService.getOne(queryboardWrapper);
            if(board==null || board.getPost_type()==Constants.ZERO&&!isAdmin){
                throw new BusinessException("二级板块不存在");
            }
            forumArticle.setBoard_name(board.getBoard_name());
        }else {
            forumArticle.setBoard_id(0);
            forumArticle.setBoard_name("");
        }

    }


    public void uploadAttachment(ForumArticle article,ForumArticleAttachment forumArticleAttachment,MultipartFile file,Boolean isUpdate){
        Integer allowSizeMb = SysCacheUtils.getSysSetting().getSysSettingPostDto().getAttachmentSize();
        long allowSize = allowSizeMb * Constants.FILE_SIZE_1M;
        if(file.getSize()>allowSize){
            throw new BusinessException("附件大小不能超过"+allowSizeMb+"MB");
        }
        //修改
        ForumArticleAttachment dbInfo = null;
        if(isUpdate){
            LambdaQueryWrapper<ForumArticleAttachment> attachmentLambdaQueryWrapper = new LambdaQueryWrapper<>();
            attachmentLambdaQueryWrapper.eq(ForumArticleAttachment::getArticle_id,article.getArticle_id());
            List<ForumArticleAttachment> attachmentList = forumArticleAttachmentmapper.selectList(attachmentLambdaQueryWrapper);
            if(!attachmentList.isEmpty()){
                dbInfo = attachmentList.get(0);
               new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_ATTACHMENT
                       + dbInfo.getFile_path()).delete();
            }
        }
        FileUploadDto fileUploadDto = fileUtils.uploadFile2Local(file, Constants.FILE_FOLDER_ATTACHMENT, FileUploadTypeEnum.ARTICLE_ATTACHMENT);
        if(dbInfo==null){
            forumArticleAttachment.setFile_id(StringTools.getRandomString(Constants.LENGTH_15));
            forumArticleAttachment.setArticle_id(article.getArticle_id());
            forumArticleAttachment.setFile_name(fileUploadDto.getOriginalFileName());
            forumArticleAttachment.setFile_path(fileUploadDto.getLocalPath());
            forumArticleAttachment.setFile_size(file.getSize());
            forumArticleAttachment.setDownload_count(Constants.ZERO);
            forumArticleAttachment.setFile_type(AttachmentFileTypeEnum.ZIP.getType());
            forumArticleAttachment.setUser_id(article.getUser_id());
            forumArticleAttachmentmapper.insert(forumArticleAttachment);
        }else {
            ForumArticleAttachment updateInfo = new ForumArticleAttachment();
            updateInfo.setFile_name(fileUploadDto.getOriginalFileName());
            updateInfo.setFile_size(file.getSize());
            updateInfo.setFile_path(fileUploadDto.getLocalPath());
            LambdaUpdateWrapper<ForumArticleAttachment> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(ForumArticleAttachment::getFile_id,dbInfo.getFile_id());
            forumArticleAttachmentmapper.update(updateInfo,lambdaUpdateWrapper);


        }


    }

}




