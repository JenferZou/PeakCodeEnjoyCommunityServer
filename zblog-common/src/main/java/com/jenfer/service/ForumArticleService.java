package com.jenfer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jenfer.pojo.ForumArticle;
import com.jenfer.vo.ForumArticleVo;
import com.jenfer.vo.PaginationResultVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

/**
* @author Jenf
* @description 针对表【forum_article(文章信息)】的数据库操作Service
* @createDate 2023-09-24 11:25:25
*/
@Service
public interface ForumArticleService extends IService<ForumArticle> {
    IPage<ForumArticleVo> queryArticlesWithPagination(Page<ForumArticle> page,  Integer boardId,  Integer pBoardId, String orderSql);

    ForumArticle readArticle(String articleId);

}