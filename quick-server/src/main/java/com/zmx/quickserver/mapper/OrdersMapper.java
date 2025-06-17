package com.zmx.quickserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zmx.quickpojo.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 订单Mapper接口
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

    /**
     * 根据订单状态和下单时间查询订单 小于
     * @param status 订单状态
     * @param orderTime 下单时间
     * @return
     */
    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> selectByStatusAndOrderTimeLT(Integer status, LocalDateTime orderTime);

    /**
     * 营业额统计
     * @param status 订单状态
     * @param begin 开始时间
     * @param end 结束时间
     * @return
     */
    @Select("select date(order_time) `date`,sum(amount) `amount` " +
            "from orders " +
            "where status = #{status} and order_time >= #{begin} and order_time < #{end} " +
            "group by date(order_time) ")
    List<Map<String, Object>> turnoverStatistics(Integer status, LocalDateTime begin, LocalDateTime end);



    @Select("select date(order_time) `date`, " +
            " count(amount) `total_nums`," +
            "sum(case when status = 5 then 1 else 0 end) `valid_nums`" +
            "from orders " +
            "where order_time >= #{begin} and order_time < #{end} " +
            "group by date(order_time) ")
    List<Map<String, Object>> orderStatistics(LocalDateTime begin, LocalDateTime end);


    /**
     * 查询指定时间段内，已完成订单的商品销售数量
     * @param begin 开始时间
     * @param end 结束时间
     * @return 商品名称及销售数量
     */
    @Select("SELECT order_detail.name, SUM(order_detail.number) `sale_nums` " +
            "FROM orders, order_detail " +
            "WHERE orders.id = order_detail.order_id " +
            "AND orders.status = 5 " +
            "AND orders.order_time BETWEEN #{begin} AND #{end} " +
            "GROUP BY order_detail.name " +
            "ORDER BY `sale_nums` DESC " +
            "LIMIT 10")
    List<Map<String, Object>> salesTop10Statistics(LocalDateTime begin, LocalDateTime end);
}