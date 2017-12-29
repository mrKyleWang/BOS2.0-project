package top.kylewang.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.kylewang.bos.dao.base.StandardRepository;
import top.kylewang.bos.domain.base.Standard;
import top.kylewang.bos.service.base.StandardService;

/**
 * @author Kyle.Wang
 * 2017/12/29 0029 12:52
 */
@Service
@Transactional
public class StandardServiceImpl implements StandardService{

    @Autowired
    private StandardRepository standardRepository;

    @Override
    public void save(Standard standard) {
        standardRepository.save(standard);
    }
}
