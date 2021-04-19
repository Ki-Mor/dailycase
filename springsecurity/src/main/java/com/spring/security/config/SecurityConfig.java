package com.spring.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @ClassName SecurityConfig
 * @Description
 * @Author wangchen
 * @Date 2021/4/16 2:32 下午
 **/
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.inMemoryAuthentication().withUser("admin").password("123456").authorities("addUser");
        auth.inMemoryAuthentication().withUser("show_user").password("123456").authorities("/");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //以下五步是表单登录进行身份认证最简单的登陆环境
        //表单登陆 1
        /*http.formLogin()
                //指定登陆页面
                .loginPage("/login.html")
                ////登陆页面提交的页面 开始使用UsernamePasswordAuthenticationFilter过滤器处理请求
                .loginProcessingUrl("/authentication/form")
                .and() //2
                .authorizeRequests() //下面的都是授权的配置 3
                //访问此地址就不需要进行身份认证了，防止重定向死循环
                .antMatchers("/login.html").permitAll()
                .anyRequest() //任何请求 4
                .authenticated(); //访问任何资源都需要身份认证 5*/
        http.authorizeRequests().antMatchers("/addUser").hasAnyAuthority("addUser")
                .antMatchers("/delUser").hasAnyAuthority("delUser")
                .antMatchers("/updateUser").hasAnyAuthority("updateUser")
                .antMatchers("/queryUser").hasAnyAuthority("queryUser")
                .antMatchers("/**").fullyAuthenticated().and().formLogin();
    }
}
