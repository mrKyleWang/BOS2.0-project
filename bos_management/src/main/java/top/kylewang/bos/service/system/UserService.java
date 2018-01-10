package top.kylewang.bos.service.system;

import top.kylewang.bos.domain.system.User;

/**
 * @author Kyle.Wang
 * 2018/1/10 0010 16:23
 */
public interface UserService {


    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    User findByUsername(String username);
}
