package com.jenfer;

import com.jenfer.service.SysSettingService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class InitRun implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(InitRun.class);
    @Resource
    private SysSettingService sysSettingService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        sysSettingService.refresCache();
        logger.info("服务初始化完成");
    }
}
