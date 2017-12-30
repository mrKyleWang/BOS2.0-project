package top.kylewang.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.kylewang.bos.dao.base.CourierRepository;
import top.kylewang.bos.domain.base.Courier;
import top.kylewang.bos.service.base.CourierService;

/**
 * @author Kyle.Wang
 * 2017/12/30 0030 10:07
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CourierServiceImpl implements CourierService {

    @Autowired
    private CourierRepository courierRepository;


    @Override
    public void save(Courier courier) {
        courierRepository.save(courier);
    }

    @Override
    public Page<Courier> findPageData(Specification<Courier> specification, Pageable pageable) {
        return courierRepository.findAll(specification, pageable);
    }

    @Override
    public void delBatch(String[] idArray) {
        for (String s : idArray) {
            Integer id = Integer.parseInt(s);
            courierRepository.updateDelTag(id);
        }
    }
}
