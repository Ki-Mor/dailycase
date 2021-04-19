package org.example;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * @ClassName ShiroRealmTest
 * @Description shiro自定义realm测试类
 * @Author wangchen
 * @Date 2020/9/17 2:21 下午
 **/
public class ShiroRealmTest {

    @Test
    public void ShiroRealmTest(){

        //引入自定义Realm
        ShiroRealm shiroRealm = new ShiroRealm();

        //创建DefaultSecurityManager
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setRealm(shiroRealm);

        //设置算法名称和加密次数
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        //算法名称
        matcher.setHashAlgorithmName("md5");
        //加密次数
        matcher.setHashIterations(10);
        shiroRealm.setCredentialsMatcher(matcher);

        //主体认证，得到SecurityManager实例 并绑定给SecurityUtils
        SecurityUtils.setSecurityManager(securityManager);

        //得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("wangchen","123456");
    }
}
