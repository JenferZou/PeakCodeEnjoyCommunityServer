package com.jenfer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {

    @Value("${project.folder}")
    private String projectFolder;

    @Value("${isDev}")
    private Boolean isDev;

    @Value("${inner.api.appKey}")
    private String innerApiKey;

    @Value("${inner.api.appSecret}")
    private String innerApiSecret;

    public Boolean getDev() {
        return isDev;
    }

    public String getInnerApiKey() {
        return innerApiKey;
    }

    public String getInnerApiSecret() {
        return innerApiSecret;
    }

    public Boolean getIsDev() {
        return isDev;
    }


    public String getProjectFolder() {
        return projectFolder;
    }
}
