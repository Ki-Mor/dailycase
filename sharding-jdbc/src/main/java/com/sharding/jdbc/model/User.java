package com.sharding.jdbc.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @ClassName User
 * @Description
 * @Author wangchen
 * @Date 2021/4/16 1:13 下午
 **/
@Data
@TableName("t_user")
public class User {

    private Long userId;
    private String userName;
    private String userStatus;
}
