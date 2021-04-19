package com.sharding.jdbc.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @ClassName MyDict
 * @Description
 * @Author wangchen
 * @Date 2021/4/16 1:28 下午
 **/
@Data
@TableName("t_dict")
public class MyDict {

    private Long dictId;
    private String dictName;
    private String dictCode;
}
