package com.jenfer.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jenfer.pojo.ForumComment;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Jenf
* @description 针对表【forum_comment(评论)】的数据库操作Mapper
* @createDate 2023-09-24 11:25:25
* @Entity generator.domain.ForumComment
*/
@Mapper
public interface ForumCommentMapper extends BaseMapper<ForumComment> {

}




