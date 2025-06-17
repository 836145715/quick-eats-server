package com.zmx.quickserver;


import com.zmx.common.constants.OrderConstant;
import com.zmx.quickserver.mapper.OrdersMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class StatisticsTest {


    @Autowired
    private OrdersMapper ordersMapper;


    @Test
    public void testTurnover() {
        log.info("=== 开始测试营业额统计 ===");
        ordersMapper.turnoverStatistics(OrderConstant.Status.COMPLETED, null, null);
        log.info("=== 营业额统计测试完成 ===");
    }

}
