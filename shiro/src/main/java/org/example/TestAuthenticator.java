package org.example;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;

import java.util.Arrays;

/**
 * @ClassName TestAuthenticator
 * @Description 用户认证
 * @Author wangchen
 * @Date 2020/9/6 4:56 下午
 **/
public class TestAuthenticator {

    public static void main(String[] args) {

        //1.创建安全管理器对象
        DefaultSecurityManager securityManager = new DefaultSecurityManager();

        //2.给安全管理器设置realm
        //securityManager.setRealm(new IniRealm("classpath:shiro.ini"));

        ShiroRealm shiroRealm = new ShiroRealm();
        // 凭证匹配器
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        // 设置加密算法
        credentialsMatcher.setHashAlgorithmName("md5");
        // 设置hash次数
        credentialsMatcher.setHashIterations(1024);
        // 为realm设置凭证匹配器
        shiroRealm.setCredentialsMatcher(credentialsMatcher);
        securityManager.setRealm(shiroRealm);

        //3.SecurityUtils 给全局安全工具类设置安全管理器
        SecurityUtils.setSecurityManager(securityManager);

        //4.关键对象 subject 主体
        Subject subject = SecurityUtils.getSubject();

        //5.创建令牌
        UsernamePasswordToken token = new UsernamePasswordToken("wangchen","123456");

        try {
            System.out.println("认证状态:"+subject.isAuthenticated());
            //用户认证
            subject.login(token);
            System.out.println("认证状态:"+subject.isAuthenticated());
        }catch (UnknownAccountException e){
            e.printStackTrace();
            System.out.println("认证失败:用户名不存在～");
        }catch (IncorrectCredentialsException e){
            e.printStackTrace();
            System.out.println("认证失败:密码错误～");
        }

        System.out.println("==========================授权============================");

        if (subject.isAuthenticated()){
            //基于角色权限控制
            System.out.println(subject.hasRole("admin"));
            //基于角色权限控制
            System.out.println(subject.hasRole("super"));

            //基于多角色权限控制(同时具有)
            System.out.println(subject.hasAllRoles(Arrays.asList("admin", "super")));
            System.out.println(subject.hasAllRoles(Arrays.asList("admin", "user")));

            //是否具有其中一个角色
            boolean[] booleans = subject.hasRoles(Arrays.asList("admin", "super", "user"));
            for (boolean aBoolean : booleans) {
                System.out.println(aBoolean);
            }


            System.out.println("====================访问控制==========================");
            //基于权限字符串的访问控制  资源标识符:操作:资源类型
            System.out.println("权限:"+subject.isPermitted("user:add:01"));
            System.out.println("权限:"+subject.isPermitted("product:create:02"));

            //分别具有那些权限
            boolean[] permitted = subject.isPermitted("user:add:01", "user:add:02");
            for (boolean b : permitted) {
                System.out.println(b);
            }

            //同时具有哪些权限
            boolean permittedAll = subject.isPermittedAll("user:add:01", "user:add:03");
            System.out.println(permittedAll);

        }

    }
}
