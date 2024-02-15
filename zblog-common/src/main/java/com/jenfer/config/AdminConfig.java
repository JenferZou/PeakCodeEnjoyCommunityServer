package com.jenfer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AdminConfig extends AppConfig{


    @Value("${admin.account}")
    private String adminAccount;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${web.api.url}")
    private String webApiUrl;


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

    public String getAdminAccount() {
        return adminAccount;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public String getWebApiUrl() {
        return webApiUrl;
    }
}
