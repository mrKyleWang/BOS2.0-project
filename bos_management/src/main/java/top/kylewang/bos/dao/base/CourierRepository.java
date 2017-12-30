package top.kylewang.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.kylewang.bos.domain.base.Courier;

/**
 * @author Kyle.Wang
 * 2017/12/30 0030 10:08
 */
public interface CourierRepository extends JpaRepository<Courier, Integer>, JpaSpecificationExecutor<Courier> {
}
