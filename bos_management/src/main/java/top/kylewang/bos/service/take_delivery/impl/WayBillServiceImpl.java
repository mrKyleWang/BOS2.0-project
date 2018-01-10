package top.kylewang.bos.service.take_delivery.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.kylewang.bos.dao.take_delivery.WayBillRepository;
import top.kylewang.bos.domain.take_delivery.WayBill;
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

    @Override
    public void save(WayBill model) {
        // 去除无效Order属性
        if(model.getOrder()!=null && (model.getOrder().getId() == null ||model.getOrder().getId()==0)){
            model.setOrder(null);
        }
        // 判断是更新还是新增
        WayBill persistentWayBill = wayBillRepository.findByWayBillNum(model.getWayBillNum());
        if(persistentWayBill!=null && persistentWayBill.getId()!=null){
            Integer id = persistentWayBill.getId();
            BeanUtils.copyProperties(model,persistentWayBill);
            persistentWayBill.setId(id);
            wayBillRepository.save(persistentWayBill);
        }else{
            wayBillRepository.save(model);
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
