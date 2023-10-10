package com.jenfer.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResponseVo<T> {

    private String status;
    private Integer code;
    private String info;
    private T data;




}
