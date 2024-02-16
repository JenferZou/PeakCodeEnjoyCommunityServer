package com.jenfer.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenfer.constants.Constants;
import com.jenfer.exception.BusinessException;
import com.jenfer.mappers.ForumArticleMapper;
import com.jenfer.mappers.ForumBoardMapper;
import com.jenfer.pojo.ForumArticle;
import com.jenfer.pojo.ForumBoard;
import com.jenfer.service.ForumArticleService;
import com.jenfer.service.ForumBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            forumBoards = baseMapper.selectList(new LambdaQueryWrapper<ForumBoard>()
                    .eq(ForumBoard::getPost_type,postType).orderByAsc(ForumBoard::getSort));
        }else {
            forumBoards = baseMapper.selectList(new LambdaQueryWrapper<ForumBoard>().orderByAsc(ForumBoard::getSort));
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
            System.out.println("boardId:=======>"+forumBoard.getBoard_id());
            this.baseMapper.insert(forumBoard);
        }else {
            //修改
            ForumBoard dbInfo = this.baseMapper.selectById(forumBoard.getBoard_id());
            if(dbInfo==null){
                throw new BusinessException("板块信息不存在");
            }
            this.baseMapper.updateById(forumBoard);
            if(!dbInfo.getBoard_name().equals(forumBoard.getBoard_name())){
                LambdaUpdateWrapper<ForumArticle> forumBoardLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                if(dbInfo.getP_board_id()==0){
                    forumBoardLambdaUpdateWrapper.eq(ForumArticle::getP_board_id, Constants.ZERO).
                            set(ForumArticle::getP_board_name,forumBoard.getBoard_name());
                }
                if(dbInfo.getP_board_id()!=0){
                    forumBoardLambdaUpdateWrapper.ne(ForumArticle::getP_board_id, Constants.ZERO).
                            set(ForumArticle::getBoard_name,forumBoard.getBoard_name());
                }
                forumArticleService.update(null, forumBoardLambdaUpdateWrapper);
            }


        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeSort(String boardIds) {
        String[] boardIdArray = boardIds.split(",");
        Integer index  = 1;
        for(String boardIdStr : boardIdArray){
            int boardId = Integer.parseInt(boardIdStr);
            LambdaUpdateWrapper<ForumBoard> forumBoardLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            forumBoardLambdaUpdateWrapper.set(ForumBoard::getSort, index).eq(ForumBoard::getBoard_id,boardId);
            this.baseMapper.update(null,forumBoardLambdaUpdateWrapper);
            index++;
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




