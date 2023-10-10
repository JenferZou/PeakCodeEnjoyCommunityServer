package com.jenfer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenfer.mappers.ForumArticleMapper;
import com.jenfer.pojo.ForumArticle;
import com.jenfer.service.ForumArticleService;
import org.springframework.stereotype.Service;

/**
* @author Jenf
* @description 针对表【forum_article(文章信息)】的数据库操作Service实现
* @createDate 2023-09-24 11:25:25
*/
@Service
public class ForumArticleServiceImpl extends ServiceImpl<ForumArticleMapper, ForumArticle>
    implements ForumArticleService {

}




