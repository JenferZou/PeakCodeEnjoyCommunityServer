package com.jenfer.controller;

import com.jenfer.constants.Constants;
import com.jenfer.dto.SessionWebUserDto;
import com.jenfer.enums.ResponseCodeEnum;
import com.jenfer.exception.BusinessException;
import com.jenfer.utils.CopyTools;
import com.jenfer.vo.CommentPaginationResultVo;
import com.jenfer.vo.PaginationResultVo;
import com.jenfer.vo.ResponseVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class ABaseController {

    protected static final String STATIC_SUCCESS = "success";
    protected static final String STATUE_ERROR = "error";

    protected <T> ResponseVo<T> getSuccessResponseVo(T t) {
        ResponseVo<T> responseVo = new ResponseVo<>();
        responseVo.setStatus(STATIC_SUCCESS);
        responseVo.setCode(ResponseCodeEnum.CODE_200.getCode());
        responseVo.setInfo(ResponseCodeEnum.CODE_200.getMsg());
        responseVo.setData(t);
        return responseVo;
    }

    protected <T> ResponseVo getBusinessErroResponseVo(BusinessException e,T t){
        ResponseVo vo = new ResponseVo();
        vo.setStatus(STATUE_ERROR);
        if(e.getCode()==null){
            vo.setCode(ResponseCodeEnum.CODE_600.getCode());
        }else {
            vo.setCode(e.getCode());
        }
        vo.setInfo(e.getMessage());
        vo.setData(t);
        return vo;
    }

    protected <T> ResponseVo getServerErrorResponseVo(T t){
        ResponseVo vo =new ResponseVo();
        vo.setStatus(STATUE_ERROR);
        vo.setCode(ResponseCodeEnum.CODE_500.getCode());
        vo.setInfo(ResponseCodeEnum.CODE_500.getMsg());
        vo.setData(t);
        return vo;
    }


    protected String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        } else if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-client-IP");
        } else if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-client-IP");
        } else if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        } else if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        } else if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        } else if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    protected SessionWebUserDto getUserInfoFromSession(HttpSession session) {
        SessionWebUserDto sessionWebUserDto = (SessionWebUserDto) session.getAttribute(Constants.SESSION_KEY);
        return sessionWebUserDto;
    }

    protected <S, T> PaginationResultVo<T> convert2PaginationVo(PaginationResultVo<S> result, Class<T> clazz) {
        PaginationResultVo<T> resultVo = new PaginationResultVo<>();
        resultVo.setList(CopyTools.copyList(result.getList(), clazz));
        resultVo.setPageSize(result.getPageSize());
        resultVo.setPageNo(result.getPageNo());
        resultVo.setPageTotal(result.getPageTotal());
        resultVo.setTotalCount(result.getTotalCount());
        return resultVo;
    }

    protected <S, T> CommentPaginationResultVo<T> convert2CommentPaginationVo(CommentPaginationResultVo<S> result, Class<T> clazz) {
        CommentPaginationResultVo<T> resultVo = new CommentPaginationResultVo<>();
        resultVo.setList(CopyTools.copyList(result.getList(), clazz));
        resultVo.setPageSize(result.getPageSize());
        resultVo.setCommentCount(result.getCommentCount());
        resultVo.setPageNo(result.getPageNo());
        resultVo.setPageTotal(result.getPageTotal());
        resultVo.setTotalCount(result.getTotalCount());
        return resultVo;
    }
}
