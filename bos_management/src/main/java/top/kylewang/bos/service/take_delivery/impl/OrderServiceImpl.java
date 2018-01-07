package top.kylewang.bos.service.take_delivery.impl;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import top.kylewang.bos.constants.Constants;
import top.kylewang.bos.dao.base.AreaRepository;
import top.kylewang.bos.dao.base.FixedAreaRepository;
import top.kylewang.bos.dao.base.WorkBillRepository;
import top.kylewang.bos.dao.take_delivery.OrderRepository;
import top.kylewang.bos.domain.base.Area;
import top.kylewang.bos.domain.base.Courier;
import top.kylewang.bos.domain.base.FixedArea;
import top.kylewang.bos.domain.base.SubArea;
import top.kylewang.bos.domain.take_delivery.Order;
import top.kylewang.bos.domain.take_delivery.WorkBill;
import top.kylewang.bos.service.take_delivery.OrderService;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.UUID;

/**
 * @author Kyle.Wang
 * 2018/1/7 0007 19:43
 */
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private FixedAreaRepository fixedAreaRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private WorkBillRepository workBillRepository;

    @Autowired
    @Qualifier("jmsQueueTemplate")
    private JmsTemplate smsTemplate;



    @Override
    public void saveOrder(Order order) {

        // 设置订单号
        order.setOrderNum(UUID.randomUUID().toString());
        // 设置下单时间
        order.setOrderTime(new Date());
        // 设置状态  "1":待取件
        order.setStatus("1");

        // 自动分单逻辑A : 基于CRM地址库完全匹配, 获取定区, 匹配快递员
        System.out.println(order);
        String fixedAreaId = WebClient
                .create(Constants.CRM_MANAGEMENT_URL + "/services/customerService/findFixedAreaIdByAddress?address=" + order.getSendAddress())
                .accept(MediaType.APPLICATION_JSON).get(String.class);
        if(fixedAreaId!=null){
            FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
            Courier courier = fixedArea.getCouriers().iterator().next();
            if(courier!=null){
                saveOrder(order,courier);
                generateWorkBill(order);
                return;
            }
        }
        // 自动分单逻辑B : 基于分区关键字,通过省市区匹配地址
        Area area = order.getSendArea();
        Area existArea = areaRepository.findByProvinceAndCityAndDistrict(area.getProvince(), area.getCity(), area.getDistrict());
        for (SubArea subArea : existArea.getSubareas()) {
            // 判断下单地址是否包含分区关键字
            if(order.getSendAddress().contains(subArea.getKeyWords())){
                // 确定分区, 确定定区, 匹配快递员
                Courier courier = subArea.getFixedArea().getCouriers().iterator().next();
                if(courier!=null){
                    saveOrder(order,courier);
                    generateWorkBill(order);
                    return;
                }
            }
        }
        // 自动分单逻辑B2 : 基于分区辅助关键字,通过省市区匹配地址
        for (SubArea subArea : existArea.getSubareas()) {
            // 判断下单地址是否包含分区关键字
            if(order.getSendAddress().contains(subArea.getAssistKeyWords())){
                // 确定分区, 确定定区, 匹配快递员
                Courier courier = subArea.getFixedArea().getCouriers().iterator().next();
                if(courier!=null){
                    saveOrder(order,courier);
                    generateWorkBill(order);
                    return;
                }

            }
        }
        // 进入人工分单
        // 设置分单类型 "2":人工分单
        order.setOrderType("2");
        orderRepository.save(order);

    }

    /**
     * 生成工单,发送短信
     * @param order
     */
    private void generateWorkBill(Order order){
        // 生成工单
        WorkBill workBill = new WorkBill();
        workBill.setType("新");
        workBill.setPickstate("新单");
        workBill.setBuildtime(new Date());
        workBill.setRemark(order.getRemark());
        // 设置短信序号
        String smsNumber = RandomStringUtils.randomNumeric(4);
        workBill.setSmsNumber(smsNumber);
        workBill.setOrder(order);
        workBill.setCourier(order.getCourier());
        workBillRepository.save(workBill);

        // 发送短信
        smsTemplate.send("bos_sms", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("telephone",order.getCourier().getTelephone());
                mapMessage.setString("msg","短信序号:"+smsNumber+",取件地址:"+order.getSendAddress()
                        +",发件人:"+order.getSendName()+",发件人手机号:"+order.getSendMobile()
                        +",快递员捎话:"+order.getSendMobileMsg());
                return mapMessage;
            }
        });

        // 修改工单状态
        workBill.setPickstate("已通知");
    }

    /**
     * 抽离自动分单成功后保存订单方法
     * @param order
     * @param courier
     */
    private void saveOrder(Order order,Courier courier){
        // 自动分单成功, 将快递员关联到订单
        order.setCourier(courier);
        // 设置分单类型 "1":自动分单
        order.setOrderType("1");
        // 保存订单
        orderRepository.save(order);
    }
}
