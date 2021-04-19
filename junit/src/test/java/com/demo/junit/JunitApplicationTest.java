package com.demo.junit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @ClassName JunitApplicationTest
 * @Description
 * @Author wangchen
 * @Date 2020/11/26 4:41 下午
 **/
class JunitApplicationTest {

    @Test
    public void cal(){
        assertEquals(4,new JunitApplication.Calculator().calculate("1+2"));
    }

}