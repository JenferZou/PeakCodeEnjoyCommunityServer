package com.jenfer.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenfer.mappers.ForumBoardMapper;
import com.jenfer.pojo.ForumBoard;
import com.jenfer.service.ForumBoardService;
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

    @Override
    public List<ForumBoard> getBoardTree() {
//        List<ForumBoard> forumBoards = baseMapper.selectList(new LambdaQueryWrapper<ForumBoard>().eq(ForumBoard::getPost_type,1));
        List<ForumBoard> forumBoards = baseMapper.selectList(null);
        return convertLine2Tree(forumBoards,0);
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




