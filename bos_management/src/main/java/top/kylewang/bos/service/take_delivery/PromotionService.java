package top.kylewang.bos.service.take_delivery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import top.kylewang.bos.domain.take_delivery.Promotion; /**
 * @author Kyle.Wang
 * 2018/1/4 0004 20:59
 */
public interface PromotionService {

    /**
     * 保存
     * @param model
     */
    void save(Promotion model);

    /**
     * 分页查询
     * @param pageable
     * @return
     */
    Page<Promotion> findPageData(Pageable pageable);

}
