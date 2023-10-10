package com.jenfer.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jenfer.pojo.ForumArticle;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Jenf
* @description 针对表【forum_article(文章信息)】的数据库操作Mapper
* @createDate 2023-09-24 11:25:25
* @Entity generator.domain.ForumArticle
*/
@Mapper
public interface ForumArticleMapper extends BaseMapper<ForumArticle> {

}




