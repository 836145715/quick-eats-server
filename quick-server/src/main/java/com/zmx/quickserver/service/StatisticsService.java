package com.zmx.quickserver.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zmx.common.constants.OrderConstant;
import com.zmx.common.exception.BusinessException;
import com.zmx.quickpojo.entity.User;
import com.zmx.quickpojo.vo.OrderStatisticsRspVO;
import com.zmx.quickpojo.vo.SalesTop10RspVO;
import com.zmx.quickpojo.vo.TurnoverStatisticsRspVO;
import com.zmx.quickpojo.vo.UserStatisticsRspVO;
import com.zmx.quickserver.mapper.OrdersMapper;
import com.zmx.quickserver.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StatisticsService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private UserMapper userMapper;

    public TurnoverStatisticsRspVO turnover(LocalDateTime begin, LocalDateTime end) {
        if (end.isBefore(begin)) {
            throw new BusinessException("结束时间不能早于开始时间");
        }

        if (end.isAfter(LocalDateTime.now())) {
            end = LocalDateTime.now();
        }

        var data = ordersMapper.turnoverStatistics(OrderConstant.Status.COMPLETED, begin, end);
        if (data == null) {
            data = new ArrayList<>();
        }

        Map<String, Double> dataMap = data.stream().collect(Collectors.toMap(item -> item.get("date").toString(),
                item -> Double.parseDouble(item.get("amount").toString())));

        var vo = new TurnoverStatisticsRspVO();

        for (; begin.isBefore(end); begin = begin.plusDays(1)) {
            vo.getDates().add(begin.toLocalDate().toString());
            vo.getTurnovers().add(dataMap.getOrDefault(begin.toLocalDate().toString(), 0.0));
        }

        return vo;
    }

    public UserStatisticsRspVO user(LocalDateTime begin, LocalDateTime end) {
        if (end.isBefore(begin)) {
            throw new BusinessException("结束时间不能早于开始时间");
        }

        if (end.isAfter(LocalDateTime.now())) {
            end = LocalDateTime.now();
        }
        var data = userMapper.userStatistics(begin, end);
        if (data == null) {
            data = new ArrayList<>();
        }

        Map<String, Integer> dataMap = data.stream().collect(Collectors.toMap(
                item -> item.get("date").toString(),
                item -> Integer.parseInt(item.get("inc_count").toString())));

        var vo = new UserStatisticsRspVO();

        // 优化点1: 提前计算初始总用户数
        // 查询在统计开始日期之前（不含该日期）的总用户数
        LambdaQueryWrapper<User> initialQuery = new LambdaQueryWrapper<>();
        initialQuery.lt(User::getCreateTime, begin.toLocalDate().atStartOfDay()); // 小于开始日期当天的0点
        long currentTotalUsers = userMapper.selectCount(initialQuery);

        // 临时变量用于迭代，不改变原始的begin参数
        LocalDateTime tempBegin = begin;
        for (; tempBegin.isBefore(end); tempBegin = tempBegin.plusDays(1)) {
            String currentDateStr = tempBegin.toLocalDate().toString();
            vo.getDates().add(currentDateStr);

            // 获取当天新增用户数
            int newUsersToday = dataMap.getOrDefault(currentDateStr, 0);
            vo.getNewUsers().add(newUsersToday);

            // 优化点2: 累计计算总用户数，避免循环查询数据库
            currentTotalUsers += newUsersToday;
            vo.getTotalUsers().add(currentTotalUsers);
        }

        return vo;
    }


    public OrderStatisticsRspVO order(LocalDateTime begin, LocalDateTime end) {
        if (end.isBefore(begin)) {
            throw new BusinessException("结束时间不能早于开始时间");
        }

        if (end.isAfter(LocalDateTime.now())) {
            end = LocalDateTime.now();
        }

        var data = ordersMapper.orderStatistics(begin, end);
        if (data == null) {
            data = new ArrayList<>();
        }

        Map<String, List<Integer>> dataMap = data.stream().collect(Collectors.toMap(item -> item.get("date").toString(),
                item -> {
                    List<Integer> list = new ArrayList<>();
                    list.add(Integer.parseInt(item.get("total_nums").toString()));
                    list.add(Integer.parseInt(item.get("valid_nums").toString()));
                    return list;
                }));

        var vo = new OrderStatisticsRspVO();

        for (; begin.isBefore(end); begin = begin.plusDays(1)) {
            vo.getDates().add(begin.toLocalDate().toString());
            var list = dataMap.getOrDefault(begin.toLocalDate().toString(), new ArrayList<>());
            vo.getTotalNums().add(list.isEmpty() ? 0 : list.get(0));
            vo.getValidNums().add(list.size() < 2 ? 0 : list.get(1));
        }

        return vo;
    }


    public SalesTop10RspVO sales(LocalDateTime begin, LocalDateTime end) {
        if (end.isBefore(begin)) {
            throw new BusinessException("结束时间不能早于开始时间");
        }

        if (end.isAfter(LocalDateTime.now())) {
            end = LocalDateTime.now();
        }

        var data = ordersMapper.salesTop10Statistics(begin, end.plusDays(1));
        if (data == null) {
            data = new ArrayList<>();
        }

        Map<String, Long> dataMap = data.stream().collect(Collectors.toMap(item -> item.get("name").toString(),
                item -> Long.parseLong(item.get("sale_nums").toString())));

        Map<String, Long> sortedMap = dataMap.entrySet()
                .stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));

        var names = new ArrayList<>(sortedMap.keySet());
        var sales = new ArrayList<>(sortedMap.values());

        var vo = new SalesTop10RspVO();

        vo.setNames(names);
        vo.setSales(sales);

        return vo;
    }

}
