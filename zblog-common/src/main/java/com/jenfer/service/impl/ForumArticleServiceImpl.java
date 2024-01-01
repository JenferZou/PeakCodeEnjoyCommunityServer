package com.jenfer.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenfer.constants.Constants;
import com.jenfer.enums.ArticleStatusEnum;
import com.jenfer.enums.ResponseCodeEnum;
import com.jenfer.enums.UpdateArticleTypeEnum;
import com.jenfer.exception.BusinessException;
import com.jenfer.mappers.ForumArticleMapper;
import com.jenfer.pojo.ForumArticle;
import com.jenfer.service.ForumArticleService;
import com.jenfer.vo.ForumArticleVo;
import org.springframework.stereotype.Service;

/**
* @author Jenf
* @description 针对表【forum_article(文章信息)】的数据库操作Service实现
* @createDate 2023-09-24 11:25:25
*/
@Service
public class ForumArticleServiceImpl extends ServiceImpl<ForumArticleMapper, ForumArticle>
    implements ForumArticleService {

    @Override
    public IPage<ForumArticleVo> queryArticlesWithPagination(Page<ForumArticle> page, Integer boardId, Integer pBoardId, String orderSql) {
        return baseMapper.queryArticlesWithPagination(page, boardId, pBoardId, orderSql);

    }

    @Override
    public ForumArticle readArticle(String articleId) {
        ForumArticle forumArticle = baseMapper.selectById(articleId);

        if(forumArticle==null){
            throw new BusinessException(ResponseCodeEnum.CODE_404);
        }
        if(ArticleStatusEnum.AUDIT.getStatus().equals(forumArticle.getStatus())){
            baseMapper.updateArticleCount(UpdateArticleTypeEnum.READ_COUNT.getType(), Constants.ONE,articleId);
        }

        return forumArticle;
    }


}




