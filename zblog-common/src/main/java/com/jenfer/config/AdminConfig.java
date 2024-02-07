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
