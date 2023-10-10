package com.jenfer.controller;

import com.jenfer.service.ForumBoardService;
import com.jenfer.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/board")
@RestController
public class ForumBoardController extends ABaseController {

    @Autowired
    private ForumBoardService forumBoardService;

    @RequestMapping("/loadBoard")
    public ResponseVo loadBoard(){
        return getSuccessResponseVo(forumBoardService.getBoardTree());
    }



}
