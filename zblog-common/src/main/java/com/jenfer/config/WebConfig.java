package com.jenfer.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
public class WebConfig extends AppConfig {

    @Value("${spring.mail.username:}")
    private  String sendUserName;

    @Value("${admin.emails:}")
    private String adminEmails;

    @Value("${inner.api.appKey}")
    private String innerApiKey;

    @Value("${inner.api.appSecret}")
    private String innerApiSecret;

    public String getInnerApiKey() {
        return innerApiKey;
    }

    public String getInnerApiSecret() {
        return innerApiSecret;
    }

    public String getAdminEmails() {
        return adminEmails;
    }

    public String getSendUserName() {
        return sendUserName;
    }
}
