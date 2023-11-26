package com.jenfer.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jenfer.dto.SysSettingDto;
import com.jenfer.enums.SysSettingCodeEnum;
import com.jenfer.mappers.SysSettingMapper;
import com.jenfer.pojo.SysSetting;
import com.jenfer.service.SysSettingService;
import com.jenfer.utils.JsonUtils;
import com.jenfer.utils.StringTools;
import com.jenfer.utils.SysCacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;

/**
* @author Jenf
* @description 针对表【sys_setting(系统设置信息)】的数据库操作Service实现
* @createDate 2023-09-24 11:25:25
*/
@Service
public class SysSettingServiceImpl extends ServiceImpl<SysSettingMapper, SysSetting>
    implements SysSettingService {
    private static final Logger logger = LoggerFactory.getLogger(SysSettingServiceImpl.class);

    @Override
    public void refresCache() {
        try{
            SysSettingDto sysSettingDto = new SysSettingDto();
            List<SysSetting> list = baseMapper.selectList(null);
            for(SysSetting sysSetting:list){
                String jsonContent = sysSetting.getJson_content();
                if(StringTools.isEmpty(jsonContent)){
                    continue;
                }
                String code = sysSetting.getCode();
                SysSettingCodeEnum sysSettingCodeEnum = SysSettingCodeEnum.getByCode(code);
                PropertyDescriptor pd = new PropertyDescriptor(sysSettingCodeEnum.getPropName(),SysSettingDto.class);
                Method writeMethod = pd.getWriteMethod();
                Class subClazz = Class.forName(sysSettingCodeEnum.getClazz());
                writeMethod.invoke(sysSettingDto, JsonUtils.convertJson2Obj(jsonContent,subClazz));


            }
            SysCacheUtils.refresh(sysSettingDto);

        }catch (Exception  e){
            logger.error("刷新缓存失败");
        }
    }
}




