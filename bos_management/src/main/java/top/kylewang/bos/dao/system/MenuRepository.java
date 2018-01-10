package top.kylewang.bos.dao.system;

import org.springframework.data.jpa.repository.JpaRepository;
import top.kylewang.bos.domain.system.Menu;

/**
 * @author Kyle.Wang
 * 2018/1/10 0010 21:21
 */
public interface MenuRepository extends JpaRepository<Menu,Integer> {
}
