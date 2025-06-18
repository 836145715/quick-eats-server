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
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
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
                        LinkedHashMap::new));

        var names = new ArrayList<>(sortedMap.keySet());
        var sales = new ArrayList<>(sortedMap.values());

        var vo = new SalesTop10RspVO();

        vo.setNames(names);
        vo.setSales(sales);

        return vo;
    }

    public void export(HttpServletResponse response) {
        LocalDate beginDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now().minusDays(1);

        try {
            // 设置响应头信息，指定文件类型和下载的文件名
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 设置文件名
            String fileName = URLEncoder.encode("数据统计" + beginDate + "至" + endDate, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/数据统计模板.xlsx");
            XSSFWorkbook excel = new XSSFWorkbook(in);
            Sheet sheet = excel.getSheet("Sheet1");
            var row = sheet.getRow(1);
            if (row == null) {
                row = sheet.createRow(1);
            }
            var cell = row.getCell(0);
            if (cell == null) {
                cell = row.createCell(0);
            }
            // 设置行高
            row.setHeight((short) 500);
            // 设置居中对齐
            cell.setCellStyle(excel.createCellStyle());
            cell.getCellStyle().setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
            cell.getCellStyle().setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
            // 设置字体加粗
            var font = excel.createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 16);
            cell.getCellStyle().setFont(font);

            cell.setCellValue("时间：" + beginDate + " 至 " + endDate);

            // 营业额设置
            LocalDateTime beginTime = beginDate.atStartOfDay();
            LocalDateTime endTime = endDate.atTime(23, 59, 59);
            var turnover = this.turnover(beginTime, endTime);
            List<Double> turnovers = turnover.getTurnovers();

            var style = excel.createCellStyle();
            style.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
            style.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
            // 设置边框
            style.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            style.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            style.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            style.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);

            font = excel.createFont();
            font.setFontHeightInPoints((short) 12);
            style.setFont(font);

            Double turnoverSum = 0.0;
            for (int i = 0; i < turnovers.size(); i++) {
                turnoverSum += turnovers.get(i);
                // 写入列表项目
                row = sheet.getRow(i + 7);
                if (row == null) {
                    row = sheet.createRow(i + 7);
                }
                row.setHeight((short) 400);
                cell = row.getCell(0);
                if (cell == null) {
                    cell = row.createCell(0);
                }
                cell.setCellStyle(style);
                cell.setCellValue(turnover.getDates().get(i));
                cell = row.getCell(1);
                if (cell == null) {
                    cell = row.createCell(1);
                }
                cell.setCellStyle(style);
                cell.setCellValue(turnovers.get(i));
            }

            // 写入总营业额
            row = sheet.getRow(3);
            if (row == null) {
                row = sheet.createRow(3);
            }
            row.setHeight((short) 400);
            cell = row.getCell(1);
            if (cell == null) {
                cell = row.createCell(1);
            }
            cell.setCellStyle(style);
            cell.setCellValue(turnoverSum);

            // 订单设置
            var order = this.order(beginTime, endTime);
            List<Integer> totalNums = order.getTotalNums();
            List<Integer> validNums = order.getValidNums();
            // 计算订单完成率
            for (int i = 0; i < totalNums.size(); i++) {
                // 有效订单
                var validNum = validNums.get(i);
                var totalNum = totalNums.get(i);
                // 订单完成率 百分号
                String completeRate = validNum == 0 ? "0.00%"
                        : String.format("%.2f%%", ((double) validNum / totalNum) * 100);
                // 写入列表项目
                row = sheet.getRow(i + 7);
                if (row == null) {
                    row = sheet.createRow(i + 7);
                }
                row.setHeight((short) 400);
                cell = row.getCell(2);
                if (cell == null) {
                    cell = row.createCell(2);
                }
                cell.setCellStyle(style);
                cell.setCellValue(validNum);

                cell = row.getCell(3);
                if (cell == null) {
                    cell = row.createCell(3);
                }
                cell.setCellStyle(style);
                cell.setCellValue(completeRate);

                // 设置平均客单价
                double averagePrice = validNum == 0 ? 0.0 : turnovers.get(i) / validNum;
                cell = row.getCell(4);
                if (cell == null) {
                    cell = row.createCell(4);
                }
                cell.setCellStyle(style);
                cell.setCellValue(averagePrice);

            }

            // 设置总的有效订单
            var totalNum = totalNums.stream().reduce(0, Integer::sum);
            var validNum = validNums.stream().reduce(0, Integer::sum);
            // 计算订单完成率
            String completeRate = validNum == 0 ? "0.00%"
                    : String.format("%.2f%%", ((double) validNum / totalNum) * 100);
            // 总平均客单价
            double averagePrice = validNum == 0 ? 0.0 : turnoverSum / validNum;

            // 写入总的有效订单
            row = sheet.getRow(4);
            if (row == null) {
                row = sheet.createRow(4);
            }
            row.setHeight((short) 400);
            cell = row.getCell(1);
            if (cell == null) {
                cell = row.createCell(1);
            }
            cell.setCellStyle(style);
            cell.setCellValue(validNum);

            // 写入总订单完成率
            row = sheet.getRow(3);
            if (row == null) {
                row = sheet.createRow(3);
            }
            cell = row.getCell(3);
            if (cell == null) {
                cell = row.createCell(3);
            }
            cell.setCellStyle(style);
            cell.setCellValue(completeRate);

            // 写入总平均客单价
            row = sheet.getRow(4);
            if (row == null) {
                row = sheet.createRow(4);
            }
            cell = row.getCell(3);
            if (cell == null) {
                cell = row.createCell(3);
            }
            cell.setCellStyle(style);
            cell.setCellValue(averagePrice);

            // 设置新增用户数量
            var user = this.user(beginTime, endTime);
            List<Integer> newUsers = user.getNewUsers();

            // 写入新增用户数量
            for (int i = 0; i < newUsers.size(); i++) {
                row = sheet.getRow(i + 7);
                if (row == null) {
                    row = sheet.createRow(i + 7);
                }
                row.setHeight((short) 400);
                cell = row.getCell(5);
                if (cell == null) {
                    cell = row.createCell(5);
                }
                cell.setCellStyle(style);
                cell.setCellValue(newUsers.get(i));
            }

            // 计算新增用户总数量
            var newUserNum = newUsers.stream().reduce(0, Integer::sum);
            row = sheet.getRow(3);
            if (row == null) {
                row = sheet.createRow(3);
            }
            cell = row.getCell(5);
            if (cell == null) {
                cell = row.createCell(5);
            }
            cell.setCellStyle(style);
            cell.setCellValue(newUserNum);

            var out = response.getOutputStream();
            // 直接写入到响应流中
            excel.write(out);
            out.flush();
            out.close();

            // 关闭工作簿
            excel.close();
            if (in != null) {
                in.close();
            }
        } catch (Exception e) {
            throw new BusinessException("导出失败: " + e.getMessage());
        }
    }
}
