package com.jenfer.controller;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jenfer.pojo.ForumArticle;
import com.jenfer.service.ForumArticleService;
import com.jenfer.vo.ForumArticleVo;
import com.jenfer.vo.PaginationResultVo;
import com.jenfer.vo.ResponseVo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/forum")
public class ForumArticleController extends ABaseController {

    @Autowired
    private ForumArticleService forumArticleService;

    @RequestMapping("/loadArticle")
    public ResponseVo loadArticle(HttpSession session,Integer boardId,Integer pBoardId,Integer orderType,
                                  Integer pageNo){
//        ForumArticle forumArticle = new ForumArticle();
//        forumArticle.setBoard_id(boardId==null||boardId==0?null:boardId);
//        forumArticle.setP_board_id(pBoardId);

        Page<ForumArticle> ArticlePage = new Page<>(1, 10);
        Page<ForumArticle> page = new LambdaQueryChainWrapper<>(forumArticleService.getBaseMapper()).eq(ForumArticle::getBoard_id, boardId == null || boardId == 0 ? null : boardId).eq(ForumArticle::getP_board_id, pBoardId)
                .page(ArticlePage);
        List<ForumArticle> records = page.getRecords();

        List<ForumArticle> list = forumArticleService.list();
        PaginationResultVo<ForumArticle> forumArticlePaginationResultVo = new PaginationResultVo<>();
        forumArticlePaginationResultVo.setList(list);
        forumArticlePaginationResultVo.setPageNo(1);
        forumArticlePaginationResultVo.setPageSize(10);
        return getSuccessResponseVo(convert2PaginationVo(forumArticlePaginationResultVo, ForumArticleVo.class));


    }

}
