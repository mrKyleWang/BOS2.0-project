package top.kylewang.bos.service.system.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.kylewang.bos.dao.system.UserRepository;
import top.kylewang.bos.domain.system.User;
import top.kylewang.bos.service.system.UserService;

/**
 * @author Kyle.Wang
 * 2018/1/10 0010 16:23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
