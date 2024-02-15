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
    public ResponseVo saveBoard(Integer boardId, @VerifyParam(required = true)Integer pBoardId,
                                @VerifyParam(required = true) String boardName, String boardDesc,
                                Integer postType, MultipartFile cover){
        ForumBoard forumBoard = new ForumBoard();
        forumBoard.setBoard_id(boardId);
        forumBoard.setP_board_id(pBoardId);
        forumBoard.setBoard_name(boardName);
        forumBoard.setBoard_desc(boardDesc);
        forumBoard.setPost_type(postType);
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
