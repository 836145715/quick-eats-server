package com.zmx.quickserver.controller.admin;

import com.zmx.common.annotation.ApiLog;
import com.zmx.common.response.PageResult;
import com.zmx.common.response.Result;
import com.zmx.quickpojo.dto.OrderPageListReqDTO;
import com.zmx.quickpojo.dto.OrderStatusDTO;
import com.zmx.quickpojo.vo.OrderPageListRspVO;
import com.zmx.quickserver.service.OrdersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端订单控制器
 */
@RestController
@RequestMapping("/admin/order")
@Slf4j
@Tag(name = "管理端订单接口", description = "管理端订单相关接口")
public class AdminOrderController {

    @Autowired
    private OrdersService ordersService;

    /**
     * 订单搜索
     *
     * @param dto 查询参数
     * @return 分页结果
     */
    @PostMapping("/conditionSearch")
    @ApiLog
    @Operation(summary = "订单搜索", description = "管理端订单条件搜索接口")
    public PageResult<OrderPageListRspVO> conditionSearch(@RequestBody OrderPageListReqDTO dto) {
        log.info("订单搜索：{}", dto);
        return ordersService.pageList(dto);
    }

    /**
     * 各个状态的订单数量统计
     *
     * @return 统计结果
     */
    @GetMapping("/statistics")
    @ApiLog
    @Operation(summary = "各个状态的订单数量统计", description = "统计各个状态的订单数量接口")
    public Result<Object> statistics() {
        log.info("各个状态的订单数量统计");
        
        // TODO: 实现订单状态统计逻辑
        // 统计待接单、已接单、派送中等各个状态的订单数量
        
        return Result.success();
    }

    /**
     * 查询订单详情
     *
     * @param id 订单ID
     * @return 订单详情
     */
    @GetMapping("/details/{id}")
    @ApiLog
    @Operation(summary = "查询订单详情", description = "根据订单ID查询订单详情接口")
    public Result<OrderPageListRspVO> details(@PathVariable Long id) {
        log.info("查询订单详情：{}", id);
        return ordersService.getById(id);
    }

    /**
     * 接单
     *
     * @param statusDTO 状态信息
     * @return 操作结果
     */
    @PutMapping("/confirm")
    @ApiLog
    @Operation(summary = "接单", description = "商家接单接口")
    public Result<Void> confirm(@RequestBody @Valid OrderStatusDTO statusDTO) {
        log.info("接单：{}", statusDTO);
        return ordersService.confirmOrder(statusDTO);
    }

    /**
     * 拒单
     *
     * @param statusDTO 状态信息
     * @return 操作结果
     */
    @PutMapping("/rejection")
    @ApiLog
    @Operation(summary = "拒单", description = "商家拒单接口")
    public Result<Void> rejection(@RequestBody @Valid OrderStatusDTO statusDTO) {
        log.info("拒单：{}", statusDTO);
        return ordersService.rejectOrder(statusDTO);
    }

    /**
     * 取消订单
     *
     * @param statusDTO 状态信息
     * @return 操作结果
     */
    @PutMapping("/cancel")
    @ApiLog
    @Operation(summary = "取消订单", description = "商家取消订单接口")
    public Result<Void> cancel(@RequestBody @Valid OrderStatusDTO statusDTO) {
        log.info("取消订单：{}", statusDTO);
        return ordersService.cancelOrder(statusDTO.getId(), statusDTO.getCancelReason());
    }

    /**
     * 派送订单
     *
     * @param id 订单ID
     * @return 操作结果
     */
    @PutMapping("/delivery/{id}")
    @ApiLog
    @Operation(summary = "派送订单", description = "商家派送订单接口")
    public Result<Void> delivery(@PathVariable Long id) {
        log.info("派送订单：{}", id);
        return ordersService.deliverOrder(id);
    }

    /**
     * 完成订单
     *
     * @param id 订单ID
     * @return 操作结果
     */
    @PutMapping("/complete/{id}")
    @ApiLog
    @Operation(summary = "完成订单", description = "商家完成订单接口")
    public Result<Void> complete(@PathVariable Long id) {
        log.info("完成订单：{}", id);
        return ordersService.completeOrder(id);
    }
}
