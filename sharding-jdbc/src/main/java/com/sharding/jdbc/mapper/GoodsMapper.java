package com.sharding.jdbc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sharding.jdbc.model.Goods;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName GoodsMapper
 * @Description 商品mapper
 * @Author wangchen
 * @Date 2021/4/15 5:56 下午
 **/
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {


}
