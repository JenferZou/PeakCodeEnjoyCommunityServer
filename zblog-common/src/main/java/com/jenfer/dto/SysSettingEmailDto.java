package com.jenfer.dto;

import com.jenfer.annotation.VerifyParam;

/**
 * 发送邮件设置
 */
public class SysSettingEmailDto {
    @VerifyParam(required = true)
    private String emailTitle;

    @VerifyParam(required = true)
    private String emailContent;

    public String getEmailTitle() {
        return emailTitle;
    }

    public void setEmailTitle(String emailTitle) {
        this.emailTitle = emailTitle;
    }

    public String getEmailContent() {
        return emailContent;
    }

    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
    }
}
