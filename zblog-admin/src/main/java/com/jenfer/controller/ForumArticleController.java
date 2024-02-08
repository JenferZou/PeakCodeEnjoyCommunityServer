package com.jenfer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jenfer.annotation.VerifyParam;
import com.jenfer.constants.Constants;
import com.jenfer.controller.base.ABaseController;
import com.jenfer.pojo.ForumArticle;
import com.jenfer.service.ForumArticleService;
import com.jenfer.vo.ForumArticleRequestVo;
import com.jenfer.vo.ForumArticleVo;
import com.jenfer.vo.PaginationResultVo;
import com.jenfer.vo.ResponseVo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/forum")
public class ForumArticleController extends ABaseController {

    private static final Logger logger = LoggerFactory.getLogger(ForumArticleController.class);


    @Resource
    private ForumArticleService forumArticleService;
    @RequestMapping("loadArticle")
    public ResponseVo loadArticle(ForumArticleRequestVo forumArticleRequestVo){

        IPage<ForumArticleVo> articleByList = forumArticleService.findArticleByList(forumArticleRequestVo);
        PaginationResultVo<ForumArticleVo> forumArticlePaginationResultVo = new PaginationResultVo<>();
        forumArticlePaginationResultVo.setList(articleByList.getRecords());
        forumArticlePaginationResultVo.setPageNo((int)articleByList.getCurrent());
        forumArticlePaginationResultVo.setPageSize((int) articleByList.getSize());
        forumArticlePaginationResultVo.setTotalCount((int)articleByList.getTotal());
        forumArticlePaginationResultVo.setPageTotal((int)articleByList.getPages());
        return getSuccessResponseVo(forumArticlePaginationResultVo);

    }


    @RequestMapping("/delArticle")
    public ResponseVo delArticle(@VerifyParam(required = true) String articleId){
        forumArticleService.deleteForumArticleById(articleId);
        return getSuccessResponseVo(null);
    }


    @RequestMapping("/updateBoard")
    public ResponseVo updateBoard(@VerifyParam(required = true) String articleId,
                                  @VerifyParam(required = true) Integer pBoardId,
                                  @VerifyParam(required = true) Integer boardId){

        boardId = boardId == null ? 0 : boardId;
        forumArticleService.updateBoard(articleId,pBoardId,boardId);
        return getSuccessResponseVo(null);


    }




}
