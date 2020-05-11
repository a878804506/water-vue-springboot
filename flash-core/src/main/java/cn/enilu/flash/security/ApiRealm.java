package cn.enilu.flash.security;

import cn.enilu.flash.bean.core.ShiroUser;
import cn.enilu.flash.cache.TokenCache;
import cn.enilu.flash.service.system.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author ：enilu
 * @date ：Created in 2019/7/30 22:58
 */
@Service
public class ApiRealm extends AuthorizingRealm {

    private     Logger logger = LogManager.getLogger(getClass());
    @Autowired
    private UserService userService;
    @Autowired
    private ShiroFactroy shiroFactroy;
    @Autowired
    private TokenCache tokenCache;


    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = JwtUtil.getUsername(principals.toString());

        ShiroUser user = shiroFactroy.shiroUser(userService.findByAccount(username));
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(user.getRoleCodes());
        Set<String> permission = user.getPermissions();
        simpleAuthorizationInfo.addStringPermissions(permission);
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 解密获得username，用于和数据库进行对比
        String username = JwtUtil.getUsername(token);
        if (username == null) {
            throw new AuthenticationException("token invalid");
        }

        ShiroUser shiroUser = tokenCache.getUser(token);
        if(shiroUser == null){
            // redis中的token 过期
            throw new AuthenticationException("Username or password error!");
        }
        if (! JwtUtil.verify(token, username, shiroUser.getPassword())) {
            // jwt生成的token 过期  ，实际上 redis的失效时间与token的失效时间一致
            throw new AuthenticationException("Username or password error");
        }
        tokenCache.setUser(token, shiroUser);
        return new SimpleAuthenticationInfo(token, token, "my_realm");
    }
}
