package com.jenfer.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jenfer.pojo.LikeRecord;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Jenf
* @description 针对表【like_record(点赞记录)】的数据库操作Mapper
* @createDate 2023-09-24 11:25:25
* @Entity generator.domain.LikeRecord
*/
@Mapper
public interface LikeRecordMapper extends BaseMapper<LikeRecord> {

}




