package com.jenfer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenfer.mappers.ForumCommentMapper;
import com.jenfer.pojo.ForumComment;
import com.jenfer.service.ForumCommentService;
import org.springframework.stereotype.Service;

/**
* @author Jenf
* @description 针对表【forum_comment(评论)】的数据库操作Service实现
* @createDate 2023-09-24 11:25:25
*/
@Service
public class ForumCommentServiceImpl extends ServiceImpl<ForumCommentMapper, ForumComment>
    implements ForumCommentService {

}




