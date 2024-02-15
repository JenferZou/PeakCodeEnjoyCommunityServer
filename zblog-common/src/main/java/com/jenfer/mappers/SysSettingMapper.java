package com.jenfer.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jenfer.pojo.SysSetting;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Jenf
* @description 针对表【sys_setting(系统设置信息)】的数据库操作Mapper
* @createDate 2023-09-24 11:25:25
* @Entity generator.domain.SysSetting
*/
@Mapper
public interface SysSettingMapper extends BaseMapper<SysSetting> {

    void insertOrUpdate(SysSetting sysSetting);
}




