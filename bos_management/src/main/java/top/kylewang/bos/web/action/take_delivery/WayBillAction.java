package top.kylewang.bos.web.action.take_delivery;

import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import top.kylewang.bos.domain.take_delivery.WayBill;
import top.kylewang.bos.service.take_delivery.WayBillService;
import top.kylewang.bos.web.action.common.BaseAction;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kyle.Wang
 * 2018/1/8 0008 15:12
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class WayBillAction extends BaseAction<WayBill>{

    @Autowired
    private WayBillService wayBillService;

    /**
     * 保存
     * @return
     */
    @Action(value = "waybill_save",results = {@Result(name = "success",type = "json")})
    public String save(){
        Map<String, Object> result = new HashMap<>(4);
        try{
            wayBillService.save(model);
            //保存成功
            result.put("success",true);
            result.put("msg","保存运单成功!");
        }catch (Exception e){
            // 保存失败
            e.printStackTrace();
            result.put("success",false);
            result.put("msg","保存运单失败!");
        }
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }

    /**
     * 分页查询
     * @return
     */
    @Action(value = "waybill_pageQuery",results = {@Result(name = "success",type = "json")})
    public String pageQuery(){
        Pageable pageable = new PageRequest(page - 1, rows,new Sort(new Sort.Order(Sort.Direction.DESC,"id")));
        Page<WayBill> pageData = wayBillService.findPageData(pageable);
        pushPageDataToValueStack(pageData);
        return SUCCESS;
    }

    /**
     * 根据运单号查询
     * @return
     */
    @Action(value = "waybill_findByWayBillNum",results = {@Result(name = "success",type = "json")})
    public String findByWayBillNum(){
        Map<String, Object> result = new HashMap<>(4);
        WayBill wayBill = wayBillService.findByWayBillNum(model.getWayBillNum());
        if(wayBill!=null){
            result.put("success",true);
            result.put("wayBillData",wayBill);
        }else{
            result.put("success",false);
        }
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }
}
