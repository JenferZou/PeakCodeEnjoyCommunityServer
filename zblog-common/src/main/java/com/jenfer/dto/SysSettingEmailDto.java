package com.jenfer.dto;

/**
 * 发送邮件设置
 */
public class SysSettingEmailDto {
    private String emailTitle;

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
