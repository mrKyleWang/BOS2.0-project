package top.kylewang.crm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.kylewang.crm.dao.CustomerRepository;
import top.kylewang.crm.domain.Customer;
import top.kylewang.crm.service.CustomerService;

import java.util.List;

/**
 * @author Kyle.Wang
 * 2017/12/31 0031 16:58
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;


    @Override
    public List<Customer> findNoAssociationCustomers() {
        return customerRepository.findByFixedAreaIdIsNull();
    }

    @Override
    public List<Customer> findAssociationFixedAreaCustomers(String fixedAreaId) {
        return customerRepository.findByFixedAreaId(fixedAreaId);
    }

    @Override
    public void associationCustomersToFixedArea(String customerIdStr, String fixedAreaId) {
        String[] idsArray = customerIdStr.split(",");
        for (String ids : idsArray) {
            int id = Integer.parseInt(ids);
            customerRepository.updateFixedAreaId(fixedAreaId,id);
        }
    }
}
