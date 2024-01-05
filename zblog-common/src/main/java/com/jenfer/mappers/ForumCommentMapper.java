package com.jenfer.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jenfer.pojo.ForumComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Jenf
* @description 针对表【forum_comment(评论)】的数据库操作Mapper
* @createDate 2023-09-24 11:25:25
* @Entity generator.domain.ForumComment
*/
@Mapper
public interface ForumCommentMapper extends BaseMapper<ForumComment> {
    public IPage<ForumComment> querySubCommentbyParam(Page<ForumComment> page,
                                                   ForumComment forumComment,
                                                   @Param("orderBy")String orderBy,
                                                   @Param("current_id") String currentId,
                                                   @Param("haveLike") Boolean likeType,
                                                      @Param("pid_list")List<Integer> pidList

    );


    public IPage<ForumComment> queryCommentbyParam(Page<ForumComment> page,
                                                   ForumComment forumComment,
                                                   @Param("orderBy")String orderBy,
                                                   @Param("current_id") String currentId,
                                                   @Param("haveLike") Boolean likeType
                                                   );



    void updateCommentGoodCount(@Param("changeCount") Integer changeCount,@Param("commentId") Integer commentId);


}




