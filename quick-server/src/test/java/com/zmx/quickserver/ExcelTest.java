package com.zmx.quickserver;

import com.zmx.quickpojo.vo.TurnoverStatisticsRspVO;
import com.zmx.quickserver.service.StatisticsService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Apache POI Excel操作测试类
 * 生成运营数据报表
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class ExcelTest {

    @Autowired
    private StatisticsService statisticsService;

    // 通过模板创建excel文件
    @Test
    public void testWriteExcel() throws IOException {
        LocalDate beginDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now().minusDays(1);

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
        var turnover = statisticsService.turnover(beginTime, endTime);
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
        var order = statisticsService.order(beginTime, endTime);
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
            double averagePrice = validNum == 0 ? 0.0 : turnover.getTurnovers().get(i) / validNum;
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
        var user = statisticsService.user(beginTime, endTime);
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

        // 修复：使用正确的路径创建 FileOutputStream，避免资源泄漏
        // 获取resources目录路径
        String resourcesPath = this.getClass().getClassLoader().getResource("template").getPath();
        String outputPath = resourcesPath + "/写出的数据统计.xlsx";
        try (FileOutputStream out = new FileOutputStream(outputPath)) {
            excel.write(out);
        }
    }

}
