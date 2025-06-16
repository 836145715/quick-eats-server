package com.zmx.quickserver.task;

import com.zmx.common.constants.OrderConstant;
import com.zmx.quickserver.config.WebSocketServer;
import com.zmx.quickserver.mapper.OrdersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrdersMapper ordersMapper;

    //检查超时订单
    @Scheduled(cron = "0 * * * * ?")
    public void checkTimeoutOrder() {
        log.info("检查超时订单：" + LocalDateTime.now());
        var time = LocalDateTime.now().minusMinutes(OrderConstant.ORDER_TIMEOUT_MINUTES);
        var orderList = ordersMapper.selectByStatusAndOrderTimeLT(OrderConstant.Status.PENDING_PAYMENT,time);
        if(orderList != null && !orderList.isEmpty()){
            orderList.forEach(order -> {
                order.setStatus(OrderConstant.Status.CANCELLED);
                order.setCancelReason("订单超时");
                order.setCancelTime(LocalDateTime.now());
                ordersMapper.updateById(order);
            });
        }
    }

    //检查配送订单 然后设置为已完成
    @Scheduled(cron = "0 0 1 * * ?")
//    @Scheduled(cron = "0/5 * * * * ?")
    public void checkDeliveryOrder() {
        log.info("检查配送订单：" + LocalDateTime.now());
        var time = LocalDateTime.now().minusMinutes(60);
        var orderList = ordersMapper.selectByStatusAndOrderTimeLT(OrderConstant.Status.DELIVERY_IN_PROGRESS,time);
        if(orderList != null && !orderList.isEmpty()){
            orderList.forEach(order -> {
                order.setStatus(OrderConstant.Status.COMPLETED);
                ordersMapper.updateById(order);
            });
        }
    }

//    @Autowired
//    private WebSocketServer webSocketServer;
//
//    @Scheduled(cron = "0/5 * * * * ?")
//    public void testSendMsg(){
//        log.debug("服务器发送消息：" + LocalDateTime.now());
//        webSocketServer.sendAllMessage("服务器消息：" +  LocalDateTime.now());
//    }


}
