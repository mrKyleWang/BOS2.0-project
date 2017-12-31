package top.kylewang.crm.service;

import top.kylewang.crm.domain.Customer;

import javax.ws.rs.*;
import java.util.List;

/**
 * @author Kyle.Wang
 * 2017/12/31 0031 16:12
 */
    public interface CustomerService {

    /**
     * 查询所有未关联客户列表
     * @return
     */
    @Path("/findnoassociationcustomers")
    @GET
    @Produces({"application/xml","application/json"})
    public List<Customer> findNoAssociationCustomers();

    /**
     * 查询已关联到指定定区的客户列表
     * @param fixedAreaId
     * @return
     */
    @Path("findassociationfixedareacustomers/{fixedareaid}")
    @GET
    @Produces({"application/xml","application/json"})
    public List<Customer> findAssociationFixedAreaCustomers(@PathParam("fixedareaid") String fixedAreaId);


    /**
     * 批量关联客户到指定定区
     * @param customerIdStr
     * @param fixedAreaId
     */
    @Path("associationcustomerstofixedarea")
    @PUT
    @Produces({"application/xml","application/json"})
    public void associationCustomersToFixedArea(
            @QueryParam("customerIdStr") String customerIdStr,
            @QueryParam("fixedAreaId") String fixedAreaId);
}
