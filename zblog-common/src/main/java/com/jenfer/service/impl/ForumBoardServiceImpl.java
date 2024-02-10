package com.jenfer.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenfer.exception.BusinessException;
import com.jenfer.mappers.ForumArticleMapper;
import com.jenfer.mappers.ForumBoardMapper;
import com.jenfer.pojo.ForumBoard;
import com.jenfer.service.ForumArticleService;
import com.jenfer.service.ForumBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author Jenf
* @description 针对表【forum_board(文章板块信息)】的数据库操作Service实现
* @createDate 2023-09-24 11:25:25
*/
@Service
public class ForumBoardServiceImpl extends ServiceImpl<ForumBoardMapper, ForumBoard>
    implements ForumBoardService {

    @Autowired
    private ForumArticleService forumArticleService;

    @Override
    public List<ForumBoard> getBoardTree(Integer postType) {
        List<ForumBoard> forumBoards = null;
        if(postType!=null){
            forumBoards = baseMapper.selectList(new LambdaQueryWrapper<ForumBoard>().eq(ForumBoard::getPost_type,postType));
        }else {
            forumBoards = baseMapper.selectList(null);
        }
        return convertLine2Tree(forumBoards,0);
    }

    @Override
    public void saveForumBoard(ForumBoard forumBoard) {
        if(forumBoard.getBoard_id()==null){
            //添加
            LambdaUpdateWrapper<ForumBoard> forumBoardLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            forumBoardLambdaUpdateWrapper.set(ForumBoard::getP_board_id,forumBoard.getP_board_id());
            Long countbyLong = this.baseMapper.selectCount(forumBoardLambdaUpdateWrapper);
            int count = countbyLong.intValue();
            forumBoard.setSort(count+1);
            this.baseMapper.insert(forumBoard);
        }else {
            //修改
            ForumBoard dbInfo = this.baseMapper.selectById(forumBoard.getBoard_id());
            if(dbInfo==null){
                throw new BusinessException("板块信息不存在");
            }
            this.baseMapper.updateById(forumBoard);

        }
    }

    private List<ForumBoard> convertLine2Tree(List<ForumBoard> dataList,Integer pid){
        List<ForumBoard> children = new ArrayList<>();
        for (ForumBoard m:dataList){
            if(m.getP_board_id().equals(pid)){
                m.setChildren(convertLine2Tree(dataList,m.getBoard_id()));
                children.add(m);
            }
        }
        return children;
    }


}




