package top.kylewang.bos.service.take_delivery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import top.kylewang.bos.domain.page.PageBean;
import top.kylewang.bos.domain.take_delivery.Promotion;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
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

    /**
     * 根据page和rows 返回分页数据
     * @param page
     * @param rows
     * @return
     */
    @Path("/pageQuery")
    @GET
    @Produces({"application/xml","application/json"})
    PageBean<Promotion> findPageData(@QueryParam("page") Integer page,
                                     @QueryParam("rows") Integer rows);

}
