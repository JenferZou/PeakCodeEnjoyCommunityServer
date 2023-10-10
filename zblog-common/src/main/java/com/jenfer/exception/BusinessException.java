package com.jenfer.exception;

import com.jenfer.enums.ResponseCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class BusinessException extends RuntimeException {

    private ResponseCodeEnum codeEnum;

    private Integer code;

    private String message;


    public BusinessException(String message){
        super(message);
        this.message = message;
    }

    public BusinessException(Throwable e){
        super(e);
    }
    public BusinessException(String message,Throwable e){
        super(message,e);
        this.message=message;
    }

    public BusinessException(Integer code,String message){
        super(message);
        this.code = code;
        this.message = message;
    }
    public BusinessException(ResponseCodeEnum responseCodeEnum) {
        super(responseCodeEnum.getMsg());
        this.codeEnum = responseCodeEnum;
        this.code = responseCodeEnum.getCode();
        this.message = responseCodeEnum.getMsg();
    }


    public ResponseCodeEnum getCodeEnum() {
        return codeEnum;
    }

    public void setCodeEnum(ResponseCodeEnum codeEnum) {
        this.codeEnum = codeEnum;
    }

    public Integer getCode() {
        return code;
    }


    @Override
    public String getMessage() {
        return message;
    }



}
