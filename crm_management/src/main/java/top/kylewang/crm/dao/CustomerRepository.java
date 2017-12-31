package top.kylewang.crm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import top.kylewang.crm.domain.Customer;

import java.util.List;

/**
 * @author Kyle.Wang
 * 2017/12/31 0031 16:51
 */
public interface CustomerRepository extends JpaRepository<Customer,Integer>{

    /**
     * 查询所有未关联客户列表
     * @return
     */
    List<Customer> findByFixedAreaIdIsNull();

    /**
     * 查询已关联到指定定区的客户列表
     * @param fixedAreaId
     * @return
     */
    List<Customer> findByFixedAreaId(String fixedAreaId);

    /**
     * 批量关联客户到指定定区
     * @param fixedAreaId
     * @param id
     */
    @Query("update Customer set fixedAreaId = ? where id = ?")
    @Modifying
    void updateFixedAreaId(String fixedAreaId,Integer id);

}
