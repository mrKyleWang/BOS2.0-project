package top.kylewang.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import top.kylewang.bos.domain.base.FixedArea; /**
 * @author Kyle.Wang
 * 2017/12/30 0030 21:58
 */
public interface FixedAreaService {

    /**
     * 保存定区
     * @param model
     */
    void save(FixedArea model);

    /**
     * 条件分页查询
     * @param specification
     * @param pageable
     * @return
     */
    Page<FixedArea> findPageData(Specification<FixedArea> specification, Pageable pageable);
}
