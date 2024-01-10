package com.jenfer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jenfer.pojo.ForumArticle;
import com.jenfer.pojo.ForumArticleAttachment;
import com.jenfer.vo.ForumArticleVo;
import com.jenfer.vo.PaginationResultVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
* @author Jenf
* @description 针对表【forum_article(文章信息)】的数据库操作Service
* @createDate 2023-09-24 11:25:25
*/
@Service
public interface ForumArticleService extends IService<ForumArticle> {
    IPage<ForumArticleVo> queryArticlesWithPagination(Page<ForumArticle> page,  Integer boardId,  Integer pBoardId, String orderSql);

    ForumArticle readArticle(String articleId);

    void postArticle(Boolean isAdmin,ForumArticle forumArticle, ForumArticleAttachment forumArticleAttachment, MultipartFile cover,MultipartFile attachment);

    void updateArticle(Boolean isAdmin,ForumArticle forumArticle, ForumArticleAttachment forumArticleAttachment, MultipartFile cover,MultipartFile attachment);


    IPage<ForumArticleVo> queryArticleBySort(Page<ForumArticle> page,ForumArticle forumArticle);



}