package com.jenfer.controller;

import com.jenfer.annotation.GloballInterceptor;
import com.jenfer.annotation.VerifyParam;
import com.jenfer.constants.Constants;
import com.jenfer.controller.base.ABaseController;
import com.jenfer.dto.FileUploadDto;
import com.jenfer.enums.FileUploadTypeEnum;
import com.jenfer.pojo.ForumBoard;
import com.jenfer.pojo.ForumComment;
import com.jenfer.service.ForumBoardService;
import com.jenfer.utils.FileUtils;
import com.jenfer.vo.ResponseVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/board")
public class ForumBoardController extends ABaseController {

    @Resource
    private ForumBoardService forumBoardService;

    @Resource
    private FileUtils fileUtils;

    @RequestMapping("/loadBoard")
    public ResponseVo loadBoard(){
        return getSuccessResponseVo(forumBoardService.getBoardTree(null));
    }


    @RequestMapping("/saveBoard")
    public ResponseVo saveBoard(Integer board_id, @VerifyParam(required = true)Integer p_board_id,
                                @VerifyParam(required = true) String board_name, String board_desc,
                                Integer post_type, MultipartFile cover){
        ForumBoard forumBoard = new ForumBoard();
        forumBoard.setBoard_id(board_id);
        forumBoard.setP_board_id(p_board_id);
        forumBoard.setBoard_name(board_name);
        forumBoard.setBoard_desc(board_desc);
        forumBoard.setPost_type(post_type);
        if(cover!=null){
           FileUploadDto fileUploadDto =  fileUtils.uploadFile2Local(cover,Constants.FILE_FOLDER_IMAGE,FileUploadTypeEnum.ARTICLE_COVER);
            forumBoard.setCover(fileUploadDto.getLocalPath());
        }
        forumBoardService.saveForumBoard(forumBoard);
        return getSuccessResponseVo(null);
    }



    @RequestMapping("/delBoard")
    @GloballInterceptor(checkParams = true)
    public ResponseVo delBoard(@VerifyParam(required = true)Integer boardId){
        forumBoardService.removeById(boardId);
        return getSuccessResponseVo(null);
    }


    @RequestMapping("/changeBoardSort")
    @GloballInterceptor(checkParams = true)
    public ResponseVo changeSort(@VerifyParam(required = true)String boardIds){
        forumBoardService.changeSort(boardIds);
        return getSuccessResponseVo(null);
    }



}
