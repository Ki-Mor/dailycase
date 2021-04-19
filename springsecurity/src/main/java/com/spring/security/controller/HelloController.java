package com.spring.security.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName HelloController
 * @Description
 * @Author wangchen
 * @Date 2021/4/16 2:26 下午
 **/
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String hello() {
        return "Hello SpringSecurity";
    }

    @RequestMapping("/addUser")
    public String addUser() {
        return "addUser";
    }

    @RequestMapping("/delUser")
    public String delUser() {
        return "addUser";
    }

    @RequestMapping("/updateUser")
    public String updateUser() {
        return "addUser";
    }

    @RequestMapping("/queryUser")
    public String queryUser() {
        return "addUser";
    }
}
