package top.kylewang.bos.web.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import top.kylewang.bos.domain.take_delivery.Promotion;
import top.kylewang.bos.web.action.common.BaseAction;

/**
 * @author Kyle.Wang
 * 2018/1/6 0006 10:46
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class PromotionAction extends BaseAction<Promotion>{


    @Action(value = "promotion_pageQuery",
            results = {@Result(name = "success",type = "json")})
    public String pageQuery(){
        //基于WebService获取bos_management 的活动列表数据信息



        return SUCCESS;
    }
}
