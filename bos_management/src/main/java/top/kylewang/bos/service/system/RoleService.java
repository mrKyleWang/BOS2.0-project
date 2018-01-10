package top.kylewang.bos.service.system;

import top.kylewang.bos.domain.system.Role;
import top.kylewang.bos.domain.system.User;

import java.util.List;

/**
 * @author Kyle.Wang
 * 2018/1/10 0010 17:29
 */
public interface RoleService {

    /**
     * 根据用户查询角色列表
     * @param user
     * @return
     */
    List<Role> findByUser(User user);
}
