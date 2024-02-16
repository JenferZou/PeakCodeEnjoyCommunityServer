package com.jenfer;

import com.jenfer.dto.SysSettingDto;
import com.jenfer.service.SysSettingService;
import jakarta.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class InitRun implements ApplicationRunner {

    @Autowired
    private SysSettingService sysSettingService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        SysSettingDto sysSettingDto = sysSettingService.refresCache();
    }
}
