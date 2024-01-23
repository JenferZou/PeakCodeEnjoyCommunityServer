package com.jenfer.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentPaginationResultVo<T>  extends PaginationResultVo<T>{

    private Integer totalCount;
    private Integer pageSize;
    private Integer pageNo;
    private Integer pageTotal;
    //一级评论加上二级评论总数
    private Integer commentCount;
    private List<T> list = new ArrayList<T>();


}
