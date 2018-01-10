package top.kylewang.bos.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.kylewang.bos.domain.system.User;
import top.kylewang.bos.service.system.UserService;

/**
 * @author Kyle.Wang
 * 2018/1/10 0010 15:48
 */
@Service("BosRealm")
public class BosRealm extends AuthorizingRealm{

    @Autowired
    private UserService userService;

    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        User user = userService.findByUsername(usernamePasswordToken.getUsername());
        if(user == null){
            // 用户不存在
            return null;
        }else{
            /*
             * 用户存在
             * SimpleAuthenticationInfo :
             *      参数一 : 期望登录后, 保存在Subject中的信息
             *      参数二 : 密码, 如果返回为null, 说明用户不存在, 报用户名不存在异常
             *      参数三 : realm名称
             * 当返回用户密码时, securityManager安全管理器会自动比较返回密码和用户输入密码是否一致
             * 如果密码一致, 登录成功, 如果密码不一致, 报密码错误异常
             */
            return new SimpleAuthenticationInfo(user,user.getPassword(),getName());
        }
    }

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

}
