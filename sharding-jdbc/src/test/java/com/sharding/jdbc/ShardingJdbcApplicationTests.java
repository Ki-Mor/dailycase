package com.sharding.jdbc;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sharding.jdbc.mapper.DictMapper;
import com.sharding.jdbc.mapper.GoodsMapper;
import com.sharding.jdbc.mapper.UserMapper;
import com.sharding.jdbc.model.Goods;
import com.sharding.jdbc.model.MyDict;
import com.sharding.jdbc.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShardingJdbcApplicationTests {

    @Autowired
    GoodsMapper goodsMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    DictMapper dictMapper;

    @Test
    void addGoods() {
        for (int i = 0; i < 20; i++) {
            Goods good = new Goods();
            good.setGname("小米手机"+i);
            good.setUserId(100L);
            good.setGstatus("已发布");
            goodsMapper.insert(good);
        }
    }

    @Test
    void getGood(){
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        //queryWrapper.eq(Goods::getGid,589769387431428097L);
        queryWrapper.eq(Goods::getGname,"小米手机1");
        Goods good = goodsMapper.selectOne(queryWrapper);
        System.out.println(good.toString());
    }

    @Test
    void addGoods02(){
        for (int i = 0; i < 20; i++) {
            Goods good = new Goods();
            good.setGname("华为手机");
            good.setUserId(Long.valueOf(i));
            good.setGstatus("已发布");
            goodsMapper.insert(good);
        }
    }


    @Test
    void addUser(){
        User user = new User();
        user.setUserName("李白");
        user.setUserStatus("0");
        userMapper.insert(user);
    }

    @Test
    void getUser(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //此处请填写自己程序生成的ID
        queryWrapper.eq("user_id",0);
        User good = userMapper.selectOne(queryWrapper);
        System.out.println(good.toString());
    }

    /**
     * 下面是公共表测试方法
     */
    @Test
    void addDict(){
        MyDict myDict = new MyDict();
        myDict.setDictName("已启用");
        myDict.setDictCode("1");
        dictMapper.insert(myDict);
    }

    @Test
    void deleteDict(){
        QueryWrapper<MyDict> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_id","");
        dictMapper.delete(wrapper);
    }

}
