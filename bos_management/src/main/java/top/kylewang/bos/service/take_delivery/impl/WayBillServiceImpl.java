package top.kylewang.bos.service.take_delivery.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.kylewang.bos.dao.take_delivery.WayBillRepository;
import top.kylewang.bos.domain.take_delivery.WayBill;
import top.kylewang.bos.index.WayBillIndexRepository;
import top.kylewang.bos.service.take_delivery.WayBillService;

/**
 * @author Kyle.Wang
 * 2018/1/8 0008 15:13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WayBillServiceImpl implements WayBillService {

    @Autowired
    private WayBillRepository wayBillRepository;

    @Autowired
    private WayBillIndexRepository wayBillIndexRepository;

    @Override
    public void save(WayBill wayBill) {
        // 去除无效Order属性
        if(wayBill.getOrder()!=null && (wayBill.getOrder().getId() == null || wayBill.getOrder().getId()==0)){
            wayBill.setOrder(null);
        }
        // 判断是更新还是新增
        WayBill persistentWayBill = wayBillRepository.findByWayBillNum(wayBill.getWayBillNum());
        if(persistentWayBill!=null && persistentWayBill.getId()!=null){
            // 更新操作
            Integer id = persistentWayBill.getId();
            BeanUtils.copyProperties(wayBill,persistentWayBill);
            persistentWayBill.setId(id);
            wayBillRepository.save(persistentWayBill);
            // 保存索引
            wayBillIndexRepository.save(persistentWayBill);
        }else{
            // 新增操作
            wayBillRepository.save(wayBill);
            // 保存索引
            wayBillIndexRepository.save(wayBill);
        }
    }

    @Override
    public Page<WayBill> findPageData(Pageable pageable) {
        return wayBillRepository.findAll(pageable);
    }

    @Override
    public WayBill findByWayBillNum(String wayBillNum) {
        return wayBillRepository.findByWayBillNum(wayBillNum);
    }
}
