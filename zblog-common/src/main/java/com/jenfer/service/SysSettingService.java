package com.jenfer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jenfer.pojo.SysSetting;
import org.springframework.stereotype.Service;

/**
* @author Jenf
* @description 针对表【sys_setting(系统设置信息)】的数据库操作Service
* @createDate 2023-09-24 11:25:25
*/
@Service
public interface SysSettingService extends IService<SysSetting> {

    void refresCache();

}
