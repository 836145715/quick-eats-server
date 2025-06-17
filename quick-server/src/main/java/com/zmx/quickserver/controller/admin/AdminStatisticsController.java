package com.zmx.quickserver.controller.admin;

import com.zmx.common.response.Result;
import com.zmx.quickpojo.vo.OrderStatisticsRspVO;
import com.zmx.quickpojo.vo.SalesTop10RspVO;
import com.zmx.quickpojo.vo.TurnoverStatisticsRspVO;
import com.zmx.quickpojo.vo.UserStatisticsRspVO;
import com.zmx.quickserver.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin/statistics")
@Tag(name = "统计管理", description = "统计相关接口")
@Slf4j
public class AdminStatisticsController {

    @Autowired
    private StatisticsService statisticsService;



    @GetMapping ("/turnover")
    @Operation (summary = "营业额统计", description = "营业额统计接口")
    public Result<TurnoverStatisticsRspVO> turnoverStatistics(@RequestParam("begin")LocalDate begin,
                                                    @RequestParam("end")LocalDate end) {
        return Result.success(statisticsService.turnover(begin.atStartOfDay(), end.atStartOfDay()));
    }

    @GetMapping ("/user")
    @Operation (summary = "用户统计", description = "新增用户和用户总量")
    public Result<UserStatisticsRspVO> userStatistics(@RequestParam("begin")LocalDate begin,
                                                @RequestParam("end")LocalDate end) {
        return Result.success(statisticsService.user(begin.atStartOfDay(), end.atStartOfDay()));
    }


    @GetMapping ("/order")
    @Operation (summary = "订单统计", description = "订单总量和订单有效数量")
    public Result<OrderStatisticsRspVO> orderStatistics(@RequestParam("begin")LocalDate begin,
                                                        @RequestParam("end")LocalDate end) {
        return Result.success(statisticsService.order(begin.atStartOfDay(), end.atStartOfDay()));
    }

    @GetMapping ("/sales")
    @Operation (summary = "销量统计", description = "商品销量top10统计")
    public Result<SalesTop10RspVO> salesStatistics(@RequestParam("begin")LocalDate begin,
                                                   @RequestParam("end")LocalDate end) {
        return Result.success(statisticsService.sales(begin.atStartOfDay(), end.atStartOfDay()));
    }

}
