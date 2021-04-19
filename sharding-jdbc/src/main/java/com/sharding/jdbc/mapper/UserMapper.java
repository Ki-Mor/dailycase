package com.sharding.jdbc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sharding.jdbc.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName UserMapper
 * @Description
 * @Author wangchen
 * @Date 2021/4/16 1:14 下午
 **/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
