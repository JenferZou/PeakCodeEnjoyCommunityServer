package com.jenfer.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jenfer.pojo.ForumArticle;
import com.jenfer.vo.ForumArticleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author Jenf
* @description 针对表【forum_article(文章信息)】的数据库操作Mapper
* @createDate 2023-09-24 11:25:25
* @Entity generator.domain.ForumArticle
*/
@Mapper
public interface ForumArticleMapper extends BaseMapper<ForumArticle> {

    IPage<ForumArticleVo> queryArticlesWithPagination(Page<ForumArticle> page, @Param("boardId") Integer boardId, @Param("pBoardId") Integer pBoardId, @Param("orderSql") String orderSql);

    void updateArticleCount(@Param("updateType")Integer updateType,@Param("changeCount")Integer changeCount,
                            @Param("articleId")String articleId);

}




