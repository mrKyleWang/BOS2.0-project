package top.kylewang.bos.web.action.base;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import top.kylewang.bos.domain.base.Courier;
import top.kylewang.bos.service.base.CourierService;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Kyle.Wang
 * 2017/12/30 0030 10:03
 */
@Controller
@Scope("prototype")
@Actions
@Namespace("/")
@ParentPackage("json-default")
public class CourierAction extends ActionSupport implements ModelDriven<Courier> {

    @Autowired
    private CourierService courierService;

    private Courier courier = new Courier();

    @Override
    public Courier getModel() {
        return courier;
    }

    /**
     * 保存快递员
     * @return
     */
    @Action(value = "courier_save",
            results = {@Result(name = "success", location = "./pages/base/courier.html", type = "redirect")})
    public String save() {
        courierService.save(courier);
        return SUCCESS;
    }

    private Integer page;
    private Integer rows;
    public void setPage(Integer page) {
        this.page = page;
    }
    public void setRows(Integer rows) {
        this.rows = rows;
    }

    /**
     * 分页条件查询
     * @return
     */
    @Action(value = "courier_pageQuery",results = {@Result(name = "success",type = "json")})
    public String pageQuery(){
        //分页查询对象
        Pageable pageable = new PageRequest(page-1,rows);
        //条件查询对象
        Specification<Courier> specification = new Specification<Courier>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(StringUtils.isNotBlank(courier.getCourierNum())){
                    //快递员编号等值查询
                    Predicate predicate1 = criteriaBuilder.equal(root.get("courierNum").as(String.class), courier.getCourierNum());
                    list.add(predicate1);
                }
                if(StringUtils.isNotBlank(courier.getType())){
                    //快递员类型等值查询
                    Predicate predicate2 = criteriaBuilder.equal(root.get("type").as(String.class), courier.getType());
                    list.add(predicate2);
                }
                if(StringUtils.isNotBlank(courier.getCompany())){
                    //所属单位模糊查询
                    Predicate predicate3 = criteriaBuilder.like(root.get("company").as(String.class), "%"+courier.getCompany()+"%");
                    list.add(predicate3);
                }
                //多表查询关联对象
                Join standard = root.join("standard", JoinType.INNER);
                if(courier.getStandard()!=null && StringUtils.isNotBlank(courier.getStandard().getName())){
                    //收派标准模糊查询
                    Predicate predicate4 = criteriaBuilder.like(standard.get("name").as(String.class), "%"+courier.getStandard().getName()+"%");
                    list.add(predicate4);
                }
                //转为参数数组
                return criteriaBuilder.and(list.toArray(new Predicate[0]));
            }
        };
        Page<Courier> pageData =  courierService.findPageData(specification,pageable);
        Map<String,Object> result = new HashMap<>(4);
        result.put("total",pageData.getNumberOfElements());
        result.put("rows",pageData.getContent());
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }

    private String ids;
    public void setIds(String ids) {
        this.ids = ids;
    }

    /**
     * 批量作废快递员
     * @return
     */
    @Action(value = "courier_delBatch",
            results = {@Result(name = "success", location = "./pages/base/courier.html", type = "redirect")})
    public String delBatch() {

        String[] idArray = ids.split(",");
        courierService.delBatch(idArray);
        return SUCCESS;
    }


}
