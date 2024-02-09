package com.jenfer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jenfer.pojo.ForumComment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* @author Jenf
* @description 针对表【forum_comment(评论)】的数据库操作Service
* @createDate 2023-09-24 11:25:25
*/
@Service
public interface ForumCommentService extends IService<ForumComment> {

    public List<ForumComment> queryListByParam(Page<ForumComment> page,
                                               ForumComment forumComment,
                                               String orderBy,
                                               Boolean queryChildren,
                                               String currentId,
                                               Boolean haveLike);



    void changeTopType(String userId,Integer commentId,Integer topType);

    void postComment(ForumComment forumComment, MultipartFile image);

    Long queryCountByArticleId(String articleId);

    Long queryCountByArticleIdAndPCommentId(String articleId, Integer pCommentId);

    List<ForumComment> queryListByParamByAdmin(Page<ForumComment> page,
                                       String articleId,
                                        Boolean queryChildren);


    List<ForumComment> queryListFuzzyByAdmin(Page<ForumComment> page,
                                             String contentFuzzy,String nickNameFuzzy,
                                              Integer status,
                                               Boolean queryChildren);

    void delComment(String commentIds);

    void delCommentSingle(Integer commentId);

    void auditCommentSingle(Integer commentId);

    void auditComment(String commentIds);

}
