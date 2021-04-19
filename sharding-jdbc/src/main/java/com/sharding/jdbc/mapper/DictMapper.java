package com.sharding.jdbc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sharding.jdbc.model.MyDict;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName DictMapper
 * @Description
 * @Author wangchen
 * @Date 2021/4/16 1:29 下午
 **/
@Mapper
public interface DictMapper extends BaseMapper<MyDict> {
}
