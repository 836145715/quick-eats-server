package com.zmx.quickserver.controller.admin;

import com.zmx.common.response.Result;
import com.zmx.quickpojo.vo.DashboardTodayRspVO;
import com.zmx.quickserver.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/dashboard")
@Tag(name = "仪表盘接口", description = "仪表盘数据展示")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Operation(summary = "获取今日数据", description = "获取今日数据接口")
    @GetMapping("/todayInfo")
    public Result<DashboardTodayRspVO> todayInfo() {
        return Result.success(dashboardService.todayInfo());
    }

}
