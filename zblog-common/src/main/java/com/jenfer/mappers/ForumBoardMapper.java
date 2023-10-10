package com.jenfer.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jenfer.pojo.ForumBoard;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author Jenf
* @description 针对表【forum_board(文章板块信息)】的数据库操作Mapper
* @createDate 2023-09-24 11:25:25
* @Entity generator.domain.ForumBoard
*/
@Mapper
public interface ForumBoardMapper extends BaseMapper<ForumBoard> {

    List<ForumBoard> getAllBoard();

}




