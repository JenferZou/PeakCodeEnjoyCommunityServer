package com.jenfer.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jenfer.enums.ArticleorderTypeEnum;
import com.jenfer.enums.PageSize;
import com.jenfer.pojo.ForumArticle;
import com.jenfer.service.ForumArticleService;
import com.jenfer.vo.ForumArticleVo;
import com.jenfer.vo.PaginationResultVo;
import com.jenfer.vo.ResponseVo;
import jakarta.servlet.http.HttpSession;
import org.assertj.core.api.IntArrayAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/forum")
public class ForumArticleController extends ABaseController {

    @Autowired
    private ForumArticleService forumArticleService;

    @RequestMapping("/loadArticle")
    public ResponseVo loadArticle(HttpSession session,Integer boardId,Integer pBoardId,Integer orderType,
                                  Integer pageNo){
        //分页需要传递的参数Page
        Page<ForumArticle> articlePage = new Page<>(pageNo==null?1:pageNo, PageSize.SIZE10.getSize());
        //分页需要传递的参数board_id
        Integer board_id =boardId==null||boardId==0?null:boardId;
        //分页需要传递的参数通过orderType来查找到对应的sql语句也就是对应的排序规则
        ArticleorderTypeEnum articleorderTypeEnum = ArticleorderTypeEnum.getByType(orderType);
        articleorderTypeEnum=articleorderTypeEnum==null?ArticleorderTypeEnum.HOT:articleorderTypeEnum;

        IPage<ForumArticleVo> forumArticleVoIPage = forumArticleService.queryArticlesWithPagination(articlePage, board_id, pBoardId, articleorderTypeEnum.getOrderSql());
        List<ForumArticleVo> records = forumArticleVoIPage.getRecords();


        PaginationResultVo<ForumArticleVo> forumArticlePaginationResultVo = new PaginationResultVo<>();
        forumArticlePaginationResultVo.setList(records);
        forumArticlePaginationResultVo.setPageNo(pageNo);
        forumArticlePaginationResultVo.setPageSize((int) forumArticleVoIPage.getSize());
        forumArticlePaginationResultVo.setTotalCount((int)forumArticleVoIPage.getTotal());
        forumArticlePaginationResultVo.setPageTotal((int)forumArticleVoIPage.getPages());
        return getSuccessResponseVo(convert2PaginationVo(forumArticlePaginationResultVo, ForumArticleVo.class));


    }

}
