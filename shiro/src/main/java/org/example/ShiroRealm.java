package org.example;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName ShiroRealm
 * @Description 自定义realm
 * @Author wangchen
 * @Date 2020/9/17 2:01 下午
 **/
public class ShiroRealm extends AuthorizingRealm {

    /**
     * doGetAuthenticationInfo获取身份验证相关信息：首先根据传入的用户名获取User信息；
     * 然后如果user为空，那么抛出没找到帐号异常UnknownAccountException；如果user找到但锁定了抛出锁定异常LockedAccountException；
     * 最后生成AuthenticationInfo信息，交给间接父类AuthenticatingRealm使用CredentialsMatcher进行判断密码是否匹配，如果不匹配将抛出密码错误异常IncorrectCredentialsException；
     * 另外如果密码重试此处太多将抛出超出重试次数异常ExcessiveAttemptsException；
     * 在组装SimpleAuthenticationInfo信息时，需要传入：身份信息（用户名）、凭据（密文密码）、盐（username+salt），CredentialsMatcher使用盐加密传入的明文密码和此处的密文密码进行匹配。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1、从主体传过来的认证信息中，获取用户名(getPrincipal();身份 | getCredentials();凭据)
        String userName=(String)authenticationToken.getPrincipal();

        //2、通过用户名从数据库中获取密码凭证
        String password=getPasswordByUsername(userName);
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName,password,this.getName());
        //将盐注册到信息中去
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("salt"));
        return authenticationInfo;
    }

    /**
     * 方便测试 设置用户数
     */
    Map<String,String> userMap=new HashMap<String, String>(16);
    {
        //将带盐"salt"也一起加密
        Md5Hash passMD5=new Md5Hash("123456","salt",1024);
        userMap.put("wangchen",passMD5.toHex());
        super.setName("shiroRealm");
    }

    /**
     * 获取密码
     * @param userName
     * @return
     */
    private String getPasswordByUsername(String userName) {
        return userMap.get(userName);
    }


    /**
     * ----------------------------------------------------------------以上是认证，以下是授权------------------------------------------------------------------------------------------------------
     */

    /**
     * doGetAuthorizationInfo获取授权信息：PrincipalCollection是一个身份集合，因为我们现在就一个Realm，所以直接调用getPrimaryPrincipal得到之前传入的用户名即可；
     * 然后根据用户名调用UserService接口获取角色及权限信息。
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        //获取用户名(getPrimaryPrincipal()得到主要的身份  Set<String> getRealmNames(); //获取所有身份验证通过的Realm名字)
        String userName = (String) principalCollection.getPrimaryPrincipal();
        System.out.println("用户名: "+userName);

        //获取角色和权限信息（一般从数据库或缓存中获取）
        Set<String> roles=getRolesByUserName();
        Set<String> pression=getPressionByUserName();

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        simpleAuthorizationInfo.setStringPermissions(pression);
        return simpleAuthorizationInfo;
    }

    /**
     * 方便测试  设置用户权限数据
     * @return
     */
    private Set<String> getPressionByUserName() {
        Set<String> sets=new HashSet<>();
        sets.add("user:add:01");
        sets.add("user:*:02");
        sets.add("user:select");
        sets.add("admin:select");
        return sets;
    }

    /**
     * 方便测试  设置用户角色数据
     * @return
     */
    private Set<String> getRolesByUserName() {
        Set<String> sets=new HashSet<>();
        sets.add("admin");
        sets.add("user");
        return sets;
    }


}
