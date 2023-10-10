package com.jenfer.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenfer.mappers.ForumArticleAttachmentMapper;
import com.jenfer.pojo.ForumArticleAttachment;
import com.jenfer.service.ForumArticleAttachmentService;
import org.springframework.stereotype.Service;

/**
* @author Jenf
* @description 针对表【forum_article_attachment(文件信息)】的数据库操作Service实现
* @createDate 2023-09-24 11:25:25
*/
@Service
public class ForumArticleAttachmentServiceImpl extends ServiceImpl
        <ForumArticleAttachmentMapper, ForumArticleAttachment>
    implements ForumArticleAttachmentService {

}




