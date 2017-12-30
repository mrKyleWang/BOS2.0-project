package top.kylewang.bos.service.base;

import top.kylewang.bos.domain.base.Area;

import java.util.List; /**
 * @author Kyle.Wang
 * 2017/12/30 0030 17:03
 */
public interface AreaService {
    /**
     * 批量保存
     * @param list
     */
    void saveBatch(List<Area> list);
}
