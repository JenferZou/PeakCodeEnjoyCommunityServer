package com.jenfer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jenfer.pojo.ForumComment;
import org.springframework.stereotype.Service;

/**
* @author Jenf
* @description 针对表【forum_comment(评论)】的数据库操作Service
* @createDate 2023-09-24 11:25:25
*/
@Service
public interface ForumCommentService extends IService<ForumComment> {

}
