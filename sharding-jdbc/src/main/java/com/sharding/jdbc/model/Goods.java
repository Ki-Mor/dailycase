package com.sharding.jdbc.model;

import lombok.Data;

/**
 * @ClassName Goods
 * @Description 商品实体类
 * @Author wangchen
 * @Date 2021/4/15 5:54 下午
 **/
@Data
public class Goods {

    private Long gid;
    private String gname;
    private Long userId;
    private String gstatus;
}
