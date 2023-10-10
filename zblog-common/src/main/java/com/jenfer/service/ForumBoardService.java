package com.jenfer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jenfer.pojo.ForumBoard;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Jenf
* @description 针对表【forum_board(文章板块信息)】的数据库操作Service
* @createDate 2023-09-24 11:25:25
*/
@Service
public interface ForumBoardService extends IService<ForumBoard> {

    List<ForumBoard> getBoardTree();


}
